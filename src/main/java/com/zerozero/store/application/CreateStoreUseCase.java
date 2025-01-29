package com.zerozero.store.application;

import com.zerozero.core.application.BaseRequest;
import com.zerozero.core.application.BaseResponse;
import com.zerozero.core.application.BaseUseCase;
import com.zerozero.core.domain.entity.Store;
import com.zerozero.core.domain.entity.User;
import com.zerozero.core.domain.infra.repository.StoreJPARepository;
import com.zerozero.core.exception.DomainException;
import com.zerozero.core.exception.error.BaseErrorCode;
import com.zerozero.external.kakao.search.application.RequestKakaoKeywordSearchUseCase;
import com.zerozero.external.kakao.search.application.RequestKakaoKeywordSearchUseCase.RequestKakaoKeywordSearchRequest;
import com.zerozero.external.kakao.search.dto.KeywordSearchResponse;
import com.zerozero.external.kakao.search.dto.KeywordSearchResponse.Document;
import com.zerozero.store.application.CreateStoreUseCase.CreateStoreRequest;
import com.zerozero.store.application.CreateStoreUseCase.CreateStoreResponse;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Log4j2
@Service
@RequiredArgsConstructor
@Transactional
public class CreateStoreUseCase implements BaseUseCase<CreateStoreRequest, CreateStoreResponse> {

  private final RequestKakaoKeywordSearchUseCase requestKakaoKeywordSearchUseCase;

  private final StoreJPARepository storeJPARepository;

  @Override
  public CreateStoreResponse execute(CreateStoreRequest request) {
    if (request == null || !request.isValid()) {
      log.error("[CreateStoreUseCase] Invalid request");
      return CreateStoreResponse.builder()
          .success(false)
          .errorCode(CreateStoreErrorCode.NOT_EXIST_REQUEST_CONDITION)
          .build();
    }
    User user = request.getUser();
    KeywordSearchResponse keywordSearchResponse = requestKakaoKeywordSearchUseCase.execute(
            RequestKakaoKeywordSearchRequest.builder()
                .query(request.getPlaceName())
                .build())
        .getKeywordSearchResponse();
    if (keywordSearchResponse == null || keywordSearchResponse.getMeta().getTotalCount() == 0) {
      log.error("[CreateStoreUseCase] Search response is null");
      return CreateStoreResponse.builder()
          .success(false)
          .errorCode(CreateStoreErrorCode.NOT_EXIST_SEARCH_RESPONSE)
          .build();
    }
    Document matchedStore = Arrays.stream(keywordSearchResponse.getDocuments()).filter(
        document -> document != null && request.getPlaceName().equals(document.getPlaceName())
            && request.getLongitude().equals(document.getX()) && request.getLatitude()
            .equals(document.getY())).findFirst().orElse(null);
    if (matchedStore == null) {
      log.error("[CreateStoreUseCase] Store not found");
      return CreateStoreResponse.builder()
          .success(false)
          .errorCode(CreateStoreErrorCode.NOT_EXIST_STORE)
          .build();
    }
    Store store = Store.of(user.getId(), matchedStore, request.getImages());
    if (store == null) {
      return CreateStoreResponse.builder()
          .success(false)
          .errorCode(CreateStoreErrorCode.FAILED_CREATE_STORE)
          .build();
    }
    storeJPARepository.save(store);
    return CreateStoreResponse.builder().storeId(store.getId()).build();
  }

  @Getter
  @RequiredArgsConstructor
  public enum CreateStoreErrorCode implements BaseErrorCode<DomainException> {
    NOT_EXIST_REQUEST_CONDITION(HttpStatus.BAD_REQUEST, "등록 요청 조건이 올바르지 않습니다."),
    NOT_EXIST_SEARCH_RESPONSE(HttpStatus.BAD_REQUEST, "검색 응답이 존재하지 않습니다."),
    NOT_EXIST_STORE(HttpStatus.BAD_REQUEST, "등록된 판매점이 존재하지 않습니다."),
    FAILED_CREATE_STORE(HttpStatus.INTERNAL_SERVER_ERROR, "판매점 등록에 실패하였습니다."),
    ;

    private final HttpStatus httpStatus;

    private final String message;

    @Override
    public DomainException toException() {
      return new DomainException(httpStatus, this);
    }
  }

  @ToString
  @Getter
  @Setter
  @SuperBuilder
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  public static class CreateStoreResponse extends BaseResponse<CreateStoreErrorCode> {

    private UUID storeId;
  }

  @ToString
  @Getter
  @Setter
  @Builder
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  @AllArgsConstructor(access = AccessLevel.PROTECTED)
  public static class CreateStoreRequest implements BaseRequest {

    private String placeName;

    private String longitude;

    private String latitude;

    private List<String> images;

    private User user;

    @Override
    public boolean isValid() {
      return placeName != null && longitude != null && !longitude.isEmpty() && latitude != null
          && !latitude.isEmpty() && images != null && !images.isEmpty()
          && user != null;
    }
  }

}

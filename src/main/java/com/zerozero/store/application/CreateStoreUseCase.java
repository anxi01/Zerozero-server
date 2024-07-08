package com.zerozero.store.application;

import com.zerozero.core.application.BaseRequest;
import com.zerozero.core.application.BaseResponse;
import com.zerozero.core.application.BaseUseCase;
import com.zerozero.core.domain.entity.Store;
import com.zerozero.core.domain.entity.User;
import com.zerozero.core.domain.infra.repository.StoreJPARepository;
import com.zerozero.core.domain.infra.repository.UserJPARepository;
import com.zerozero.core.domain.vo.AccessToken;
import com.zerozero.core.exception.DomainException;
import com.zerozero.core.exception.error.BaseErrorCode;
import com.zerozero.core.util.AWSS3Service;
import com.zerozero.core.util.JwtUtil;
import com.zerozero.external.naver.search.application.SearchNaverLocalUseCase;
import com.zerozero.external.naver.search.application.SearchNaverLocalUseCase.SearchNaverLocalRequest;
import com.zerozero.external.naver.search.dto.SearchLocalResponse;
import com.zerozero.external.naver.search.dto.SearchLocalResponse.SearchLocalItem;
import com.zerozero.store.application.CreateStoreUseCase.CreateStoreRequest;
import com.zerozero.store.application.CreateStoreUseCase.CreateStoreResponse;
import io.jsonwebtoken.io.IOException;
import java.util.List;
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
import org.springframework.web.multipart.MultipartFile;

@Log4j2
@Service
@RequiredArgsConstructor
@Transactional
public class CreateStoreUseCase implements BaseUseCase<CreateStoreRequest, CreateStoreResponse> {

  private final JwtUtil jwtUtil;

  private final AWSS3Service awss3Service;

  private final SearchNaverLocalUseCase searchNaverLocalUseCase;

  private final UserJPARepository userJPARepository;

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
    AccessToken accessToken = request.getAccessToken();
    if (jwtUtil.isTokenExpired(accessToken.getToken())) {
      log.error("[CreateStoreUseCase] Expired access token");
      return CreateStoreResponse.builder()
          .success(false)
          .errorCode(CreateStoreErrorCode.EXPIRED_TOKEN)
          .build();
    }
    String userEmail = jwtUtil.extractUsername(accessToken.getToken());
    User user = userJPARepository.findByEmail(userEmail);
    if (user == null) {
      log.error("[CreateStoreUseCase] not found user with email {}", userEmail);
      return CreateStoreResponse.builder()
          .success(false)
          .errorCode(CreateStoreErrorCode.NOT_EXIST_USER)
          .build();
    }
    SearchLocalResponse searchLocalResponse = searchNaverLocalUseCase.execute(
        SearchNaverLocalRequest.builder().query(request.getTitle()).build()).getResponse();
    if (searchLocalResponse == null || searchLocalResponse.getTotal() == 0) {
      log.error("[CreateStoreUseCase] Search response is null");
      return CreateStoreResponse.builder()
          .success(false)
          .errorCode(CreateStoreErrorCode.NOT_EXIST_SEARCH_RESPONSE)
          .build();
    }
    SearchLocalItem matchedStore = searchLocalResponse.getItems().stream().filter(
            item -> item.getTitle().equals(request.getTitle()) && item.getMapx() == request.getMapx()
                && item.getMapy() == request.getMapy())
        .findFirst()
        .orElse(null);
    if (matchedStore == null) {
      log.error("[CreateStoreUseCase] Store not found");
      return CreateStoreResponse.builder()
          .success(false)
          .errorCode(CreateStoreErrorCode.NOT_EXIST_STORE)
          .build();
    }
    List<String> imageUrls;
    try {
      imageUrls = awss3Service.uploadImages(request.getImageFiles());
      if (imageUrls == null || imageUrls.isEmpty()) {
        log.error("[CreateStoreUseCase] Failed to upload images");
        return CreateStoreResponse.builder()
            .success(false)
            .errorCode(CreateStoreErrorCode.FAILED_IMAGE_CONVERT)
            .build();
      }
    } catch (IOException e) {
      return CreateStoreResponse.builder()
          .success(false)
          .errorCode(CreateStoreErrorCode.FAILED_IMAGE_CONVERT)
          .build();
    }
    Store store = Store.of(user.getId(), matchedStore, imageUrls);
    if (store == null) {
      return CreateStoreResponse.builder()
          .success(false)
          .errorCode(CreateStoreErrorCode.FAILED_CREATE_STORE)
          .build();
    }
    storeJPARepository.save(store);
    return CreateStoreResponse.builder().build();
  }

  @Getter
  @RequiredArgsConstructor
  public enum CreateStoreErrorCode implements BaseErrorCode<DomainException> {
    NOT_EXIST_REQUEST_CONDITION(HttpStatus.BAD_REQUEST, "등록 요청 조건이 올바르지 않습니다."),
    EXPIRED_TOKEN(HttpStatus.BAD_REQUEST, "만료된 토큰입니다."),
    NOT_EXIST_USER(HttpStatus.BAD_REQUEST, "존재하지 않는 사용자입니다."),
    NOT_EXIST_SEARCH_RESPONSE(HttpStatus.BAD_REQUEST, "검색 응답이 존재하지 않습니다."),
    NOT_EXIST_STORE(HttpStatus.BAD_REQUEST, "등록된 판매점이 존재하지 않습니다."),
    FAILED_IMAGE_CONVERT(HttpStatus.INTERNAL_SERVER_ERROR, "이미지 변환에 실패하였습니다."),
    FAILED_CREATE_STORE(HttpStatus.INTERNAL_SERVER_ERROR, "판매점 등록에 실패하였습니다.");

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
  }

  @ToString
  @Getter
  @Setter
  @Builder
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  @AllArgsConstructor(access = AccessLevel.PROTECTED)
  public static class CreateStoreRequest implements BaseRequest {

    private String title;

    private int mapx;

    private int mapy;

    private List<MultipartFile> imageFiles;

    private AccessToken accessToken;

    @Override
    public boolean isValid() {
      return title != null && mapx != 0 && mapy != 0 && imageFiles != null && !imageFiles.isEmpty() && accessToken != null;
    }
  }

}

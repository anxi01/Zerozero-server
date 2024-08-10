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
import com.zerozero.core.util.JwtUtil;
import com.zerozero.store.application.ReadStoreInfoUseCase.ReadStoreInfoRequest;
import com.zerozero.store.application.ReadStoreInfoUseCase.ReadStoreInfoResponse;
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
@Transactional(readOnly = true)
public class ReadStoreInfoUseCase implements BaseUseCase<ReadStoreInfoRequest, ReadStoreInfoResponse> {

  private final JwtUtil jwtUtil;

  private final UserJPARepository userJPARepository;

  private final StoreJPARepository storeJPARepository;

  @Override
  public ReadStoreInfoResponse execute(ReadStoreInfoRequest request) {
    if (request == null || !request.isValid()) {
      log.error("[ReadStoreInfoUseCase] Invalid request");
      return ReadStoreInfoResponse.builder()
          .success(false)
          .errorCode(ReadStoreInfoErrorCode.NOT_EXIST_REQUEST_CONDITION)
          .build();
    }
    AccessToken accessToken = request.getAccessToken();
    if (jwtUtil.isTokenExpired(accessToken.getToken())) {
      log.error("[ReadStoreInfoUseCase] Expired access token");
      return ReadStoreInfoResponse.builder().success(false)
          .errorCode(ReadStoreInfoErrorCode.EXPIRED_TOKEN)
          .build();
    }
    String userEmail = jwtUtil.extractUsername(accessToken.getToken());
    User user = userJPARepository.findByEmail(userEmail);
    if (user == null) {
      log.error("[ReadStoreInfoUseCase] not found user with email {}", userEmail);
      return ReadStoreInfoResponse.builder().success(false)
          .errorCode(ReadStoreInfoErrorCode.NOT_EXIST_USER)
          .build();
    }
    Store store = storeJPARepository.findById(request.getStoreId()).orElse(null);
    if (store == null) {
      log.error("[ReadStoreInfoUseCase] Store not found");
      return ReadStoreInfoResponse.builder()
          .success(false)
          .errorCode(ReadStoreInfoErrorCode.NOT_EXIST_STORE)
          .build();
    }
    com.zerozero.core.domain.vo.Store storeVO = com.zerozero.core.domain.vo.Store.of(store);
    return ReadStoreInfoResponse.builder().store(storeVO).build();
  }

  @Getter
  @RequiredArgsConstructor
  public enum ReadStoreInfoErrorCode implements BaseErrorCode<DomainException> {
    NOT_EXIST_REQUEST_CONDITION(HttpStatus.BAD_REQUEST, "검색 요청 조건이 올바르지 않습니다."),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "만료된 토큰입니다."),
    NOT_EXIST_USER(HttpStatus.BAD_REQUEST, "존재하지 않는 사용자입니다."),
    NOT_EXIST_SEARCH_RESPONSE(HttpStatus.BAD_REQUEST, "검색 응답이 존재하지 않습니다."),
    NOT_EXIST_STORE(HttpStatus.BAD_REQUEST, "등록된 판매점이 존재하지 않습니다.");

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
  @AllArgsConstructor(access = AccessLevel.PROTECTED)
  public static class ReadStoreInfoResponse extends BaseResponse<ReadStoreInfoErrorCode> {

    private com.zerozero.core.domain.vo.Store store;
  }

  @ToString
  @Getter
  @Setter
  @Builder
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  @AllArgsConstructor(access = AccessLevel.PROTECTED)
  public static class ReadStoreInfoRequest implements BaseRequest {

    private UUID storeId;

    private AccessToken accessToken;

    @Override
    public boolean isValid() {
      return storeId != null && accessToken != null;
    }
  }

}

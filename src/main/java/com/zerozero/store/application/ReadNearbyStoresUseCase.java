package com.zerozero.store.application;

import com.zerozero.core.application.BaseRequest;
import com.zerozero.core.application.BaseResponse;
import com.zerozero.core.application.BaseUseCase;
import com.zerozero.core.domain.entity.User;
import com.zerozero.core.domain.infra.mongodb.store.Store;
import com.zerozero.core.domain.infra.mongodb.store.StoreMongoRepository;
import com.zerozero.core.domain.infra.repository.UserJPARepository;
import com.zerozero.core.domain.vo.AccessToken;
import com.zerozero.core.exception.DomainException;
import com.zerozero.core.exception.error.BaseErrorCode;
import com.zerozero.core.util.JwtUtil;
import com.zerozero.store.application.ReadNearbyStoresUseCase.ReadNearbyStoresRequest;
import com.zerozero.store.application.ReadNearbyStoresUseCase.ReadNearbyStoresResponse;
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

@Log4j2
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReadNearbyStoresUseCase implements BaseUseCase<ReadNearbyStoresRequest, ReadNearbyStoresResponse> {

  private final JwtUtil jwtUtil;

  private final UserJPARepository userJPARepository;

  private final StoreMongoRepository storeMongoRepository;

  public static final Double DEFAULT_RADIUS = 100.0;

  @Override
  public ReadNearbyStoresResponse execute(ReadNearbyStoresRequest request) {
    if (request == null || !request.isValid()) {
      log.error("[ReadNearbyStoresUseCase] Invalid request");
      return ReadNearbyStoresResponse.builder()
          .success(false)
          .errorCode(ReadNearbyStoresErrorCode.NOT_EXIST_REQUEST_CONDITION)
          .build();
    }
    AccessToken accessToken = request.getAccessToken();
    if (jwtUtil.isTokenExpired(accessToken.getToken())) {
      log.error("[ReadNearbyStoresUseCase] Expired access token");
      return ReadNearbyStoresResponse.builder()
          .success(false)
          .errorCode(ReadNearbyStoresErrorCode.EXPIRED_TOKEN)
          .build();
    }
    String userEmail = jwtUtil.extractUsername(accessToken.getToken());
    User user = userJPARepository.findByEmail(userEmail);
    if (user == null) {
      log.error("[ReadNearbyStoresUseCase] not found user with email {}", userEmail);
      return ReadNearbyStoresResponse.builder()
          .success(false)
          .errorCode(ReadNearbyStoresErrorCode.NOT_EXIST_USER)
          .build();
    }
    List<Store> stores = storeMongoRepository.findStoresWithinCoordinatesRadius(request.getLongitude(), request.getLatitude(), DEFAULT_RADIUS);
    return ReadNearbyStoresResponse.builder()
        .success(true)
        .stores(stores)
        .build();
  }

  @Getter
  @RequiredArgsConstructor
  public enum ReadNearbyStoresErrorCode implements BaseErrorCode<DomainException> {
    NOT_EXIST_REQUEST_CONDITION(HttpStatus.BAD_REQUEST, "검색 요청 조건이 올바르지 않습니다."),
    EXPIRED_TOKEN(HttpStatus.BAD_REQUEST, "만료된 토큰입니다."),
    NOT_EXIST_USER(HttpStatus.BAD_REQUEST, "존재하지 않는 사용자입니다.");

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
  public static class ReadNearbyStoresResponse extends BaseResponse<ReadNearbyStoresErrorCode> {

    private List<Store> stores;
  }

  @ToString
  @Getter
  @Setter
  @Builder
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  @AllArgsConstructor(access = AccessLevel.PROTECTED)
  public static class ReadNearbyStoresRequest implements BaseRequest {

    private Double longitude;

    private Double latitude;

    private AccessToken accessToken;

    @Override
    public boolean isValid() {
      return longitude != null && latitude != null && accessToken != null;
    }
  }

}

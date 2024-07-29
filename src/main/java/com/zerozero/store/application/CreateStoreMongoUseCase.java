package com.zerozero.store.application;

import com.zerozero.core.application.BaseRequest;
import com.zerozero.core.application.BaseResponse;
import com.zerozero.core.application.BaseUseCase;
import com.zerozero.core.domain.entity.Store;
import com.zerozero.core.domain.infra.mongodb.store.StoreMongoRepository;
import com.zerozero.core.domain.infra.repository.StoreJPARepository;
import com.zerozero.core.exception.DomainException;
import com.zerozero.core.exception.error.BaseErrorCode;
import com.zerozero.store.application.CreateStoreMongoUseCase.CreateStoreMongoRequest;
import com.zerozero.store.application.CreateStoreMongoUseCase.CreateStoreMongoResponse;
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
public class CreateStoreMongoUseCase implements BaseUseCase<CreateStoreMongoRequest, CreateStoreMongoResponse> {

  private final StoreJPARepository storeJPARepository;

  private final StoreMongoRepository storeMongoRepository;

  @Override
  public CreateStoreMongoResponse execute(CreateStoreMongoRequest request) {
    if (request == null || !request.isValid()) {
      log.error("[CreateStoreMongoUseCase] Invalid request");
      return CreateStoreMongoResponse.builder()
          .success(false)
          .errorCode(CreateStoreMongoErrorCode.NOT_EXIST_REQUEST_CONDITION)
          .build();
    }
    Store store = storeJPARepository.findById(request.getStoreId()).orElse(null);
    if (store == null) {
      log.error("[CreateStoreMongoUseCase] Store not found");
      return CreateStoreMongoResponse.builder()
          .success(false)
          .errorCode(CreateStoreMongoErrorCode.NOT_EXIST_STORE)
          .build();
    }
    com.zerozero.core.domain.infra.mongodb.store.Store storeMongo = com.zerozero.core.domain.infra.mongodb.store.Store.of(store);
    if (storeMongo == null) {
      log.error("[CreateStoreMongoUseCase] Failed to create store");
      return CreateStoreMongoResponse.builder()
          .success(false)
          .errorCode(CreateStoreMongoErrorCode.FAILED_STORE_CREATE)
          .build();
    }
    storeMongoRepository.save(storeMongo);
    return CreateStoreMongoResponse.builder().build();
  }

  @Getter
  @RequiredArgsConstructor
  public enum CreateStoreMongoErrorCode implements BaseErrorCode<DomainException> {
    NOT_EXIST_REQUEST_CONDITION(HttpStatus.BAD_REQUEST, "판매점 생성 요청 조건이 존재하지 않습니다."),
    NOT_EXIST_STORE(HttpStatus.BAD_REQUEST, "판매점이 존재하지 않습니다."),
    FAILED_STORE_CREATE(HttpStatus.INTERNAL_SERVER_ERROR, "판매점 생성에 실패하였습니다.");

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
  public static class CreateStoreMongoResponse extends BaseResponse<CreateStoreMongoErrorCode> {
  }

  @ToString
  @Getter
  @Setter
  @Builder
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  @AllArgsConstructor(access = AccessLevel.PROTECTED)
  public static class CreateStoreMongoRequest implements BaseRequest {

    private UUID storeId;

    @Override
    public boolean isValid() {
      return storeId != null;
    }
  }

}

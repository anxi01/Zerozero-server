package com.zerozero.user.application;

import com.zerozero.core.application.BaseRequest;
import com.zerozero.core.application.BaseResponse;
import com.zerozero.core.application.BaseUseCase;
import com.zerozero.core.domain.entity.User;
import com.zerozero.core.domain.infra.repository.StoreJPARepository;
import com.zerozero.core.domain.vo.Store;
import com.zerozero.core.exception.DomainException;
import com.zerozero.core.exception.error.BaseErrorCode;
import com.zerozero.user.application.ReadUserStoresUseCase.ReadUserStoresRequest;
import com.zerozero.user.application.ReadUserStoresUseCase.ReadUserStoresResponse;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReadUserStoresUseCase implements BaseUseCase<ReadUserStoresRequest, ReadUserStoresResponse> {

  private final StoreJPARepository storeJPARepository;

  @Override
  public ReadUserStoresResponse execute(ReadUserStoresRequest request) {
    if (request == null || !request.isValid()) {
      log.error("[ReadUserStoresUseCase] Invalid request");
      return ReadUserStoresResponse.builder().success(false)
          .errorCode(ReadUserStoresErrorCode.NOT_EXIST_REQUEST_CONDITION).build();
    }
    User user = request.getUser();
    List<Store> stores = storeJPARepository.findAllByUserId(user.getId()).stream()
        .map(com.zerozero.core.domain.vo.Store::of).filter(Objects::nonNull)
        .collect(Collectors.toList());
    return ReadUserStoresResponse.builder().stores(stores).build();
  }

  @Getter
  @RequiredArgsConstructor
  public enum ReadUserStoresErrorCode implements BaseErrorCode<DomainException> {
    NOT_EXIST_REQUEST_CONDITION(HttpStatus.BAD_REQUEST, "요청 조건이 올바르지 않습니다."),
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
  @AllArgsConstructor(access = AccessLevel.PROTECTED)
  public static class ReadUserStoresResponse extends BaseResponse<ReadUserStoresErrorCode> {

    private List<Store> stores;
  }

  @ToString
  @Getter
  @Setter
  @Builder
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  @AllArgsConstructor(access = AccessLevel.PROTECTED)
  public static class ReadUserStoresRequest implements BaseRequest {

    private User user;

    @Override
    public boolean isValid() {
      return user != null;
    }
  }

}

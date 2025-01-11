package com.zerozero.auth.error;

import com.zerozero.core.exception.DomainException;
import com.zerozero.core.exception.error.BaseErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AuthenticationErrorCode implements BaseErrorCode<DomainException> {

  NOT_EXIST_HEADER(HttpStatus.UNAUTHORIZED, "Authorization Header가 존재하지 않습니다."),
  NOT_EXIST_TOKEN(HttpStatus.UNAUTHORIZED, "Authorization Header에 Token이 존재하지 않습니다."),
  NOT_MATCH_TOKEN_FORMAT(HttpStatus.UNAUTHORIZED, "토큰의 형식이 맞지 않습니다."),
  NOT_DEFINE_TOKEN(HttpStatus.UNAUTHORIZED, "정의되지 않은 토큰입니다."),
  EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "만료된 토큰입니다."),
  NOT_FOUND_MEMBER(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
  ;

  private final HttpStatus httpStatus;

  private final String message;

  @Override
  public DomainException toException() {
    return new DomainException(httpStatus, this);
  }
}

package com.zerozero.user.application;

import com.zerozero.core.application.BaseRequest;
import com.zerozero.core.application.BaseResponse;
import com.zerozero.core.application.BaseUseCase;
import com.zerozero.core.domain.entity.User;
import com.zerozero.core.domain.vo.Image;
import com.zerozero.core.exception.DomainException;
import com.zerozero.core.exception.error.BaseErrorCode;
import com.zerozero.user.application.UpdateUserProfileUseCase.UpdateUserProfileRequest;
import com.zerozero.user.application.UpdateUserProfileUseCase.UpdateUserProfileResponse;
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
public class UpdateUserProfileUseCase implements BaseUseCase<UpdateUserProfileRequest, UpdateUserProfileResponse> {

  @Override
  public UpdateUserProfileResponse execute(UpdateUserProfileRequest request) {
    if (request == null || !request.isValid()) {
      log.error("[UpdateUserProfileUseCase] Invalid request");
      return UpdateUserProfileResponse.builder().success(false)
          .errorCode(UpdateUserProfileErrorCode.NOT_EXIST_REQUEST_CONDITION)
          .build();
    }
    User user = request.getUser();
    String image = request.getImage();
    if (image == null) {
      user.uploadProfileImage(null);
    } else {
      user.uploadProfileImage(Image.convertUrlToImage(image));
    }
    if (!request.getNickname().equals(user.getNickname())) {
      user.updateNickname(request.getNickname());
    }
    return UpdateUserProfileResponse.builder().build();
  }

  @Getter
  @RequiredArgsConstructor
  public enum UpdateUserProfileErrorCode implements BaseErrorCode<DomainException> {
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
  public static class UpdateUserProfileResponse extends BaseResponse<UpdateUserProfileErrorCode> {
  }

  @ToString
  @Getter
  @Setter
  @Builder
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  @AllArgsConstructor(access = AccessLevel.PROTECTED)
  public static class UpdateUserProfileRequest implements BaseRequest {

    private String nickname;

    private String image;

    private User user;

    @Override
    public boolean isValid() {
      return nickname != null && user != null;
    }
  }

}

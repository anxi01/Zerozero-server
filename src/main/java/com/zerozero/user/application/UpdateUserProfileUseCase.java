package com.zerozero.user.application;

import com.zerozero.core.application.BaseRequest;
import com.zerozero.core.application.BaseResponse;
import com.zerozero.core.application.BaseUseCase;
import com.zerozero.core.domain.entity.User;
import com.zerozero.core.domain.infra.repository.UserJPARepository;
import com.zerozero.core.domain.vo.AccessToken;
import com.zerozero.core.domain.vo.Image;
import com.zerozero.core.exception.DomainException;
import com.zerozero.core.exception.error.BaseErrorCode;
import com.zerozero.core.util.AWSS3Service;
import com.zerozero.core.util.JwtUtil;
import com.zerozero.user.application.UpdateUserProfileUseCase.UpdateUserProfileRequest;
import com.zerozero.user.application.UpdateUserProfileUseCase.UpdateUserProfileResponse;
import java.io.IOException;
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
public class UpdateUserProfileUseCase implements BaseUseCase<UpdateUserProfileRequest, UpdateUserProfileResponse> {

  private final JwtUtil jwtUtil;

  private final AWSS3Service awss3Service;

  private final UserJPARepository userJPARepository;

  @Override
  public UpdateUserProfileResponse execute(UpdateUserProfileRequest request) {
    if (request == null || !request.isValid()) {
      log.error("[UpdateUserProfileUseCase] Invalid request");
      return UpdateUserProfileResponse.builder().success(false)
          .errorCode(UpdateUserProfileErrorCode.NOT_EXIST_REQUEST_CONDITION)
          .build();
    }
    AccessToken accessToken = request.getAccessToken();
    if (jwtUtil.isTokenExpired(accessToken.getToken())) {
      log.error("[UpdateUserProfileUseCase] Expired access token");
      return UpdateUserProfileResponse.builder().success(false)
          .errorCode(UpdateUserProfileErrorCode.EXPIRED_TOKEN).build();
    }
    String userEmail = jwtUtil.extractUsername(accessToken.getToken());
    User user = userJPARepository.findByEmail(userEmail);
    if (user == null) {
      log.error("[UpdateUserProfileUseCase] not found user with email {}", userEmail);
      return UpdateUserProfileResponse.builder().success(false)
          .errorCode(UpdateUserProfileErrorCode.NOT_EXIST_USER).build();
    }
    MultipartFile imageFile = request.getImageFile();
    if (imageFile == null) {
      user.uploadProfileImage(null);
    } else {
      String imageUrl;
      try {
        imageUrl = awss3Service.uploadImage(imageFile);
        if (imageUrl == null || imageUrl.isEmpty()) {
          log.error("[UpdateUserProfileUseCase] imageUrl is null");
          return UpdateUserProfileResponse.builder().success(false)
              .errorCode(UpdateUserProfileErrorCode.FAILED_IMAGE_CONVERT).build();
        }
      } catch (IOException e) {
        log.error("[UpdateUserProfileUseCase] image upload error", e);
        return UpdateUserProfileResponse.builder().success(false)
            .errorCode(UpdateUserProfileErrorCode.FAILED_IMAGE_CONVERT).build();
      }
      user.uploadProfileImage(Image.convertUrlToImage(imageUrl));
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
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "만료된 토큰입니다."),
    NOT_EXIST_USER(HttpStatus.BAD_REQUEST, "존재하지 않는 사용자입니다."),
    FAILED_IMAGE_CONVERT(HttpStatus.INTERNAL_SERVER_ERROR, "이미지 변환에 실패하였습니다.");

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

    private MultipartFile imageFile;

    private AccessToken accessToken;

    @Override
    public boolean isValid() {
      return nickname != null && accessToken != null;
    }
  }

}

package com.zerozero.user.presentation;

import com.zerozero.core.application.BaseResponse;
import com.zerozero.core.domain.record.UserProfile;
import com.zerozero.core.domain.vo.AccessToken;
import com.zerozero.core.exception.error.GlobalErrorCode;
import com.zerozero.user.application.ReadUserInfoUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "User", description = "사용자")
public class ReadUserInfoController {

  private final ReadUserInfoUseCase readUserInfoUseCase;

  @Operation(
      summary = "마이페이지 조회 API",
      description = "사용자의 마이페이지를 토큰을 통해 조회합니다.",
      operationId = "/user/mypage"
  )
  @GetMapping("/user/mypage")
  public ResponseEntity<ReadUserInfoResponse> readUserInfo(@Parameter(hidden = true) AccessToken accessToken) {
    ReadUserInfoUseCase.ReadUserInfoResponse readUserInfoResponse = readUserInfoUseCase.execute(
        ReadUserInfoUseCase.ReadUserInfoRequest.builder()
            .accessToken(accessToken)
            .build());
    if (readUserInfoResponse == null || !readUserInfoResponse.isSuccess()) {
      Optional.ofNullable(readUserInfoResponse)
          .map(BaseResponse::getErrorCode)
          .ifPresentOrElse(errorCode -> {
            throw errorCode.toException();
          }, () -> {
            throw GlobalErrorCode.INTERNAL_ERROR.toException();
          });
    }
    return ResponseEntity.ok(ReadUserInfoResponse.builder()
        .userProfile(Optional.ofNullable(readUserInfoResponse.getUserProfile())
            .map(
                userProfile -> new UserProfile(userProfile.getNickname(),
                    userProfile.getProfileImage(),
                    userProfile.getRank(), userProfile.getStoreReportCount())).orElse(null)).build()
    );
  }

  @ToString
  @Getter
  @Setter
  @SuperBuilder
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  @AllArgsConstructor(access = AccessLevel.PROTECTED)
  @Schema(description = "사용자 마이페이지 조회 응답")
  public static class ReadUserInfoResponse extends BaseResponse<GlobalErrorCode> {

    @Schema(description = "사용자 정보")
    private UserProfile userProfile;
  }
}

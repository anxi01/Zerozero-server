package com.zerozero.domain.user.api;

import com.zerozero.domain.user.application.UserService;
import com.zerozero.domain.user.dto.UserStoreRankDTO;
import com.zerozero.domain.user.dto.response.UserInfoResponse;
import com.zerozero.global.common.dto.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import java.security.Principal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "User Controller", description = "사용자 관련 API입니다.")
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  @Operation(summary = "마이페이지 조회", description = "로그인한 사용자의 정보를 조회합니다.")
  @GetMapping("/my-page")
  public ApiResponse<UserInfoResponse> getMyInfo(Principal connectedUser) {
    return ApiResponse.ok(userService.getUserInfo(connectedUser));
  }

  @Operation(summary = "Top10 랭크 조회", description = "판매점 등록을 많이 한 사용자 순으로 10명 조회합니다.")
  @GetMapping("/rank")
  public ApiResponse<List<UserStoreRankDTO>> getTop10Users() {
    return ApiResponse.ok(userService.getTop10UsersByStoreCount());
  }

  @Operation(summary = "프로필 사진 업로드", description = "프로필 사진을 업로드합니다.")
  @PostMapping("/upload-profile")
  public ApiResponse<String> uploadProfileImage(Principal connectedUser, @RequestPart MultipartFile profileImage)
      throws IOException {
    userService.uploadProfileImage(connectedUser, profileImage);
    return ApiResponse.ok();
  }
}

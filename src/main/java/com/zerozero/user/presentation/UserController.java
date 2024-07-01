package com.zerozero.user.presentation;

import com.zerozero.store.StoreReviewResponse.StoreInfoResponse;
import com.zerozero.user.application.UserService;
import com.zerozero.user.UserInfoResponse;
import com.zerozero.core.presentation.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import java.security.Principal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
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

  @Operation(summary = "프로필 사진 업로드", description = "프로필 사진을 업로드합니다.")
  @PostMapping(value = "/upload-profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ApiResponse<String> uploadProfileImage(Principal connectedUser,
      @RequestPart MultipartFile profileImage)
      throws IOException {
    return ApiResponse.ok(userService.uploadProfileImage(connectedUser, profileImage));
  }

  @Operation(summary = "등록한 판매점 리스트 조회", description = "자신이 등록한 판매점의 리스트를 조회합니다.")
  @GetMapping("/storeList")
  public ApiResponse<List<StoreInfoResponse>> getMyStores(Principal connectedUser) {
    return ApiResponse.ok(userService.getMyStores(connectedUser));
  }
}
package com.zerozero.user.controller;

import com.zerozero.common.response.ApiResponse;
import com.zerozero.user.dto.response.UserInfoResponse;
import com.zerozero.user.service.UserService;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  @GetMapping("/my-page")
  public ApiResponse<UserInfoResponse> getMyInfo(Principal connectedUser) {
    return ApiResponse.ok(userService.getUserInfo(connectedUser));
  }

}

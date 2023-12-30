package com.zerozero.controller;

import com.zerozero.response.UserInfoResponse;
import com.zerozero.service.UserService;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  @GetMapping("/my-page")
  public ResponseEntity<UserInfoResponse> getMyInfo(Principal connectedUser) {
    return ResponseEntity.ok(userService.getUserInfo(connectedUser));
  }

}
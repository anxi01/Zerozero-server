package com.zerozero.auth;

import com.zerozero.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

  private final AuthenticationService service;

  /* 회원가입 */
  @PostMapping("/register")
  public ApiResponse<AuthenticationResponse> register(@RequestBody RegisterRequest request) {
    return ApiResponse.ok(service.register(request));
  }

  /* 로그인 */
  @PostMapping("/authenticate")
  public ApiResponse<AuthenticationResponse> authenticate(
      @RequestBody AuthenticationRequest request) {
    return ApiResponse.ok(service.authenticate(request));
  }

  /* 이메일 중복 체크 */
  @GetMapping("/check-email/{email}")
  public ApiResponse<String> checkEmail(@PathVariable String email) {
    return ApiResponse.ok(service.checkEmail(email));
  }

  /* 닉네임 중복 체크 */
  @GetMapping("/check-nickname/{nickname}")
  public ApiResponse<String> checkNickname(@PathVariable String nickname) {
    return ApiResponse.ok(service.checkNickname(nickname));
  }
}

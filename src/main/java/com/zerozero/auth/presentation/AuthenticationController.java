package com.zerozero.auth.presentation;

import com.zerozero.auth.application.AuthenticationService;
import com.zerozero.auth.AuthenticationRequest;
import com.zerozero.auth.RegisterRequest;
import com.zerozero.auth.AuthenticationResponse;
import com.zerozero.core.presentation.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "인증/인가 controller", description = "회원 가입, 로그인 관련 API입니다.")
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

  private final AuthenticationService service;

  @Operation(summary = "회원가입")
  @PostMapping("/register")
  public ApiResponse<AuthenticationResponse> register(@RequestBody RegisterRequest request) {
    return ApiResponse.ok(service.register(request));
  }

  @Operation(summary = "로그인", description = "로그인한 사용자의 토큰을 이용하여 API를 사용할 수 있습니다.")
  @PostMapping("/authenticate")
  public ApiResponse<AuthenticationResponse> authenticate(
      @RequestBody AuthenticationRequest request) {
    return ApiResponse.ok(service.authenticate(request));
  }

  @Operation(summary = "이메일 중복체크")
  @GetMapping("/check-email/{email}")
  public ApiResponse<String> checkEmail(@PathVariable String email) {
    service.checkEmail(email);
    return ApiResponse.ok();
  }

  @Operation(summary = "닉네임 중복체크")
  @GetMapping("/check-nickname/{nickname}")
  public ApiResponse<String> checkNickname(@PathVariable String nickname) {
    service.checkNickname(nickname);
    return ApiResponse.ok();
  }
}
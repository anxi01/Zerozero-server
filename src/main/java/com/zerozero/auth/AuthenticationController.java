package com.zerozero.auth;

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
  public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request) {
    return ResponseEntity.ok(service.register(request));
  }

  /* 로그인 */
  @PostMapping("/authenticate")
  public ResponseEntity<AuthenticationResponse> authenticate(
      @RequestBody AuthenticationRequest request) {
    return ResponseEntity.ok(service.authenticate(request));
  }

  /* 이메일 중복 체크 */
  @GetMapping("/check-email/{email}")
  public ResponseEntity<String> checkEmail(@PathVariable String email) {
    return ResponseEntity.ok(service.checkEmail(email));
  }

  /* 닉네임 중복 체크 */
  @GetMapping("/check-nickname/{nickname}")
  public ResponseEntity<String> checkNickname(@PathVariable String nickname) {
    return ResponseEntity.ok(service.checkNickname(nickname));
  }
}

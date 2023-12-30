package com.zerozero.auth;

import com.zerozero.config.JwtService;
import com.zerozero.user.entity.Role;
import com.zerozero.user.entity.User;
import com.zerozero.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;

  public AuthenticationResponse register(RegisterRequest request) {
    User user = User.builder()
        .nickname(request.getNickname())
        .email(request.getEmail())
        .password(passwordEncoder.encode(request.getPassword()))
        .role(Role.USER)
        .build();

    userRepository.save(user);
    String jwtToken = jwtService.generateToken(user);

    return AuthenticationResponse.builder()
        .token(jwtToken)
        .build();
  }

  public AuthenticationResponse authenticate(AuthenticationRequest request) {
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

    User user = userRepository.findByEmail(request.getEmail()).orElseThrow();
    String jwtToken = jwtService.generateToken(user);

    return AuthenticationResponse.builder()
        .token(jwtToken)
        .build();
  }

  public String checkEmail(String email) {
    if (userRepository.existsByEmail(email)) {
      throw new IllegalArgumentException("이메일이 중복됩니다.");
    } else {
      return "사용 가능한 이메일입니다.";
    }
  }

  public String checkNickname(String nickname) {
    if (userRepository.existsByNickname(nickname)) {
      throw new IllegalArgumentException("닉네임이 중복됩니다.");
    } else {
      return "사용 가능한 닉네임입니다.";
    }
  }
}

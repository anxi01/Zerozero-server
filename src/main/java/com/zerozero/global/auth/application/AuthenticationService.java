package com.zerozero.global.auth.application;

import com.zerozero.domain.user.domain.Role;
import com.zerozero.domain.user.domain.User;
import com.zerozero.domain.user.repository.UserRepository;
import com.zerozero.global.auth.dto.request.AuthenticationRequest;
import com.zerozero.global.auth.dto.request.RegisterRequest;
import com.zerozero.global.auth.dto.response.AuthenticationResponse;
import com.zerozero.global.auth.exception.DuplicateEmailException;
import com.zerozero.global.auth.exception.DuplicateNicknameException;
import com.zerozero.global.auth.exception.UserNotFoundException;
import com.zerozero.global.config.security.JwtService;
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

    User user = userRepository.findByEmail(request.getEmail()).orElseThrow(UserNotFoundException::new);
    String jwtToken = jwtService.generateToken(user);

    return AuthenticationResponse.builder()
        .token(jwtToken)
        .build();
  }

  public void checkEmail(String email) {
    if (userRepository.existsByEmail(email)) {
      throw new DuplicateEmailException();
    }
  }

  public void checkNickname(String nickname) {
    if (userRepository.existsByNickname(nickname)) {
      throw new DuplicateNicknameException();
    }
  }
}

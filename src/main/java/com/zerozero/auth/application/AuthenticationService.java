package com.zerozero.auth.application;

import com.zerozero.auth.AuthenticationRequest;
import com.zerozero.user.Role;
import com.zerozero.core.domain.entity.User;
import com.zerozero.core.domain.infra.repository.UserJPARepository;
import com.zerozero.auth.RegisterRequest;
import com.zerozero.auth.AuthenticationResponse;
import com.zerozero.auth.DuplicateEmailException;
import com.zerozero.auth.DuplicateNicknameException;
import com.zerozero.auth.UserNotFoundException;
import com.zerozero.core.util.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

  private final UserJPARepository userJPARepository;
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

    userJPARepository.save(user);
    String jwtToken = jwtService.generateToken(user);

    return AuthenticationResponse.builder()
        .token(jwtToken)
        .build();
  }

  public AuthenticationResponse authenticate(AuthenticationRequest request) {
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

    User user = userJPARepository.findByEmail(request.getEmail()).orElseThrow(UserNotFoundException::new);
    String jwtToken = jwtService.generateToken(user);

    return AuthenticationResponse.builder()
        .token(jwtToken)
        .build();
  }

  public void checkEmail(String email) {
    if (userJPARepository.existsByEmail(email)) {
      throw new DuplicateEmailException();
    }
  }

  public void checkNickname(String nickname) {
    if (userJPARepository.existsByNickname(nickname)) {
      throw new DuplicateNicknameException();
    }
  }
}

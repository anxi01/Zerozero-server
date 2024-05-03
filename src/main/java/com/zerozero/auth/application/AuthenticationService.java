package com.zerozero.auth.application;

import com.zerozero.auth.AuthenticationRequest;
import com.zerozero.auth.AuthenticationResponse;
import com.zerozero.auth.DuplicateEmailException;
import com.zerozero.auth.DuplicateNicknameException;
import com.zerozero.auth.InvalidTokenException;
import com.zerozero.auth.RegisterRequest;
import com.zerozero.auth.TokenDto;
import com.zerozero.auth.UserNotFoundException;
import com.zerozero.core.domain.entity.RefreshToken;
import com.zerozero.core.domain.entity.User;
import com.zerozero.core.domain.infra.repository.RefreshTokenRepository;
import com.zerozero.core.domain.infra.repository.UserJPARepository;
import com.zerozero.core.util.JwtService;
import com.zerozero.user.Role;
import java.util.Optional;
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
  private final RefreshTokenRepository refreshTokenRepository;

  public AuthenticationResponse register(RegisterRequest request) {
    User user = User.builder()
        .nickname(request.getNickname())
        .email(request.getEmail())
        .password(passwordEncoder.encode(request.getPassword()))
        .role(Role.USER)
        .build();

    userJPARepository.save(user);
    TokenDto tokens = jwtService.generateToken(user);

    return AuthenticationResponse.builder()
        .accessToken(tokens.getAccessToken())
        .refreshToken(tokens.getRefreshToken())
        .build();
  }

  public AuthenticationResponse authenticate(AuthenticationRequest request) {
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

    User user = userJPARepository.findByEmail(request.getEmail()).orElseThrow(UserNotFoundException::new);
    TokenDto tokens = jwtService.generateToken(user);

    Optional<RefreshToken> refreshToken = refreshTokenRepository.findByUser(user);

    if (refreshToken.isPresent()) {
      refreshTokenRepository.save(refreshToken.get().update(tokens.getRefreshToken()));
    } else {
      RefreshToken newRefreshToken = new RefreshToken(tokens.getRefreshToken(), user);
      refreshTokenRepository.save(newRefreshToken);
    }

    return AuthenticationResponse.builder()
        .accessToken(tokens.getAccessToken())
        .refreshToken(tokens.getRefreshToken())
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

  public AuthenticationResponse refreshToken(String refreshToken) {

    String userEmail = jwtService.extractUsername(refreshToken);
    if (userEmail != null) {
      User user = userJPARepository.findByEmail(userEmail).orElseThrow(UserNotFoundException::new);
      String storedRefreshToken = refreshTokenRepository.findByUser(user).orElseThrow(
          InvalidTokenException::new).getRefreshToken();
      if (storedRefreshToken.equals(refreshToken) && jwtService.isTokenValid(refreshToken, user)) {
        TokenDto tokens = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
            .accessToken(tokens.getAccessToken())
            .refreshToken(refreshToken)
            .build();
      }
    }
    throw new InvalidTokenException();
  }
}

package com.zerozero.service;

import com.zerozero.domain.User;
import com.zerozero.response.UserInfoResponse;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

  public UserInfoResponse getUserInfo(Principal connectedUser) {

    User user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

    return UserInfoResponse.builder()
        .nickname(user.getNickname())
        .stores(user.getStores())
        .build();
  }

}

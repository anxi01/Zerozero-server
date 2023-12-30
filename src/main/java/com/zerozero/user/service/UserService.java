package com.zerozero.user.service;

import com.zerozero.user.dto.response.UserInfoResponse;
import com.zerozero.user.entity.User;
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

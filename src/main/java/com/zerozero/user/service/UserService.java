package com.zerozero.user.service;

import com.zerozero.store.dto.response.StoreInfoResponse;
import com.zerozero.store.repository.StoreRepository;
import com.zerozero.user.dto.response.UserInfoResponse;
import com.zerozero.user.entity.User;
import java.security.Principal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

  private final StoreRepository storeRepository;

  public UserInfoResponse getUserInfo(Principal connectedUser) {

    User user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

    List<StoreInfoResponse> stores = storeRepository.findStoresByUser(user)
        .stream().map(StoreInfoResponse::from).toList();

    return UserInfoResponse.builder()
        .nickname(user.getNickname())
        .stores(stores)
        .build();
  }
}

package com.zerozero.domain.user.application;

import com.zerozero.domain.store.dto.response.StoreInfoResponse;
import com.zerozero.domain.store.repository.StoreRepository;
import com.zerozero.domain.user.domain.User;
import com.zerozero.domain.user.dto.response.UserInfoResponse;
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

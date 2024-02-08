package com.zerozero.domain.user.application;

import com.zerozero.domain.store.application.S3Service;
import com.zerozero.domain.store.dto.response.StoreInfoResponse;
import com.zerozero.domain.store.repository.StoreRepository;
import com.zerozero.domain.user.domain.User;
import com.zerozero.domain.user.dto.UserStoreRankDTO;
import com.zerozero.domain.user.dto.response.UserInfoResponse;
import com.zerozero.domain.user.repository.UserRepository;
import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

  private final UserRepository userRepository;
  private final StoreRepository storeRepository;
  private final S3Service s3Service;

  public UserInfoResponse getUserInfo(Principal connectedUser) {

    User user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

    List<StoreInfoResponse> stores = storeRepository.findStoresByUser(user)
        .stream().map(StoreInfoResponse::from).toList();

    return UserInfoResponse.builder()
        .nickname(user.getNickname())
        .stores(stores)
        .build();
  }

  public List<UserStoreRankDTO> getTop10UsersByStoreCount() {
    List<Object[]> allRankInfos = storeRepository.countStoresByUserId();

    List<UserStoreRankDTO> top10RankInfos = new ArrayList<>();

    int count = Math.min(10, allRankInfos.size());

    for (int i = 0; i < count; i++) {
      User user = userRepository.findById((long) allRankInfos.get(i)[0])
          .orElseThrow(() -> new IllegalArgumentException("판매점을 등록한 사용자가 없습니다."));

      top10RankInfos.add(new UserStoreRankDTO(user.getNickname(), (Long) allRankInfos.get(i)[1]));
    }
    return top10RankInfos;
  }

  public void uploadProfileImage(Principal connectedUser, MultipartFile profileImage)
      throws IOException {

    User user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

    String imageUrl = s3Service.uploadImage(profileImage);

    user.uploadProfileImage(imageUrl);
    userRepository.save(user);
  }
}
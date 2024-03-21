package com.zerozero.domain.user.application;

import com.zerozero.domain.store.domain.Store;
import com.zerozero.domain.store.dto.response.StoreReviewResponse.StoreInfoResponse;
import com.zerozero.global.s3.application.S3Service;
import com.zerozero.domain.store.repository.StoreRepository;
import com.zerozero.domain.user.domain.User;
import com.zerozero.domain.user.dto.response.UserInfoResponse;
import com.zerozero.domain.user.repository.UserRepository;
import java.io.IOException;
import java.security.Principal;
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

    List<Object[]> allRankInfos = storeRepository.countStoresByUserId();

    long userRank = calculateUserRank(allRankInfos, user.getId());

    long storeReportCountByUser = storeRepository.countStoresByUser(user);

    return UserInfoResponse.builder()
        .profileImage(user.getProfileImage())
        .nickname(user.getNickname())
        .rank(userRank)
        .storeReportCount(storeReportCountByUser)
        .build();
  }

  public String uploadProfileImage(Principal connectedUser, MultipartFile profileImage)
      throws IOException {

    User user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

    String imageUrl = s3Service.uploadImage(profileImage);

    user.uploadProfileImage(imageUrl);
    userRepository.save(user);

    return user.getProfileImage();
  }

  private long calculateUserRank(List<Object[]> allRankInfos, Long userId) {

    for (int i = 0; i < allRankInfos.size(); i++) {
      Object[] rankInfo = allRankInfos.get(i);
      Long currentUserId = (Long) rankInfo[0];
      if (userId.equals(currentUserId)) {
        return i + 1;
      }
    }
    return -1;
  }

  public List<StoreInfoResponse> getMyStores(Principal connectedUser) {

    User user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

    List<Store> stores = storeRepository.findAllByUser(user);

    return stores.stream().map(StoreInfoResponse::from).toList();
  }
}
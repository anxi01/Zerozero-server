package com.zerozero.user.application;

import com.zerozero.core.domain.entity.Store;
import com.zerozero.store.StoreReviewResponse.StoreInfoResponse;
import com.zerozero.core.util.AWSS3Service;
import com.zerozero.core.domain.infra.repository.StoreJPARepository;
import com.zerozero.core.domain.entity.User;
import com.zerozero.user.UserInfoResponse;
import com.zerozero.core.domain.infra.repository.UserJPARepository;
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

  private final UserJPARepository userJPARepository;
  private final StoreJPARepository storeJPARepository;
  private final AWSS3Service AWSS3Service;

  public UserInfoResponse getUserInfo(Principal connectedUser) {

    User user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

    List<Object[]> allRankInfos = storeJPARepository.countStoresByUserId();

    long userRank = calculateUserRank(allRankInfos, user.getId());

    long storeReportCountByUser = storeJPARepository.countStoresByUser(user);

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

    String imageUrl = AWSS3Service.uploadImage(profileImage);

    user.uploadProfileImage(imageUrl);
    userJPARepository.save(user);

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

    List<Store> stores = storeJPARepository.findAllByUser(user);

    return stores.stream().map(StoreInfoResponse::from).toList();
  }
}
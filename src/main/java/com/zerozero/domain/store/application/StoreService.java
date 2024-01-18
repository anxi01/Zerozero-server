package com.zerozero.domain.store.application;

import com.zerozero.domain.naver.NaverClient;
import com.zerozero.domain.naver.dto.request.SearchLocalRequest;
import com.zerozero.domain.store.domain.Store;
import com.zerozero.domain.store.dto.request.RegisterStoreRequest;
import com.zerozero.domain.store.dto.response.StoreInfoResponse;
import com.zerozero.domain.store.dto.response.StoreListResponse;
import com.zerozero.domain.store.repository.StoreRepository;
import com.zerozero.domain.user.domain.User;
import java.security.Principal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class StoreService {

  private final NaverClient naverClient;
  private final StoreRepository storeRepository;
  private final S3Service s3Service;

  public StoreListResponse search(String query) {

    // 지역 검색
    var searchLocalRequest = new SearchLocalRequest();
    searchLocalRequest.setQuery(query);

    var searchLocalResponse = naverClient.localSearch(searchLocalRequest);

    /* 식당 존재 확인 */
    if (searchLocalResponse.getTotal() > 0) {
      return StoreListResponse.builder()
          .items(searchLocalResponse.getItems())
          .build();
    } else {
      throw new IllegalArgumentException("해당 음식점이 존재하지 않습니다.");
    }
  }

  public StoreInfoResponse add(Principal connectedUser, String query, RegisterStoreRequest request,
      List<MultipartFile> images) {

    var storeList = search(query);
    var storeItem = storeList.getItems().stream()
        .filter(s ->
            s.getTitle().equals(request.getTitle()) &&
                s.getMapx() == request.getMapx() &&
                s.getMapy() == request.getMapy()
        )
        .findFirst()
        .orElseThrow(IllegalArgumentException::new);

    var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
    List<String> uploadImages = s3Service.uploadImages(images);
    Store store = Store.of(user, storeItem, uploadImages);

    storeRepository.save(store);
    return StoreInfoResponse.from(store);
  }

  public StoreInfoResponse getStoreInfo(Long storeId) {
    Store store = storeRepository.findById(storeId)
        .orElseThrow(() -> new IllegalArgumentException("판매점이 존재하지 않습니다."));

    return StoreInfoResponse.from(store);
  }
}

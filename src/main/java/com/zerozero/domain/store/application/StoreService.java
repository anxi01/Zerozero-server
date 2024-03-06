package com.zerozero.domain.store.application;

import com.zerozero.domain.naver.NaverClient;
import com.zerozero.domain.naver.dto.request.SearchLocalRequest;
import com.zerozero.domain.naver.dto.response.SearchLocalResponse.SearchLocalItem;
import com.zerozero.domain.store.domain.Store;
import com.zerozero.domain.store.dto.request.RegisterStoreRequest;
import com.zerozero.domain.store.dto.response.StoreInfoResponse;
import com.zerozero.domain.store.dto.response.StoreListResponse;
import com.zerozero.domain.store.exception.StoreNotFoundException;
import com.zerozero.domain.store.repository.StoreRepository;
import com.zerozero.domain.user.domain.User;
import com.zerozero.global.s3.application.S3Service;
import java.security.Principal;
import java.util.ArrayList;
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

    /* 가게 존재 확인 */
    if (searchLocalResponse.getTotal() > 0) {
      List<SearchLocalItem> storeInfos = new ArrayList<>();

      for (SearchLocalItem item : searchLocalResponse.getItems()) {
        boolean isSelling = storeRepository.existsByNameAndMapxAndMapyAndSellingIsTrue(
            item.getTitle(), item.getMapx(), item.getMapy());

        if (isSelling) {
          item.setSellingTrue();
        }

        storeInfos.add(item);
      }

      return StoreListResponse.builder()
          .items(storeInfos)
          .build();
    }
    throw new StoreNotFoundException();
  }

  public StoreInfoResponse add(Principal connectedUser, RegisterStoreRequest request,
      List<MultipartFile> images) {

    var storeList = search(request.getTitle());
    var storeItem = storeList.getItems().stream()
        .filter(s ->
            s.getTitle().equals(request.getTitle()) &&
                s.getMapx() == request.getMapx() &&
                s.getMapy() == request.getMapy()
        )
        .findFirst()
        .orElseThrow(StoreNotFoundException::new);

    var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
    List<String> uploadImages = s3Service.uploadImages(images);
    Store store = Store.of(user, storeItem, uploadImages);

    storeRepository.save(store);
    return StoreInfoResponse.from(store);
  }

  public StoreInfoResponse getStoreInfo(Long storeId) {
    Store store = storeRepository.findById(storeId).orElseThrow(StoreNotFoundException::new);
    return StoreInfoResponse.from(store);
  }
}

package com.zerozero.store.application;

import com.zerozero.core.domain.entity.Review;
import com.zerozero.core.domain.entity.Store;
import com.zerozero.core.domain.entity.User;
import com.zerozero.core.domain.infra.repository.ReviewJPARepository;
import com.zerozero.core.domain.infra.repository.ReviewLikeJPARepository;
import com.zerozero.core.domain.infra.repository.StoreJPARepository;
import com.zerozero.core.domain.vo.ZeroDrink;
import com.zerozero.core.util.AWSS3Service;
import com.zerozero.external.naver.NaverClient;
import com.zerozero.external.naver.SearchLocalRequest;
import com.zerozero.external.naver.SearchLocalResponse.SearchLocalItem;
import com.zerozero.store.RegisterStoreRequest;
import com.zerozero.store.StoreListResponse;
import com.zerozero.store.StoreNotFoundException;
import com.zerozero.store.StoreReviewResponse;
import com.zerozero.store.StoreReviewResponse.StoreInfoResponse;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class StoreService {

  private final static String SORT_BY_RECENT = "LATEST";
  private final static String SORT_BY_LIKE_DESC = "LIKE_DESC";

  private final NaverClient naverClient;
  private final StoreJPARepository storeJPARepository;
  private final AWSS3Service AWSS3Service;
  private final ReviewJPARepository reviewJPARepository;
  private final ReviewLikeJPARepository reviewLikeJPARepository;

  public StoreListResponse search(String query) {

    // 지역 검색
    var searchLocalRequest = new SearchLocalRequest();
    searchLocalRequest.setQuery(query);

    var searchLocalResponse = naverClient.localSearch(searchLocalRequest);

    /* 가게 존재 확인 */
    if (searchLocalResponse.getTotal() > 0) {
      List<SearchLocalItem> storeInfos = new ArrayList<>();

      for (SearchLocalItem item : searchLocalResponse.getItems()) {
        boolean isSelling = storeJPARepository.existsByNameAndMapxAndMapyAndSellingIsTrue(
            item.getTitle(), item.getMapx(), item.getMapy());

        if (isSelling) {
          item.setSellingTrue();

          Store store = storeJPARepository.findByNameAndMapxAndMapyAndSellingIsTrue(
              item.getTitle(), item.getMapx(), item.getMapy());

          item.setStoreId(store.getId());
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
    List<String> uploadImages = AWSS3Service.uploadImages(images);
    Store store = Store.of(user, storeItem, uploadImages);

    storeJPARepository.save(store);
    return StoreInfoResponse.from(store);
  }

  public StoreReviewResponse getStoreInfo(UUID storeId, String sortBy) {
    Store store = storeJPARepository.findById(storeId).orElseThrow(StoreNotFoundException::new);

    List<Review> reviews = reviewJPARepository.findAllByStore(store);
    sortReviews(reviews, sortBy);

    List<Integer> likeCounts = getLikeCounts(reviews);

    List<ZeroDrink> top3ZeroDrinks = getTop3ZeroDrinks(reviews);

    return StoreReviewResponse.of(store, reviews, likeCounts, top3ZeroDrinks);
  }

  private void sortReviews(List<Review> reviews, String sortBy) {
    if (sortBy.equalsIgnoreCase(SORT_BY_RECENT)) {
      reviews.sort(Comparator.comparing(Review::getCreatedAt).reversed());
    } else if (sortBy.equalsIgnoreCase(SORT_BY_LIKE_DESC)) {
      reviews.sort(Comparator.comparing(review -> -reviewLikeJPARepository.countByReview(review)));
    }
  }

  private List<Integer> getLikeCounts(List<Review> reviews) {
    List<Integer> likeCounts = new ArrayList<>();
    for (Review review : reviews) {
      int likeCount = reviewLikeJPARepository.countByReview(review);
      likeCounts.add(likeCount);
    }
    return likeCounts;
  }

  private List<ZeroDrink> getTop3ZeroDrinks(List<Review> reviews) {
    // 모든 리뷰의 zeroDrinks를 하나의 리스트로 모음
    List<ZeroDrink> allZeroDrinks = reviews.stream()
        .flatMap(review -> review.getZeroDrinks().stream())
        .toList();

    // zeroDrinks 출현 빈도를 계산
    Map<ZeroDrink, Long> zeroDrinksFrequency = allZeroDrinks.stream()
        .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

    // 출현 빈도를 기준으로 내림차순으로 정렬
    List<Map.Entry<ZeroDrink, Long>> sortedZeroDrinksFrequency = zeroDrinksFrequency.entrySet().stream()
        .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
        .toList();

    // 정렬된 결과를 순위로 변환하여 저장
    Map<ZeroDrink, Integer> zeroDrinksRanking = new LinkedHashMap<>();
    int rank = 1;
    for (Map.Entry<ZeroDrink, Long> entry : sortedZeroDrinksFrequency) {
      zeroDrinksRanking.put(entry.getKey(), rank++);
    }

    // 순위를 List<ZeroDrinks>로 변환하여 저장
    List<ZeroDrink> top3ZeroDrinks = new ArrayList<>();
    for (int i = 1; i <= 3; i++) {
      for (Map.Entry<ZeroDrink, Integer> entry : zeroDrinksRanking.entrySet()) {
        if (entry.getValue() == i) {
          top3ZeroDrinks.add(entry.getKey());
        }
      }
    }
    return top3ZeroDrinks;
  }
}
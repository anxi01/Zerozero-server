package com.zerozero.domain.store.application;

import com.zerozero.domain.naver.NaverClient;
import com.zerozero.domain.naver.dto.request.SearchLocalRequest;
import com.zerozero.domain.naver.dto.response.SearchLocalResponse.SearchLocalItem;
import com.zerozero.domain.review.domain.Review;
import com.zerozero.domain.store.domain.Store;
import com.zerozero.domain.review.domain.ZeroDrinks;
import com.zerozero.domain.store.dto.request.RegisterStoreRequest;
import com.zerozero.domain.store.dto.response.StoreListResponse;
import com.zerozero.domain.store.dto.response.StoreReviewResponse;
import com.zerozero.domain.store.dto.response.StoreReviewResponse.StoreInfoResponse;
import com.zerozero.domain.store.exception.StoreNotFoundException;
import com.zerozero.domain.review.repository.ReviewLikeRepository;
import com.zerozero.domain.review.repository.ReviewRepository;
import com.zerozero.domain.store.repository.StoreRepository;
import com.zerozero.domain.user.domain.User;
import com.zerozero.global.s3.application.S3Service;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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
  private final StoreRepository storeRepository;
  private final S3Service s3Service;
  private final ReviewRepository reviewRepository;
  private final ReviewLikeRepository reviewLikeRepository;

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

  public StoreReviewResponse getStoreInfo(Long storeId, String sortBy) {
    Store store = storeRepository.findById(storeId).orElseThrow(StoreNotFoundException::new);

    List<Review> reviews = reviewRepository.findAllByStore(store);
    sortReviews(reviews, sortBy);

    List<Integer> likeCounts = getLikeCounts(reviews);

    List<ZeroDrinks> top3ZeroDrinks = getTop3ZeroDrinks(reviews);

    return StoreReviewResponse.of(store, reviews, likeCounts, top3ZeroDrinks);
  }

  private void sortReviews(List<Review> reviews, String sortBy) {
    if (sortBy.equalsIgnoreCase(SORT_BY_RECENT)) {
      reviews.sort(Comparator.comparing(Review::getCreatedAt).reversed());
    } else if (sortBy.equalsIgnoreCase(SORT_BY_LIKE_DESC)) {
      reviews.sort(Comparator.comparing(review -> -reviewLikeRepository.countByReview(review)));
    }
  }

  private List<Integer> getLikeCounts(List<Review> reviews) {
    List<Integer> likeCounts = new ArrayList<>();
    for (Review review : reviews) {
      int likeCount = reviewLikeRepository.countByReview(review);
      likeCounts.add(likeCount);
    }
    return likeCounts;
  }

  private List<ZeroDrinks> getTop3ZeroDrinks(List<Review> reviews) {
    // 모든 리뷰의 zeroDrinks를 하나의 리스트로 모음
    List<ZeroDrinks> allZeroDrinks = reviews.stream()
        .flatMap(review -> review.getZeroDrinks().stream())
        .toList();

    // zeroDrinks 출현 빈도를 계산
    Map<ZeroDrinks, Long> zeroDrinksFrequency = allZeroDrinks.stream()
        .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

    // 출현 빈도를 기준으로 내림차순으로 정렬
    List<Map.Entry<ZeroDrinks, Long>> sortedZeroDrinksFrequency = zeroDrinksFrequency.entrySet().stream()
        .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
        .toList();

    // 정렬된 결과를 순위로 변환하여 저장
    Map<ZeroDrinks, Integer> zeroDrinksRanking = new LinkedHashMap<>();
    int rank = 1;
    for (Map.Entry<ZeroDrinks, Long> entry : sortedZeroDrinksFrequency) {
      zeroDrinksRanking.put(entry.getKey(), rank++);
    }

    // 순위를 List<ZeroDrinks>로 변환하여 저장
    List<ZeroDrinks> top3ZeroDrinks = new ArrayList<>();
    for (int i = 1; i <= 3; i++) {
      for (Map.Entry<ZeroDrinks, Integer> entry : zeroDrinksRanking.entrySet()) {
        if (entry.getValue() == i) {
          top3ZeroDrinks.add(entry.getKey());
        }
      }
    }
    return top3ZeroDrinks;
  }
}
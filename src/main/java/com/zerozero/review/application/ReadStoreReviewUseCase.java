package com.zerozero.review.application;

import com.zerozero.core.application.BaseRequest;
import com.zerozero.core.application.BaseResponse;
import com.zerozero.core.application.BaseUseCase;
import com.zerozero.core.domain.entity.Review;
import com.zerozero.core.domain.entity.Review.Filter;
import com.zerozero.core.domain.infra.repository.ReviewJPARepository;
import com.zerozero.core.domain.infra.repository.ReviewLikeJPARepository;
import com.zerozero.core.domain.infra.repository.UserJPARepository;
import com.zerozero.core.domain.vo.AccessToken;
import com.zerozero.core.domain.vo.User;
import com.zerozero.core.exception.DomainException;
import com.zerozero.core.exception.error.BaseErrorCode;
import com.zerozero.core.util.JwtUtil;
import com.zerozero.review.application.ReadStoreReviewUseCase.ReadStoreReviewRequest;
import com.zerozero.review.application.ReadStoreReviewUseCase.ReadStoreReviewResponse;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Log4j2
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReadStoreReviewUseCase implements BaseUseCase<ReadStoreReviewRequest, ReadStoreReviewResponse> {

  private final ReviewJPARepository reviewJPARepository;

  private final ReviewLikeJPARepository reviewLikeJPARepository;

  private final UserJPARepository userJPARepository;

  private final JwtUtil jwtUtil;

  @Override
  public ReadStoreReviewResponse execute(ReadStoreReviewRequest request) {
    if (request == null || !request.isValid()) {
      log.error("[ReadStoreReviewUseCase] Invalid request");
      return ReadStoreReviewResponse.builder()
          .success(false)
          .errorCode(ReadStoreReviewErrorCode.NOT_EXIST_REQUEST_CONDITION)
          .build();
    }
    AccessToken accessToken = request.getAccessToken();
    if (jwtUtil.isTokenExpired(accessToken.getToken())) {
      log.error("[ReadStoreReviewUseCase] Expired access token");
      return ReadStoreReviewResponse.builder()
          .success(false)
          .errorCode(ReadStoreReviewErrorCode.EXPIRED_TOKEN)
          .build();
    }
    List<Review> reviews = reviewJPARepository.findAllByStoreIdAndDeleted(request.getStoreId(), false);
    List<Integer> reviewsCount = reviews.stream()
        .map(review -> reviewLikeJPARepository.countByReviewId(review.getId()))
        .collect(Collectors.toList());
    com.zerozero.core.domain.vo.Review[] reviewsVO = Review.filter(reviews, request.getFilter(),
            reviewsCount).stream().map(com.zerozero.core.domain.vo.Review::of)
        .toArray(com.zerozero.core.domain.vo.Review[]::new);
    return ReadStoreReviewResponse.builder().reviews(convertReviewResponse(reviewsVO)).build();
  }

  private ReadStoreReviewResponse.Review[] convertReviewResponse(com.zerozero.core.domain.vo.Review[] reviewsVO) {
    if (reviewsVO == null) {
      return null;
    }
    return Arrays.stream(reviewsVO).map(review ->
        ReadStoreReviewResponse.Review.builder().review(review)
            .user(User.of(userJPARepository.findById(review.getUserId()).orElse(null))).build()
    ).toArray(ReadStoreReviewResponse.Review[]::new);
  }

  @Getter
  @RequiredArgsConstructor
  public enum ReadStoreReviewErrorCode implements BaseErrorCode<DomainException> {
    NOT_EXIST_REQUEST_CONDITION(HttpStatus.BAD_REQUEST, "요청 조건이 올바르지 않습니다."),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "만료된 토큰입니다.");

    private final HttpStatus httpStatus;

    private final String message;

    @Override
    public DomainException toException() {
      return new DomainException(httpStatus, this);
    }
  }

  @ToString
  @Getter
  @Setter
  @SuperBuilder
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  @AllArgsConstructor(access = AccessLevel.PROTECTED)
  public static class ReadStoreReviewResponse extends BaseResponse<ReadStoreReviewErrorCode> {

    private Review[] reviews;

    @ToString
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Review {

      private com.zerozero.core.domain.vo.Review review;

      private User user;
    }
  }

  @ToString
  @Getter
  @Setter
  @Builder
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  @AllArgsConstructor(access = AccessLevel.PROTECTED)
  public static class ReadStoreReviewRequest implements BaseRequest {

    private UUID storeId;

    private Filter filter;

    private AccessToken accessToken;

    @Override
    public boolean isValid() {
      return storeId != null && accessToken != null;
    }
  }

}

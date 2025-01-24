package com.zerozero.review.application;

import com.zerozero.core.application.BaseRequest;
import com.zerozero.core.application.BaseResponse;
import com.zerozero.core.application.BaseUseCase;
import com.zerozero.core.domain.entity.Review;
import com.zerozero.core.domain.entity.Review.Filter;
import com.zerozero.core.domain.entity.User;
import com.zerozero.core.domain.infra.querydsl.ReviewQueryRepository;
import com.zerozero.core.domain.infra.repository.UserJPARepository;
import com.zerozero.core.exception.DomainException;
import com.zerozero.core.exception.error.BaseErrorCode;
import com.zerozero.review.application.ReadStoreReviewUseCase.ReadStoreReviewRequest;
import com.zerozero.review.application.ReadStoreReviewUseCase.ReadStoreReviewResponse;
import java.util.List;
import java.util.Map;
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

  private final ReviewQueryRepository reviewQueryRepository;

  private final UserJPARepository userJPARepository;

  @Override
  public ReadStoreReviewResponse execute(ReadStoreReviewRequest request) {
    if (request == null || !request.isValid()) {
      log.error("[ReadStoreReviewUseCase] Invalid request");
      return ReadStoreReviewResponse.builder()
          .success(false)
          .errorCode(ReadStoreReviewErrorCode.NOT_EXIST_REQUEST_CONDITION)
          .build();
    }
    User user = request.getUser();
    List<Review> reviews = reviewQueryRepository.findByStoreIdAndFilter(request.getStoreId(), request.getFilter());
    Map<UUID, User> reviewAuthorMap = getReviewAuthors(reviews);
    return ReadStoreReviewResponse.builder().reviews(convertReviewResponse(user, reviews, reviewAuthorMap)).build();
  }

  private ReadStoreReviewResponse.Review[] convertReviewResponse(User user, List<Review> reviews, Map<UUID, User> reviewAuthorMap) {
    if (user == null || reviews.isEmpty() || reviewAuthorMap.isEmpty()) {
      return null;
    }
    return reviews.stream()
        .map(review -> {
          User reviewAuthor = reviewAuthorMap.get(review.getUserId());
          return ReadStoreReviewResponse.Review.builder()
              .review(com.zerozero.core.domain.vo.Review.of(review))
              .user(com.zerozero.core.domain.vo.User.of(reviewAuthor))
              .likeCount(review.getReviewLikes().size())
              .isLiked(review.getReviewLikes().stream()
                  .anyMatch(like -> user.getId().equals(like.getUserId())))
              .build();
        })
        .toArray(ReadStoreReviewResponse.Review[]::new);
  }

  private Map<UUID, User> getReviewAuthors(List<Review> reviews) {
    return userJPARepository.findAllByIdInAndDeleted(
        reviews.stream()
            .map(Review::getUserId)
            .distinct()
            .collect(Collectors.toList()),
        false
    ).stream().collect(Collectors.toMap(User::getId, reviewAuthor -> reviewAuthor));
  }

  @Getter
  @RequiredArgsConstructor
  public enum ReadStoreReviewErrorCode implements BaseErrorCode<DomainException> {
    NOT_EXIST_REQUEST_CONDITION(HttpStatus.BAD_REQUEST, "요청 조건이 올바르지 않습니다."),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "만료된 토큰입니다."),
    NOT_EXIST_USER(HttpStatus.BAD_REQUEST, "존재하지 않는 사용자입니다.");

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

      private com.zerozero.core.domain.vo.User user;

      private Integer likeCount;

      private Boolean isLiked;
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

    private User user;

    @Override
    public boolean isValid() {
      return storeId != null && user != null;
    }
  }

}

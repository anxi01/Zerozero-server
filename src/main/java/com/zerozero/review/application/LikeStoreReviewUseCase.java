package com.zerozero.review.application;

import com.zerozero.core.application.BaseRequest;
import com.zerozero.core.application.BaseResponse;
import com.zerozero.core.application.BaseUseCase;
import com.zerozero.core.domain.entity.Review;
import com.zerozero.core.domain.entity.ReviewLike;
import com.zerozero.core.domain.entity.User;
import com.zerozero.core.domain.infra.repository.ReviewJPARepository;
import com.zerozero.core.domain.infra.repository.ReviewLikeJPARepository;
import com.zerozero.core.exception.DomainException;
import com.zerozero.core.exception.error.BaseErrorCode;
import com.zerozero.review.application.LikeStoreReviewUseCase.LikeStoreReviewRequest;
import com.zerozero.review.application.LikeStoreReviewUseCase.LikeStoreReviewResponse;
import java.util.UUID;
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
@Transactional
public class LikeStoreReviewUseCase implements BaseUseCase<LikeStoreReviewRequest, LikeStoreReviewResponse> {

  private final ReviewJPARepository reviewJPARepository;

  private final ReviewLikeJPARepository reviewLikeJPARepository;

  @Override
  public LikeStoreReviewResponse execute(LikeStoreReviewRequest request) {
    if (request == null || !request.isValid()) {
      log.error("[LikeStoreReviewUseCase] Invalid request");
      return LikeStoreReviewResponse.builder()
          .success(false)
          .errorCode(LikeStoreReviewErrorCode.NOT_EXIST_REVIEW_LIKE_CONDITION)
          .build();
    }
    User user = request.getUser();
    Review review = reviewJPARepository.findByIdAndDeleted(request.getReviewId(), false);
    if (review == null) {
      log.error("[LikeStoreReviewUseCase] not found review with id {}", request.getReviewId());
      return LikeStoreReviewResponse.builder()
          .success(false)
          .errorCode(LikeStoreReviewErrorCode.NOT_EXIST_DELETABLE_REVIEW)
          .build();
    }
    ReviewLike reviewLike = reviewLikeJPARepository.findByReviewIdAndUserIdAndDeleted(review.getId(), user.getId(), false);
    if (reviewLike == null) {
      reviewLike = new ReviewLike(review, user);
      reviewLikeJPARepository.save(reviewLike);
    } else {
      reviewLikeJPARepository.delete(reviewLike);
    }
    return LikeStoreReviewResponse.builder().build();
  }

  @Getter
  @RequiredArgsConstructor
  public enum LikeStoreReviewErrorCode implements BaseErrorCode<DomainException> {
    NOT_EXIST_REVIEW_LIKE_CONDITION(HttpStatus.BAD_REQUEST, "리뷰 좋아요 조건이 올바르지 않습니다."),
    NOT_EXIST_DELETABLE_REVIEW(HttpStatus.BAD_REQUEST, "삭제 가능한 리뷰가 존재하지 않습니다.");

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
  public static class LikeStoreReviewResponse extends BaseResponse<LikeStoreReviewErrorCode> {
  }

  @ToString
  @Getter
  @Setter
  @Builder
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  @AllArgsConstructor(access = AccessLevel.PROTECTED)
  public static class LikeStoreReviewRequest implements BaseRequest {

    private UUID reviewId;

    private User user;

    @Override
    public boolean isValid() {
      return reviewId != null && user != null;
    }
  }

}

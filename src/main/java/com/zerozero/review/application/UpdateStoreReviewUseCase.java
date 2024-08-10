package com.zerozero.review.application;

import com.zerozero.core.application.BaseRequest;
import com.zerozero.core.application.BaseResponse;
import com.zerozero.core.application.BaseUseCase;
import com.zerozero.core.domain.entity.Review;
import com.zerozero.core.domain.entity.User;
import com.zerozero.core.domain.infra.repository.ReviewJPARepository;
import com.zerozero.core.domain.infra.repository.UserJPARepository;
import com.zerozero.core.domain.vo.AccessToken;
import com.zerozero.core.domain.vo.ZeroDrink;
import com.zerozero.core.exception.DomainException;
import com.zerozero.core.exception.error.BaseErrorCode;
import com.zerozero.core.util.JwtUtil;
import com.zerozero.review.application.UpdateStoreReviewUseCase.UpdateStoreReviewRequest;
import com.zerozero.review.application.UpdateStoreReviewUseCase.UpdateStoreReviewResponse;
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
public class UpdateStoreReviewUseCase implements BaseUseCase<UpdateStoreReviewRequest, UpdateStoreReviewResponse> {

  private final JwtUtil jwtUtil;

  private final UserJPARepository userJPARepository;

  private final ReviewJPARepository reviewJPARepository;

  @Override
  public UpdateStoreReviewResponse execute(UpdateStoreReviewRequest request) {
    if (request == null || !request.isValid()) {
      log.error("[UpdateStoreReviewUseCase] Invalid request");
      return UpdateStoreReviewResponse.builder()
          .success(false)
          .errorCode(UpdateStoreReviewErrorCode.NOT_EXIST_REVIEW_CONDITION)
          .build();
    }
    AccessToken accessToken = request.getAccessToken();
    if (jwtUtil.isTokenExpired(accessToken.getToken())) {
      log.error("[UpdateStoreReviewUseCase] Expired access token");
      return UpdateStoreReviewResponse.builder()
          .success(false)
          .errorCode(UpdateStoreReviewErrorCode.EXPIRED_TOKEN)
          .build();
    }
    String userEmail = jwtUtil.extractUsername(accessToken.getToken());
    User user = userJPARepository.findByEmail(userEmail);
    if (user == null) {
      log.error("[UpdateStoreReviewUseCase] not found user with email {}", userEmail);
      return UpdateStoreReviewResponse.builder()
          .success(false)
          .errorCode(UpdateStoreReviewErrorCode.NOT_EXIST_USER)
          .build();
    }
    Review review = reviewJPARepository.findByIdAndDeleted(request.getReviewId(), false);
    if (review == null) {
      log.error("[UpdateStoreReviewUseCase] not found review with id {}", request.getReviewId());
      return UpdateStoreReviewResponse.builder()
          .success(false)
          .errorCode(UpdateStoreReviewErrorCode.NOT_EXIST_REVIEW)
          .build();
    }
    if (!review.hasUserReviewed(user)) {
      log.error("[UpdateStoreReviewUseCase] User does not have review with id {}", request.getReviewId());
      return UpdateStoreReviewResponse.builder()
          .success(false)
          .errorCode(UpdateStoreReviewErrorCode.USER_VALIDATION_FAILED)
          .build();
    }
    review.setContent(request.getContent());
    review.setZeroDrinks(request.getZeroDrinks());
    return UpdateStoreReviewResponse.builder().build();
  }

  @Getter
  @RequiredArgsConstructor
  public enum UpdateStoreReviewErrorCode implements BaseErrorCode<DomainException> {
    NOT_EXIST_REVIEW_CONDITION(HttpStatus.BAD_REQUEST, "리뷰 요청 조건이 올바르지 않습니다."),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "만료된 토큰입니다."),
    NOT_EXIST_USER(HttpStatus.BAD_REQUEST, "존재하지 않는 사용자입니다."),
    NOT_EXIST_REVIEW(HttpStatus.BAD_REQUEST, "리뷰가 존재하지 않습니다."),
    USER_VALIDATION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "리뷰를 작성한 사용자가 아닙니다.");
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
  public static class UpdateStoreReviewResponse extends BaseResponse<UpdateStoreReviewErrorCode> {
  }

  @ToString
  @Getter
  @Setter
  @Builder
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  @AllArgsConstructor(access = AccessLevel.PROTECTED)
  public static class UpdateStoreReviewRequest implements BaseRequest {

    private UUID reviewId;

    private String content;

    private ZeroDrink[] zeroDrinks;

    private AccessToken accessToken;

    @Override
    public boolean isValid() {
      return reviewId != null && content != null && zeroDrinks != null && zeroDrinks.length > 0 && accessToken != null;
    }
  }

}

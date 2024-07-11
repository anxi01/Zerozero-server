package com.zerozero.review.application;

import com.zerozero.core.application.BaseRequest;
import com.zerozero.core.application.BaseResponse;
import com.zerozero.core.application.BaseUseCase;
import com.zerozero.core.domain.entity.Review;
import com.zerozero.core.domain.entity.Store;
import com.zerozero.core.domain.entity.User;
import com.zerozero.core.domain.infra.repository.ReviewJPARepository;
import com.zerozero.core.domain.infra.repository.StoreJPARepository;
import com.zerozero.core.domain.infra.repository.UserJPARepository;
import com.zerozero.core.domain.vo.AccessToken;
import com.zerozero.core.domain.vo.ZeroDrink;
import com.zerozero.core.exception.DomainException;
import com.zerozero.core.exception.error.BaseErrorCode;
import com.zerozero.core.util.JwtUtil;
import com.zerozero.review.application.CreateStoreReviewUseCase.CreateStoreReviewRequest;
import com.zerozero.review.application.CreateStoreReviewUseCase.CreateStoreReviewResponse;
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
public class CreateStoreReviewUseCase implements BaseUseCase<CreateStoreReviewRequest, CreateStoreReviewResponse> {

  private final JwtUtil jwtUtil;

  private final UserJPARepository userJPARepository;

  private final StoreJPARepository storeJPARepository;

  private final ReviewJPARepository reviewJPARepository;

  @Override
  public CreateStoreReviewResponse execute(CreateStoreReviewRequest request) {
    if (request == null || !request.isValid()) {
      log.error("[CreateStoreReviewUseCase] Invalid request");
      return CreateStoreReviewResponse.builder()
          .success(false)
          .errorCode(CreateStoreReviewErrorCode.NOT_EXIST_REVIEW_CONDITION)
          .build();
    }
    AccessToken accessToken = request.getAccessToken();
    if (jwtUtil.isTokenExpired(accessToken.getToken())) {
      log.error("[CreateStoreReviewUseCase] Expired access token");
      return CreateStoreReviewResponse.builder()
          .success(false)
          .errorCode(CreateStoreReviewErrorCode.EXPIRED_TOKEN)
          .build();
    }
    String userEmail = jwtUtil.extractUsername(accessToken.getToken());
    User user = userJPARepository.findByEmail(userEmail);
    if (user == null) {
      log.error("[CreateStoreReviewUseCase] not found user with email {}", userEmail);
      return CreateStoreReviewResponse.builder()
          .success(false)
          .errorCode(CreateStoreReviewErrorCode.NOT_EXIST_USER)
          .build();
    }
    Store store = storeJPARepository.findById(request.getStoreId()).orElse(null);
    if (store == null) {
      log.error("[CreateStoreReviewUseCase] not found store with id {}", request.getStoreId());
      return CreateStoreReviewResponse.builder()
          .success(false)
          .errorCode(CreateStoreReviewErrorCode.NOT_EXIST_STORE)
          .build();
    }
    if (isUserAlreadyReviewed(user, store)) {
      log.error("[CreateStoreReviewUseCase] User already reviewed");
      return CreateStoreReviewResponse.builder()
          .success(false)
          .errorCode(CreateStoreReviewErrorCode.ALREADY_USER_REVIEWED)
          .build();
    }
    Review review = Review.of(request.getContent(), request.getZeroDrinks(), user, store);
    reviewJPARepository.save(review);
    return CreateStoreReviewResponse.builder().build();
  }

  private boolean isUserAlreadyReviewed(User user, Store store) {
    return reviewJPARepository.existsByUserIdAndStoreId(user.getId(), store.getId());
  }

  @Getter
  @RequiredArgsConstructor
  public enum CreateStoreReviewErrorCode implements BaseErrorCode<DomainException> {
    NOT_EXIST_REVIEW_CONDITION(HttpStatus.BAD_REQUEST, "리뷰 요청 조건이 올바르지 않습니다."),
    EXPIRED_TOKEN(HttpStatus.BAD_REQUEST, "만료된 토큰입니다."),
    NOT_EXIST_USER(HttpStatus.BAD_REQUEST, "존재하지 않는 사용자입니다."),
    NOT_EXIST_STORE(HttpStatus.BAD_REQUEST, "등록된 판매점이 존재하지 않습니다."),
    ALREADY_USER_REVIEWED(HttpStatus.INTERNAL_SERVER_ERROR, "이미 리뷰를 작성한 사용자입니다.");

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
  public static class CreateStoreReviewResponse extends BaseResponse<CreateStoreReviewErrorCode> {
  }

  @ToString
  @Getter
  @Setter
  @Builder
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  @AllArgsConstructor(access = AccessLevel.PROTECTED)
  public static class CreateStoreReviewRequest implements BaseRequest {

    private UUID storeId;

    private String content;

    private ZeroDrink[] zeroDrinks;

    private AccessToken accessToken;

    @Override
    public boolean isValid() {
      return storeId != null && content != null && zeroDrinks != null && zeroDrinks.length > 0 && accessToken != null;
    }
  }

}

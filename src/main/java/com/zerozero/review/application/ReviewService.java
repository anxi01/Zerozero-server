package com.zerozero.review.application;

import com.zerozero.core.domain.entity.Review;
import com.zerozero.core.domain.entity.ReviewLike;
import com.zerozero.core.domain.entity.Store;
import com.zerozero.review.ReviewRequest;
import com.zerozero.store.AccessDeniedException;
import com.zerozero.review.AlreadyReviewedException;
import com.zerozero.review.ReviewNotFoundException;
import com.zerozero.store.StoreNotFoundException;
import com.zerozero.core.domain.infra.repository.ReviewLikeJPARepository;
import com.zerozero.core.domain.infra.repository.ReviewJPARepository;
import com.zerozero.core.domain.infra.repository.StoreJPARepository;
import com.zerozero.core.domain.entity.User;
import java.security.Principal;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewService {

  private final ReviewJPARepository reviewJPARepository;
  private final StoreJPARepository storeJPARepository;
  private final ReviewLikeJPARepository reviewLikeJPARepository;

  public void uploadReview(Principal connectedUser, Long storeId, ReviewRequest request) {

    User user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
    Store store = storeJPARepository.findById(storeId).orElseThrow(StoreNotFoundException::new);

    if (hasUserAlreadyReviewed(user, store)) {
      throw new AlreadyReviewedException();
    }

    Review review = Review.of(request, user, store);
    reviewJPARepository.save(review);
  }

  public void editReview(Principal connectedUser, Long reviewId, ReviewRequest request) {

    User user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
    Review review = reviewJPARepository.findById(reviewId).orElseThrow(ReviewNotFoundException::new);

    if (isUserAuthorized(user, review)) {
      review.editReview(request);
      reviewJPARepository.save(review);
    } else {
      throw new AccessDeniedException();
    }
  }

  public void deleteReview(Principal connectedUser, Long reviewId) {

    User user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
    Review review = reviewJPARepository.findById(reviewId).orElseThrow(ReviewNotFoundException::new);

    if (isUserAuthorized(user, review)) {
      reviewJPARepository.delete(review);
    } else {
      throw new AccessDeniedException();
    }
  }

  public void likeReview(Principal connectedUser, Long reviewId) {

    User user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
    Review review = reviewJPARepository.findById(reviewId).orElseThrow(ReviewNotFoundException::new);

    Optional<ReviewLike> like = reviewLikeJPARepository.findByUserAndReview(user, review);

    if (like.isEmpty()) {
      reviewLikeJPARepository.save(new ReviewLike(review, user));
    } else {
      reviewLikeJPARepository.delete(like.get());
    }
  }

  private boolean hasUserAlreadyReviewed(User user, Store store) {
    return reviewJPARepository.existsByUserAndStore(user, store);
  }

  private boolean isUserAuthorized(User user, Review review) {
    return user.getId().equals(review.getUser().getId());
  }
}

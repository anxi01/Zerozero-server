package com.zerozero.domain.store.application;

import com.zerozero.domain.store.domain.Review;
import com.zerozero.domain.store.domain.Store;
import com.zerozero.domain.store.dto.request.ReviewRequest;
import com.zerozero.domain.store.exception.AccessDeniedException;
import com.zerozero.domain.store.exception.ReviewNotFoundException;
import com.zerozero.domain.store.exception.StoreNotFoundException;
import com.zerozero.domain.store.repository.ReviewRepository;
import com.zerozero.domain.store.repository.StoreRepository;
import com.zerozero.domain.user.domain.User;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewService {

  private final ReviewRepository reviewRepository;
  private final StoreRepository storeRepository;

  public void uploadReview(Principal connectedUser, Long storeId, ReviewRequest request) {

    User user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
    Store store = storeRepository.findById(storeId).orElseThrow(StoreNotFoundException::new);

    Review review = Review.of(request, user, store);

    reviewRepository.save(review);
  }


  public void editReview(Principal connectedUser, Long reviewId, ReviewRequest request) {

    User user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
    Review review = reviewRepository.findById(reviewId).orElseThrow(ReviewNotFoundException::new);

    if (isUserAuthorized(user, review)) {
      review.editReview(request);
      reviewRepository.save(review);
    } else {
      throw new AccessDeniedException();
    }
  }

  public void deleteReview(Principal connectedUser, Long reviewId) {

    User user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
    Review review = reviewRepository.findById(reviewId).orElseThrow(ReviewNotFoundException::new);

    if (isUserAuthorized(user, review)) {
      reviewRepository.delete(review);
    } else {
      throw new AccessDeniedException();
    }
  }

  private boolean isUserAuthorized(User user, Review review) {
    return user.getId().equals(review.getUser().getId());
  }
}

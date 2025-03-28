package com.zerozero.review.application;

import com.zerozero.review.domain.model.Review;
import com.zerozero.review.domain.repository.ReviewRepository;
import com.zerozero.review.exception.ReviewErrorType;
import com.zerozero.review.exception.ReviewException;
import com.zerozero.review.presentation.request.ReviewRequest;
import com.zerozero.store.domain.model.Store;
import com.zerozero.store.domain.repository.StoreRepository;
import com.zerozero.store.exception.StoreErrorType;
import com.zerozero.store.exception.StoreException;
import com.zerozero.user.domain.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
@Log4j2
public class ReviewService {

    private final StoreRepository storeRepository;

    private final ReviewRepository reviewRepository;

    public void createStoreReview(UUID storeId, ReviewRequest reviewRequest, User user) {
        Store store = storeRepository.findById(storeId).orElseThrow(() -> new StoreException(StoreErrorType.NOT_EXIST_STORE));
        if (isUserAlreadyReviewed(user.getId(), store.getId())) {
            log.error("[ReviewService] User already reviewed");
            throw new ReviewException(ReviewErrorType.ALREADY_USER_REVIEWED);
        }
        Review review = Review.create(reviewRequest.content(), reviewRequest.zeroDrinks(), user.getId(), store.getId());
        reviewRepository.save(review);
    }

    private boolean isUserAlreadyReviewed(UUID userId, UUID storeId) {
        return reviewRepository.existsByUserIdAndStoreId(userId, storeId);
    }

}

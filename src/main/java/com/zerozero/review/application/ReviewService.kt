package com.zerozero.review.application

import com.zerozero.review.domain.model.Review
import com.zerozero.review.domain.repository.ReviewRepository
import com.zerozero.review.exception.ReviewErrorType
import com.zerozero.review.exception.ReviewException
import com.zerozero.review.presentation.request.ReviewRequest
import com.zerozero.store.domain.repository.StoreRepository
import com.zerozero.store.exception.StoreErrorType
import com.zerozero.store.exception.StoreException
import com.zerozero.user.domain.model.User
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional
class ReviewService(
    private val storeRepository: StoreRepository,
    private val reviewRepository: ReviewRepository,
) {

    private val log: Logger = LoggerFactory.getLogger(javaClass)

    fun createStoreReview(storeId: UUID, reviewRequest: ReviewRequest, user: User) {
        val store = storeRepository.findById(storeId).orElseThrow { StoreException(StoreErrorType.NOT_EXIST_STORE) }
        if (isUserAlreadyReviewed(user.id, store.id)) {
            log.error("[ReviewService] User already reviewed")
            throw ReviewException(ReviewErrorType.ALREADY_USER_REVIEWED)
        }

        val review = Review.create(reviewRequest.content, reviewRequest.zeroDrinks, user.id, store.id)
        reviewRepository.save(review)
    }

    private fun isUserAlreadyReviewed(userId: UUID, storeId: UUID): Boolean {
        return reviewRepository.existsByUserIdAndStoreId(userId, storeId)
    }
}

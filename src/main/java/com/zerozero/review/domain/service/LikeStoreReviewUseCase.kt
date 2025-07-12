package com.zerozero.review.domain.service

import com.zerozero.review.domain.model.ReviewLike
import com.zerozero.review.domain.repository.ReviewLikeRepository
import com.zerozero.review.domain.repository.ReviewRepository
import com.zerozero.review.exception.ReviewErrorType
import com.zerozero.review.exception.ReviewException
import com.zerozero.user.domain.model.User
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional
class LikeStoreReviewUseCase(
    private val reviewRepository: ReviewRepository,
    private val reviewLikeRepository: ReviewLikeRepository,
) {

    private val log: Logger = LoggerFactory.getLogger(javaClass)

    fun execute(reviewId: UUID, user: User) {
        val review = reviewRepository.findById(reviewId)
            .orElseThrow { ReviewException(ReviewErrorType.NOT_EXIST_DELETABLE_REVIEW) }

        val existingLike = reviewLikeRepository.findByReviewIdAndUserId(review.id, user.id)

        if (existingLike != null) {
            log.info("[Unlike] User {} unliked Review {}", user.id, review.id)
            reviewLikeRepository.delete(existingLike)
        } else {
            log.info("[Like] User {} liked Review {}", user.id, review.id)
            reviewLikeRepository.save(ReviewLike.create(review.id, user.id))
        }
    }
}

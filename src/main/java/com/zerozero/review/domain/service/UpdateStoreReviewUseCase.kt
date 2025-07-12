package com.zerozero.review.domain.service

import com.zerozero.review.domain.repository.ReviewRepository
import com.zerozero.review.exception.ReviewErrorType
import com.zerozero.review.exception.ReviewException
import com.zerozero.review.presentation.request.ReviewRequest
import com.zerozero.user.domain.model.User
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional
class UpdateStoreReviewUseCase(
    private val reviewRepository: ReviewRepository,
) {

    private val log: Logger = LoggerFactory.getLogger(javaClass)

    fun execute(reviewId: UUID, reviewRequest: ReviewRequest, user: User) {
        val review =
            reviewRepository.findById(reviewId).orElseThrow { ReviewException(ReviewErrorType.NOT_EXIST_REVIEW) }

        if (!review.isWrittenBy(user.id)) {
            log.error("[UpdateStoreReviewUseCase] User does not have review with id {}", reviewId)
            throw ReviewException(ReviewErrorType.USER_VALIDATION_FAILED)
        }

        review.updateContent(reviewRequest.content)
        review.updateZeroDrinks(reviewRequest.zeroDrinks)
    }
}

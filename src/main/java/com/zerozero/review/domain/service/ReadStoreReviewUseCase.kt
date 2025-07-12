package com.zerozero.review.domain.service

import com.zerozero.review.domain.model.Review
import com.zerozero.review.domain.response.ReviewResponse
import com.zerozero.review.infrastructure.querydsl.ReviewQueryRepository
import com.zerozero.store.presentation.request.ReadStoreRequest
import com.zerozero.user.domain.model.User
import com.zerozero.user.domain.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional(readOnly = true)
class ReadStoreReviewUseCase(
    private val reviewQueryRepository: ReviewQueryRepository,
    private val userRepository: UserRepository,
) {

    fun execute(readStoreRequest: ReadStoreRequest, user: User): List<ReviewResponse> {
        val reviews = reviewQueryRepository.findByStoreIdAndFilter(readStoreRequest.storeId, readStoreRequest.filter)
        val reviewAuthorMap = getReviewAuthors(reviews)
        return convertReviewResponse(user, reviews, reviewAuthorMap)
    }

    private fun convertReviewResponse(
        user: User,
        reviews: List<Review>,
        reviewAuthorMap: Map<UUID, User>,
    ): List<ReviewResponse> {
        if (reviews.isEmpty() || reviewAuthorMap.isEmpty()) return emptyList()

        return reviews.map { review ->
            val reviewAuthor = reviewAuthorMap[review.userId] ?: return@map null
            val isLiked = review.reviewLikes.any { it.userId == user.id }
            ReviewResponse.of(review, reviewAuthor, review.reviewLikes.size, isLiked)
        }.filterNotNull()
    }

    private fun getReviewAuthors(reviews: List<Review>): Map<UUID, User> {
        val userIds = reviews.map { it.userId }.distinct()
        val users = userRepository.findAllByIdIn(userIds)
        return users.associateBy { it.id }
    }
}

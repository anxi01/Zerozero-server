package com.zerozero.review.domain.repository

import com.zerozero.review.domain.model.ReviewLike
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface ReviewLikeRepository : JpaRepository<ReviewLike, Long> {

    fun findByReviewIdAndUserId(reviewId: UUID, userId: UUID): ReviewLike?
}

package com.zerozero.review.domain.model

import com.zerozero.core.domain.BaseAutoIncrementEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint
import java.util.*

@Entity
@Table(
    name = "review_like",
    uniqueConstraints = [UniqueConstraint(columnNames = ["review_id", "user_id"])]
)
class ReviewLike(
    @Column(name = "review_id", nullable = true)
    val reviewId: UUID,

    @Column(name = "user_id", nullable = false)
    val userId: UUID,
) : BaseAutoIncrementEntity() {

    companion object {
        fun create(reviewId: UUID, userId: UUID): ReviewLike {
            return ReviewLike(
                reviewId = reviewId,
                userId = userId
            )
        }
    }
}

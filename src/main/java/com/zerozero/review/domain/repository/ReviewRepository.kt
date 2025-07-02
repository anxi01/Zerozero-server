package com.zerozero.review.domain.repository

import com.zerozero.review.domain.model.Review
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface ReviewRepository : JpaRepository<Review, UUID> {

    fun existsByUserIdAndStoreId(userId: UUID, storeId: UUID): Boolean
}

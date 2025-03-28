package com.zerozero.review.domain.repository;

import com.zerozero.review.domain.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ReviewRepository extends JpaRepository<Review, UUID> {

    Boolean existsByUserIdAndStoreId(UUID userId, UUID storeId);

}

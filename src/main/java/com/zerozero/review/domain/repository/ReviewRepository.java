package com.zerozero.review.domain.repository;

import com.zerozero.review.domain.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ReviewRepository extends JpaRepository<Review, UUID> {

    Boolean existsByUserIdAndStoreIdAndDeleted(UUID userId, UUID storeId, boolean deleted);

    Optional<Review> findByIdAndDeleted(UUID id, Boolean deleted);

}

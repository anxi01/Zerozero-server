package com.zerozero.review.domain.model;

import com.zerozero.core.domain.BaseAutoIncrementEntity;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@SuperBuilder
public class ReviewLike extends BaseAutoIncrementEntity {

    private UUID reviewId;

    private UUID userId;

    public static ReviewLike create(UUID reviewId, UUID userId) {
        return ReviewLike.builder()
                .reviewId(reviewId)
                .userId(userId)
                .build();
    }

}

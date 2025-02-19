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

    public ReviewLike(UUID reviewId, UUID userId) {
        this.reviewId = reviewId;
        this.userId = userId;
    }
}

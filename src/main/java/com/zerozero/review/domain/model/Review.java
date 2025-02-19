package com.zerozero.review.domain.model;

import com.zerozero.core.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@SuperBuilder
public class Review extends BaseEntity {

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private Set<ZeroDrink> zeroDrinks = new HashSet<>();

    private String content;

    private UUID userId;

    private UUID storeId;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "reviewId")
    private List<ReviewLike> reviewLikes = new ArrayList<>();

    public static Review of(String content, Set<ZeroDrink> zeroDrinks, UUID userId, UUID storeId) {
        return Review.builder()
                .content(content)
                .zeroDrinks(zeroDrinks)
                .userId(userId)
                .storeId(storeId)
                .build();
    }

    public boolean isWrittenBy(UUID userId) {
        return this.userId.equals(userId);
    }

    public void updateContent(String content) {
        this.content = content;
    }

    public void updateZeroDrinks(Set<ZeroDrink> zeroDrinks) {
        this.zeroDrinks.clear();
        this.zeroDrinks.addAll(zeroDrinks);
    }

    public void deleted(boolean deleted) {
        setDeleted(deleted);
    }
}

package com.zerozero.review.domain.service;

import com.zerozero.review.domain.model.Review;
import com.zerozero.review.domain.response.ReviewResponse;
import com.zerozero.review.infrastructure.querydsl.ReviewQueryRepository;
import com.zerozero.store.presentation.request.ReadStoreRequest;
import com.zerozero.user.domain.model.User;
import com.zerozero.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Log4j2
public class ReadStoreReviewUseCase {

    private final ReviewQueryRepository reviewQueryRepository;

    private final UserRepository userRepository;

    public List<ReviewResponse> execute(ReadStoreRequest readStoreRequest, User user) {
        List<Review> reviews = reviewQueryRepository.findByStoreIdAndFilter(readStoreRequest.storeId(), readStoreRequest.filter());
        Map<UUID, User> reviewAuthorMap = getReviewAuthors(reviews);
        return convertReviewResponse(user, reviews, reviewAuthorMap);
    }

    private List<ReviewResponse> convertReviewResponse(User user, List<Review> reviews, Map<UUID, User> reviewAuthorMap) {
        if (user == null || reviews.isEmpty() || reviewAuthorMap.isEmpty()) {
            return Collections.emptyList();
        }
        return reviews.stream()
                .map(review -> {
                    User reviewAuthor = reviewAuthorMap.get(review.getUserId());
                    return ReviewResponse.of(review, reviewAuthor, review.getReviewLikes().size(), review.getReviewLikes().stream()
                            .anyMatch(like -> user.getId().equals(like.getUserId())));
                })
                .collect(Collectors.toList());
    }

    private Map<UUID, User> getReviewAuthors(List<Review> reviews) {
        return userRepository.findAllByIdInAndDeleted(
                reviews.stream()
                        .map(Review::getUserId)
                        .distinct()
                        .collect(Collectors.toList()),
                false
        ).stream().collect(Collectors.toMap(User::getId, reviewAuthor -> reviewAuthor));
    }

}

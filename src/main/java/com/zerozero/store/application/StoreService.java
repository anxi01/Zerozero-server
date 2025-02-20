package com.zerozero.store.application;

import com.zerozero.review.domain.model.ZeroDrink;
import com.zerozero.review.domain.response.ReviewResponse;
import com.zerozero.review.domain.service.ReadStoreReviewUseCase;
import com.zerozero.store.domain.response.StoreResponse;
import com.zerozero.store.domain.service.ReadStoreInfoUseCase;
import com.zerozero.store.presentation.request.ReadStoreRequest;
import com.zerozero.store.presentation.response.ReadStoreResponse;
import com.zerozero.user.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StoreService {

    private final ReadStoreInfoUseCase readStoreInfoUseCase;

    private final ReadStoreReviewUseCase readStoreReviewUseCase;

    public ReadStoreResponse readStore(ReadStoreRequest readStoreRequest, User user) {
        StoreResponse store = readStoreInfoUseCase.execute(readStoreRequest.storeId());
        List<ReviewResponse> reviews = readStoreReviewUseCase.execute(readStoreRequest, user);
        return ReadStoreResponse.of(store, reviews, getTop3ZeroDrinks(reviews));
    }

    private List<ZeroDrink> getTop3ZeroDrinks(List<ReviewResponse> reviews) {
        if (reviews == null) {
            return Collections.emptyList();
        }
        List<ZeroDrink> allZeroDrinks = reviews.stream()
                .flatMap(review -> review.zeroDrinks().stream())
                .collect(Collectors.toList());

        return allZeroDrinks.stream()
                .collect(Collectors.groupingBy(type -> type, Collectors.counting()))
                .entrySet().stream()
                .sorted(Map.Entry.<ZeroDrink, Long>comparingByValue().reversed()
                        .thenComparing(Map.Entry.comparingByKey()))
                .limit(3)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }
}

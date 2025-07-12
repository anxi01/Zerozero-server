package com.zerozero.store.application

import com.zerozero.review.domain.model.ZeroDrink
import com.zerozero.review.domain.response.ReviewResponse
import com.zerozero.review.domain.service.ReadStoreReviewUseCase
import com.zerozero.store.domain.service.ReadStoreInfoUseCase
import com.zerozero.store.presentation.request.ReadStoreRequest
import com.zerozero.store.presentation.response.ReadStoreResponse
import com.zerozero.user.domain.model.User
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class StoreService(
    private val readStoreInfoUseCase: ReadStoreInfoUseCase,
    private val readStoreReviewUseCase: ReadStoreReviewUseCase
) {

    fun readStore(readStoreRequest: ReadStoreRequest, user: User): ReadStoreResponse {
        val store = readStoreInfoUseCase.execute(readStoreRequest.storeId)
        val reviews = readStoreReviewUseCase.execute(readStoreRequest, user)
        return ReadStoreResponse.of(store, reviews, getTop3ZeroDrinks(reviews))
    }

    private fun getTop3ZeroDrinks(reviews: List<ReviewResponse>?): List<ZeroDrink> {
        if (reviews == null) return emptyList()

        val allZeroDrinks = reviews.flatMap { it.zeroDrinks }

        return allZeroDrinks
            .groupingBy { it }
            .eachCount()
            .entries
            .sortedWith(compareByDescending<Map.Entry<ZeroDrink, Int>> { it.value }
                .thenBy { it.key })
            .take(3)
            .map { it.key }
    }
}

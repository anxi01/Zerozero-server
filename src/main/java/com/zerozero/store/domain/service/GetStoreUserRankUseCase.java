package com.zerozero.store.domain.service;

import com.zerozero.store.domain.repository.StoreRepository;
import com.zerozero.store.domain.response.StoreUserRankResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetStoreUserRankUseCase {

    private final StoreRepository storeRepository;

    public StoreUserRankResponse execute(UUID userId) {
        int storeUserRank = storeRepository.findStoreUserRank(userId).orElse(0);
        int storeCount = storeRepository.countStoresByUserId(userId);
        return StoreUserRankResponse.of(storeUserRank, storeCount);
    }
}

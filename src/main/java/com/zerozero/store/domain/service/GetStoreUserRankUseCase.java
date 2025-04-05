package com.zerozero.store.domain.service;

import com.zerozero.store.domain.repository.StoreRepository;
import com.zerozero.store.domain.response.StoreUserRankProjection;
import com.zerozero.store.exception.StoreErrorType;
import com.zerozero.store.exception.StoreException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetStoreUserRankUseCase {

    private final StoreRepository storeRepository;

    public StoreUserRankProjection execute(UUID userId) {
        return storeRepository.findStoreUserRank(userId).orElseThrow(() -> new StoreException(StoreErrorType.USER_RANK_NOT_AVAILABLE));
    }
}

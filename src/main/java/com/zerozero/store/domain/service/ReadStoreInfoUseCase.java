package com.zerozero.store.domain.service;

import com.zerozero.store.domain.model.Store;
import com.zerozero.store.domain.repository.StoreRepository;
import com.zerozero.store.domain.response.StoreResponse;
import com.zerozero.store.exception.StoreErrorType;
import com.zerozero.store.exception.StoreException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReadStoreInfoUseCase {

    private final StoreRepository storeRepository;

    public StoreResponse execute(UUID storeId) {
        Store store = storeRepository.findById(storeId).orElseThrow(() -> new StoreException(StoreErrorType.NOT_EXIST_STORE));
        return StoreResponse.from(store);
    }

}

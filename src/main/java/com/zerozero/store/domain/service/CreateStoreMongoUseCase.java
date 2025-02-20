package com.zerozero.store.domain.service;

import com.zerozero.store.domain.model.Store;
import com.zerozero.store.domain.repository.StoreRepository;
import com.zerozero.store.exception.StoreErrorType;
import com.zerozero.store.exception.StoreException;
import com.zerozero.store.infrastructure.mongodb.StoreMongoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class CreateStoreMongoUseCase {

    private final StoreRepository storeRepository;

    private final StoreMongoRepository storeMongoRepository;

    public void execute(UUID storeId) {
        Store store = storeRepository.findById(storeId).orElseThrow(() -> new StoreException(StoreErrorType.NOT_EXIST_STORE));
        com.zerozero.store.infrastructure.mongodb.Store storeMongo = com.zerozero.store.infrastructure.mongodb.Store.of(store);
        storeMongoRepository.save(storeMongo);
    }

}

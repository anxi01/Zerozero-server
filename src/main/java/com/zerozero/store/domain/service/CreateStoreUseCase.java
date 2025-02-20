package com.zerozero.store.domain.service;

import com.zerozero.store.domain.model.Store;
import com.zerozero.store.domain.repository.StoreRepository;
import com.zerozero.store.domain.response.StoreSearchResponse;
import com.zerozero.store.exception.StoreErrorType;
import com.zerozero.store.exception.StoreException;
import com.zerozero.store.infrastructure.rabbitmq.CreateStoreMessageProducer;
import com.zerozero.store.infrastructure.rabbitmq.CreateStoreQueueProperty;
import com.zerozero.store.presentation.request.CreateStoreRequest;
import com.zerozero.user.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class CreateStoreUseCase {

    private final StoreRepository storeRepository;

    private final StoreSearcher storeSearcher;

    private final CreateStoreQueueProperty createStoreQueueProperty;

    public UUID execute(CreateStoreRequest createStoreRequest, User user) {
        List<StoreSearchResponse> storeSearchResponses = storeSearcher.search(createStoreRequest.placeName());

        StoreSearchResponse storeSearchResponse = storeSearchResponses.stream()
                .filter(store ->
                        createStoreRequest.placeName().equals(store.placeName()) &&
                                createStoreRequest.longitude().equals(store.longitude()) &&
                                createStoreRequest.latitude().equals(store.latitude()))
                .findFirst()
                .orElseThrow(() -> new StoreException(StoreErrorType.NOT_EXIST_STORE));

        Store store = Store.create(user.getId(), storeSearchResponse, createStoreRequest.images());
        storeRepository.save(store);

        UUID storeId = store.getId();
        CreateStoreMessageProducer createStoreMessageProducer = new CreateStoreMessageProducer(createStoreQueueProperty, storeId);
        createStoreMessageProducer.publishMessage();

        return storeId;
    }

}

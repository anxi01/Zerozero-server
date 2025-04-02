package com.zerozero.store.domain.service;

import com.zerozero.store.domain.event.StoreCreatedEvent;
import com.zerozero.store.domain.model.Store;
import com.zerozero.store.domain.repository.StoreRepository;
import com.zerozero.store.presentation.request.CreateStoreRequest;
import com.zerozero.user.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class CreateStoreUseCase {

    private final StoreRepository storeRepository;

    private final ApplicationEventPublisher eventPublisher;

    public UUID execute(CreateStoreRequest createStoreRequest, User user) {
        Store store = Store.create(user.getId(), createStoreRequest);
        storeRepository.save(store);

        UUID storeId = store.getId();
        eventPublisher.publishEvent(new StoreCreatedEvent(storeId));
        return storeId;
    }

}

package com.zerozero.store.domain.service;

import com.zerozero.store.domain.repository.StoreRepository;
import com.zerozero.store.domain.response.StoreResponse;
import com.zerozero.user.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReadUserStoresUseCase {

    private final StoreRepository storeRepository;

    public List<StoreResponse> execute(User user) {
        return storeRepository.findAllByUserIdWithImages(user.getId())
                .stream()
                .map(StoreResponse::from)
                .collect(Collectors.toList());
    }

}

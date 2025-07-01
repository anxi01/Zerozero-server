package com.zerozero.store.domain.service;

import com.zerozero.store.domain.model.Store;
import com.zerozero.store.domain.repository.StoreRepository;
import com.zerozero.store.domain.response.StoreResponse;
import com.zerozero.store.domain.response.StoreSearchResponse;
import com.zerozero.store.domain.value.GeoLocation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SearchStoreUseCase {

    private final StoreRepository storeRepository;

    private final StoreSearcher storeSearcher;

    public List<StoreResponse> execute(String query) {
        List<StoreSearchResponse> storeSearchResponses = storeSearcher.search(query);

        return storeSearchResponses.stream()
                .map(storeSearchResponse -> {
                    Store store = storeRepository.findByNameAndGeoLocation(
                            storeSearchResponse.placeName(), new GeoLocation(storeSearchResponse.longitude(), storeSearchResponse.latitude())
                    );

                    if (store == null) {
                        return StoreResponse.of(storeSearchResponse, null, false);
                    }

                    return StoreResponse.of(
                            storeSearchResponse,
                            store.getId(),
                            store.getStatus()
                    );
                })
                .collect(Collectors.toList());
    }

}

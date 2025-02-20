package com.zerozero.store.domain.service;

import com.zerozero.store.domain.model.Store;
import com.zerozero.store.domain.repository.StoreRepository;
import com.zerozero.store.domain.response.StoreResponse;
import com.zerozero.store.domain.response.StoreSearchResponse;
import com.zerozero.store.domain.value.GeoLocation;
import com.zerozero.store.presentation.request.StoreSearchRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SearchNearbyStoresUseCase {

    private final StoreRepository storeRepository;

    private final StoreSearcher storeSearcher;

    public List<StoreResponse> execute(StoreSearchRequest storeSearchRequest) {
        List<StoreSearchResponse> storeSearchResponses = storeSearcher.searchByLocation(
                storeSearchRequest.query(), storeSearchRequest.longitude(), storeSearchRequest.latitude()
        );

        return storeSearchResponses.stream()
                .map(storeSearchResponse -> {
                    Store store = storeRepository.findByNameAndGeoLocation(
                            storeSearchResponse.placeName(), GeoLocation.of(storeSearchResponse.longitude(), storeSearchResponse.latitude())
                    );

                    if (store == null) {
                        return StoreResponse.of(storeSearchResponse, null, false);
                    }

                    return StoreResponse.of(
                            storeSearchResponse,
                            store.getId(),
                            store.isStatus()
                    );
                })
                .collect(Collectors.toList());
    }

}

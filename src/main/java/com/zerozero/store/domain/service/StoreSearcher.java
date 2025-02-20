package com.zerozero.store.domain.service;

import com.zerozero.store.domain.response.StoreSearchResponse;

import java.util.List;

public interface StoreSearcher {

    List<StoreSearchResponse> search(String query);

    List<StoreSearchResponse> searchByLocation(String query, double longitude, double latitude);
}

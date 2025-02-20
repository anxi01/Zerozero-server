package com.zerozero.store.domain.request;

public record StoreLocationRequest(
        double longitude,
        double latitude,
        String accessToken
) {
}

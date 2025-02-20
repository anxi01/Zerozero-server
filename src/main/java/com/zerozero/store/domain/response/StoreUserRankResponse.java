package com.zerozero.store.domain.response;

public record StoreUserRankResponse(
        int rank,
        int storeReportCount
) {
    public static StoreUserRankResponse of(int rank, int storeReportCount) {
        return new StoreUserRankResponse(rank, storeReportCount);
    }
}

package com.zerozero.store.infrastructure.mongodb;

import com.zerozero.core.util.GeoJsonConverter;
import lombok.*;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@ToString
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "store")
public class Store {

    private UUID storeId;

    private String kakaoId;

    private String name;

    private String category;

    private String phone;

    private String address;

    private String roadAddress;

    private String longitude;

    private String latitude;

    private GeoJsonPoint location;

    private String placeUrl;

    private boolean status;

    private UUID userId;

    public static Store of(com.zerozero.store.domain.model.Store store) {
        return Store.builder()
                .storeId(store.getId())
                .kakaoId(store.getKakaoId())
                .name(store.getName())
                .category(store.getCategory())
                .phone(store.getPhone())
                .address(store.getAddress().getAddress())
                .roadAddress(store.getAddress().getRoadAddress())
                .longitude(store.getGeoLocation().getLongitude())
                .latitude(store.getGeoLocation().getLatitude())
                .location(GeoJsonConverter.of(store.getGeoLocation().getLongitude(), store.getGeoLocation().getLatitude()))
                .placeUrl(store.getPlaceUrl())
                .status(true)
                .userId(store.getUserId())
                .build();
    }

}

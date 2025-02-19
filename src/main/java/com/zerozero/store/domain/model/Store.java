package com.zerozero.store.domain.model;

import com.zerozero.core.domain.BaseEntity;
import com.zerozero.image.domain.model.Image;
import com.zerozero.store.domain.response.StoreSearchResponse;
import com.zerozero.store.domain.value.Address;
import com.zerozero.store.domain.value.GeoLocation;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@SuperBuilder
public class Store extends BaseEntity {

    private String kakaoId;

    private String name;

    private String category;

    private String phone;

    @Embedded
    private Address address;

    @Embedded
    private GeoLocation geoLocation;

    @Builder.Default
    private boolean status = false;

    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
    private List<Image> images = new ArrayList<>();

    private String placeUrl;

    private UUID userId;

    public static Store of(UUID userId, StoreSearchResponse store, List<String> images) {
        return Store.builder()
                .kakaoId(store.id())
                .name(store.placeName())
                .category(store.categoryName())
                .phone(store.phone())
                .address(Address.of(store.addressName(), store.roadAddressName()))
                .geoLocation(GeoLocation.of(store.longitude(), store.latitude()))
                .status(true)
                .images(images.stream().map(Image::from).collect(Collectors.toList()))
                .placeUrl(store.placeUrl())
                .userId(userId)
                .build();
    }

}

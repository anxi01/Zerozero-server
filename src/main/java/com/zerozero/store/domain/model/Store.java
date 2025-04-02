package com.zerozero.store.domain.model;

import com.zerozero.core.domain.BaseEntity;
import com.zerozero.image.domain.model.Image;
import com.zerozero.store.domain.value.Address;
import com.zerozero.store.domain.value.GeoLocation;
import com.zerozero.store.presentation.request.CreateStoreRequest;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@SuperBuilder
@SQLDelete(sql = "UPDATE store SET deleted = true WHERE id = ?")
@SQLRestriction("deleted = false")
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

    @ElementCollection
    @CollectionTable(name = "store_images",
            joinColumns = @JoinColumn(name = "storeId"))
    private List<Image> images = new ArrayList<>();

    private String placeUrl;

    private UUID userId;

    public static Store create(UUID userId, CreateStoreRequest createStoreRequest) {
        return Store.builder()
                .kakaoId(createStoreRequest.kakaoId())
                .name(createStoreRequest.placeName())
                .category(createStoreRequest.category())
                .phone(createStoreRequest.phone())
                .address(Address.of(createStoreRequest.address(), createStoreRequest.roadAddress()))
                .geoLocation(GeoLocation.of(createStoreRequest.longitude(), createStoreRequest.latitude()))
                .status(true)
                .images(createStoreRequest.images().stream().map(Image::from).collect(Collectors.toList()))
                .placeUrl(createStoreRequest.placeUrl())
                .userId(userId)
                .build();
    }

}

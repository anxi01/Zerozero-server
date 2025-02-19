package com.zerozero.store.domain.value;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class GeoLocation {
    private String longitude;
    private String latitude;

    public static GeoLocation of(String longitude, String latitude) {
        return new GeoLocation(longitude, latitude);
    }
}


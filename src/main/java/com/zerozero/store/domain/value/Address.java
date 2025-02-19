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
public class Address {
    private String address;
    private String roadAddress;

    public static Address of(String address, String roadAddress) {
        return new Address(address, roadAddress);
    }
}

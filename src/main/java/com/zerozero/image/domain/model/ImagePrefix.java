package com.zerozero.image.domain.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum ImagePrefix {
    STORE("store"),
    USER("user"),
    ;

    private final String prefix;

    public static ImagePrefix fromString(String value) {
        return Arrays.stream(ImagePrefix.values())
                .filter(imagePrefix -> imagePrefix.getPrefix().equalsIgnoreCase(value))
                .findFirst()
                .orElse(null);
    }
}

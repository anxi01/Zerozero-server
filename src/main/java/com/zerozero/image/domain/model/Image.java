package com.zerozero.image.domain.model;

import com.zerozero.core.domain.BaseAutoIncrementEntity;
import com.zerozero.image.exception.ImageErrorType;
import com.zerozero.image.exception.ImageException;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Image extends BaseAutoIncrementEntity {

    private static final Set<String> ALLOWED_EXTENSIONS = Set.of("PNG", "JPG", "JPEG", "HEIC");

    private String url;

    public static Image from(String image) {
        return new Image(image);
    }

    public static void validateExtension(String extension) {
        if (!ALLOWED_EXTENSIONS.contains(extension.toUpperCase())) {
            throw new ImageException(ImageErrorType.INVALID_IMAGE_EXTENSION);
        }
    }
}

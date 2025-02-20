package com.zerozero.image.presentation.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record PreSignedUrlRequest(
        @Schema(description = "이미지 접두사 ['store', 'user']", example = "store")
        String prefix,

        @Schema(description = "업로드된 이미지 파일명", example = "profile-image.png")
        String fileName
) {
}

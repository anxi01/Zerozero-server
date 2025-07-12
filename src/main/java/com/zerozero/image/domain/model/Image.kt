package com.zerozero.image.domain.model

import com.zerozero.image.exception.ImageErrorType
import com.zerozero.image.exception.ImageException
import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import java.util.*

@Embeddable
data class Image(
    @Column(name = "image_url", nullable = true)
    var imageUrl: String
) {
    init {
        val extension = imageUrl.substringAfterLast('.', "")
        validateExtension(extension)
    }

    companion object {
        private val ALLOWED_EXTENSIONS = setOf("PNG", "JPG", "JPEG", "HEIC")

        fun validateExtension(extension: String) {
            if (!ALLOWED_EXTENSIONS.contains(extension.uppercase(Locale.getDefault()))) {
                throw ImageException(ImageErrorType.INVALID_IMAGE_EXTENSION)
            }
        }
    }
}

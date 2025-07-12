package com.zerozero.image.domain.model

enum class ImagePrefix {
    STORE,
    USER,
    ;

    companion object {
        fun fromString(value: String): ImagePrefix? {
            return entries.firstOrNull { it.name.equals(value, ignoreCase = true) }
        }
    }
}

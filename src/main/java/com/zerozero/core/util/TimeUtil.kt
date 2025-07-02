package com.zerozero.core.util

import java.time.LocalDate
import java.time.format.DateTimeFormatter

object TimeUtil {
    private val YYYY_MM_DD_DOT_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd")

    @JvmStatic
    fun toDotFormattedString(localDate: LocalDate): String {
        return localDate.format(YYYY_MM_DD_DOT_FORMATTER)
    }
}

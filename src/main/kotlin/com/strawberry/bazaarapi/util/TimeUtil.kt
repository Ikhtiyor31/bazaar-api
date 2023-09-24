package com.strawberry.bazaarapi.util

import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime

object TimeUtil {

    private val UTC = ZoneId.of("UTC")
    private val UZT = ZoneId.of("Asia/Tashkent")


    fun getCurrentTimeZoneOfUTC(): ZonedDateTime {
        return ZonedDateTime.now(UTC)
    }

    fun getCurrentTimeZoneOfUZT(): ZonedDateTime {
        return ZonedDateTime.now(UZT)
    }

    fun getCurrentLocalTimeInUTC(): LocalDateTime {
        return getCurrentTimeZoneOfUTC().toLocalDateTime()
    }

    fun getCurrentLocalTimeInUZT(): LocalDateTime {
        return getCurrentTimeZoneOfUZT().toLocalDateTime()
    }

    fun getUZTZoneId(): ZoneId {
        return ZoneId.of("Asia/Tashkent")
    }
}
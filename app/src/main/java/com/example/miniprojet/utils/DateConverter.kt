package com.example.miniprojet.utils

import androidx.room.TypeConverter
import java.util.Date

/**
 * Type converter for Room database to convert Date objects to Long and vice versa
 */
class DateConverter {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}

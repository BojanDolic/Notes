package com.dolic.kotlinnotesapp

import androidx.room.TypeConverter
import java.util.*

class Converters {

    @TypeConverter
    fun convertToUnixTimestamp(date: Date): Long = date.time

    @TypeConverter
    fun convertToDate(timestamp: Long): Date = Date(timestamp)

}
package com.myplaygroup.app.feature_main.data.local

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.github.boguszpawlowski.composecalendar.day.Day
import java.time.DayOfWeek
import java.util.*

class Converters {

    @TypeConverter
    fun fromList(list: List<String>) : String{
        return Gson().toJson(list)
    }

    @TypeConverter
    fun toList(string: String): List<String> {
        return Gson().fromJson(string, object : TypeToken<List<String>>(){}.type)
    }

    @TypeConverter
    fun fromHealth(list: List<DayOfWeek>) : String {
        return fromList(list.map { x -> x.name })
    }

    @TypeConverter
    fun toDayOfWeek(value: String) : List<DayOfWeek> {
        return toList(value).map { x -> enumValueOf(x) }
    }

    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return if (date == null) null else date.time
    }
}
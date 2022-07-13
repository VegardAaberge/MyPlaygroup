package com.myplaygroup.app.feature_main.data.local

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.sql.Date
import java.time.LocalDate

class Converters {

    @TypeConverter
    fun fromList(list: List<String>?) : String{
        val listToJson = list?.toMutableList() ?: mutableListOf()
        listToJson.removeAll { it.isBlank() }
        return Gson().toJson(listToJson)
    }

    @TypeConverter
    fun toList(string: String): List<String> {
        val list = Gson().fromJson<List<String>>(
            string,
            object : TypeToken<List<String>>(){}.type
        ).toMutableList()
        list.removeAll { it.isBlank()}

        return list
    }

    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return if (date == null) null else date.time
    }

    @TypeConverter
    fun convertLocalDateToSqlDate(localDate: LocalDate): Date? {
        return localDate.let { Date.valueOf(localDate.toString()) }
    }
}
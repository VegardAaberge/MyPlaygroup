package com.myplaygroup.app.core.util

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class TextUtils {
    companion object {
        fun deCamelCasealize(camelCasedString: String): String {
            if (camelCasedString.isEmpty())
                return camelCasedString
            val result = StringBuilder()
            result.append(camelCasedString[0])
            for (i in 1 until camelCasedString.length) {
                if (Character.isUpperCase(camelCasedString[i])) result.append(" ")
                result.append(camelCasedString[i])
            }
            return result.toString()
        }

        fun displayMessageDate(date: LocalDateTime?): String {
            if(date == null)
                return ""

            val pattern = if(date.plusDays(6) > LocalDateTime.now()){
                "EEE hh:mm a"
            } else "d/M/yy hh:mm a"

            return date.format(DateTimeFormatter.ofPattern(pattern)).toString()
        }

        fun displayShortMessageDate(date: LocalDateTime?): String {
            if(date == null)
                return ""

            val currentTime = LocalDateTime.now()
            val pattern = if(date.plusDays(1) > currentTime && date.dayOfMonth == currentTime.dayOfMonth)
                "HH:mm"
            else if(date.plusDays(6) > currentTime){
                "EEE HH:mm"
            } else "d/M/yy HH:mm"

            return date.format(DateTimeFormatter.ofPattern(pattern)).toString()
        }
    }
}

fun String.display() : String {
    return this.lowercase().replaceFirstChar { x -> x.uppercase() }.replace('_', ' ')
}
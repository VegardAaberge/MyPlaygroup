package com.myplaygroup.app.core.util

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
    }
}
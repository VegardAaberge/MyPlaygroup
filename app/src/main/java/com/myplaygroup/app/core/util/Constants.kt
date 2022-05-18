package com.myplaygroup.app.core.util

object Constants {

    const val MAIN_DATABASE_NAME = "main_db"

    const val DEBUG_KEY = "MyPlaygroupApp"

    const val ENCRYPTED_SHARED_PREF_NAME = "enc_shared_pref"
    const val KEY_USERNAME = "KEY_USERNAME"
    const val KEY_ACCESS_TOKEN = "KEY_ACCESS_TOKEN"
    const val KEY_REFRESH_TOKEN = "KEY_REFRESH_TOKEN"
    const val NO_VALUE = "KEY_HAS_NO_VALUE"

    const val KEY_PROFILE_NAME = "KEY_PROFILE_NAME"
    const val KEY_PHONE_NUMBER = "KEY_PHONE_NUMBER"
    const val KEY_EMAIL = "KEY_EMAIL"


    const val BASE_URL = "http://vegardaaberge.no:8080"

    val IGNORE_AUTH_URLS = listOf("/api/v1/login", "/api/v1/reset-password/send", "/api/v1/reset-password/verify")
}
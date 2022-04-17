package com.myplaygroup.app.core.util

object Constants {

    const val DEBUG_KEY = "MyPlaygroupApp"

    const val ENCRYPTED_SHARED_PREF_NAME = "enc_shared_pref"
    const val KEY_USERNAME = "KEY_USERNAME"
    const val KEY_PASSWORD = "KEY_PASSWORD"
    const val NO_USERNAME = "NO_USERNAME"
    const val NO_PASSWORD = "NO_PASSWORD"

    const val BASE_URL = "http://10.0.2.2:8080"

    val IGNORE_AUTH_URLS = listOf("/login", "/register")
}
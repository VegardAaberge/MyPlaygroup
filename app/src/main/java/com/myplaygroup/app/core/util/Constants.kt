package com.myplaygroup.app.core.util

object Constants {

    const val MAIN_DATABASE_NAME = "main_db"

    const val DEBUG_KEY = "MyPlaygroupApp"

    const val KEYSET_NAME = "master_keyset"
    const val PREFERENCE_FILE = "master_key_preference"
    const val MASTER_KEY_URI = "android-keystore://master_key"
    const val DATASTORE_FILE = "user.pb"

    const val NO_VALUE = "VALUE_HAS_NOT_BEEN_SET"

    const val LOCALHOST_URL = "https://192.168.50.91:8080"
    const val SERVER_URL = "https://vegardaaberge.no:8080"
    const val LOCALHOST_SOCKET_URL = "wss://192.168.50.91:8080"
    const val SERVER_SOCKET_URL = "wss://vegardaaberge.no:8080"
    const val BASE_URL = SERVER_URL
    const val BASE_SOCKET_URL = SERVER_SOCKET_URL

    public const val AUTHENTICATION_ERROR_MESSAGE = "Couldn't reach server: Failed to load token"

    val IGNORE_AUTH_URLS = listOf("/api/v1/login", "/api/v1/reset-password/send", "/api/v1/reset-password/verify")
}
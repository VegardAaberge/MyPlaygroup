package com.myplaygroup.app.feature_admin.presentation

import android.net.Uri

data class AdminState (
    val adminUri: Uri? = null,
    val username: String = "",
    val profileName: String = ""
)
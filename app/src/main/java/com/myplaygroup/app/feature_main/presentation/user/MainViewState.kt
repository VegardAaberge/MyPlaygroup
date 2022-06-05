package com.myplaygroup.app.feature_main.presentation.user

import android.net.Uri

data class MainViewState (
    val receiver : String = "",
    val usernameUri: Uri? = null,
    val receiverUri: Uri? = null,
)
package com.myplaygroup.app.feature_admin.presentation

import android.net.Uri
import com.myplaygroup.app.feature_admin.presentation.nav_drawer.NavDrawer

data class AdminState (
    val adminUri: Uri? = null,
    val currentRoute: String = NavDrawer.OVERVIEW
)
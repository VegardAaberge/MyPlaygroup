package com.myplaygroup.app.core.presentation

import android.content.SharedPreferences
import com.myplaygroup.app.core.util.Constants.KEY_PASSWORD
import com.myplaygroup.app.core.util.Constants.KEY_USERNAME
import com.myplaygroup.app.core.util.Constants.NO_PASSWORD
import com.myplaygroup.app.core.util.Constants.NO_USERNAME
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RootViewModel @Inject constructor(
    private val sharedPref: SharedPreferences,
) : BaseViewModel() {

    fun IsAuthenticated(): Boolean {
        val currentUsername = sharedPref.getString(KEY_USERNAME, NO_USERNAME) ?: NO_USERNAME
        val currentPassword = sharedPref.getString(KEY_PASSWORD, NO_PASSWORD) ?: NO_PASSWORD

        return currentUsername != NO_USERNAME && currentPassword != NO_PASSWORD
    }
}
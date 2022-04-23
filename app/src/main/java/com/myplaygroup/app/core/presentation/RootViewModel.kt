package com.myplaygroup.app.core.presentation

import android.content.SharedPreferences
import com.myplaygroup.app.core.data.remote.BasicAuthInterceptor
import com.myplaygroup.app.core.util.Constants.KEY_PASSWORD
import com.myplaygroup.app.core.util.Constants.KEY_USERNAME
import com.myplaygroup.app.core.util.Constants.NO_PASSWORD
import com.myplaygroup.app.core.util.Constants.NO_USERNAME
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RootViewModel @Inject constructor(
    private val sharedPref: SharedPreferences,
    private val basicAuthInterceptor: BasicAuthInterceptor
) : BaseViewModel() {

    fun isAuthenticated(): Boolean {
        val currentUsername = sharedPref.getString(KEY_USERNAME, NO_USERNAME) ?: NO_USERNAME
        val currentPassword = sharedPref.getString(KEY_PASSWORD, NO_PASSWORD) ?: NO_PASSWORD

        val isAuthenticated = currentUsername != NO_USERNAME && currentPassword != NO_PASSWORD
        if(isAuthenticated){
            basicAuthInterceptor.username = currentUsername
            basicAuthInterceptor.password = currentPassword
        }

        return isAuthenticated
    }
}
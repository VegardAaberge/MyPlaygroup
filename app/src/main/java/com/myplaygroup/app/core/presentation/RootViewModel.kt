package com.myplaygroup.app.core.presentation

import android.content.SharedPreferences
import com.myplaygroup.app.core.data.remote.BasicAuthInterceptor
import com.myplaygroup.app.core.util.Constants.KEY_ACCESS_TOKEN
import com.myplaygroup.app.core.util.Constants.NO_VALUE
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RootViewModel @Inject constructor(
    private val sharedPref: SharedPreferences,
    private val basicAuthInterceptor: BasicAuthInterceptor
) : BaseViewModel() {

    fun isAuthenticated(): Boolean {
        val currentAccessToken = sharedPref.getString(KEY_ACCESS_TOKEN, NO_VALUE) ?: NO_VALUE

        val isAuthenticated = currentAccessToken != NO_VALUE
        if(isAuthenticated){
            basicAuthInterceptor.accessToken = currentAccessToken
        }

        return isAuthenticated
    }
}
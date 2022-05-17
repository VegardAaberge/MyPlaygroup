package com.myplaygroup.app.core.presentation

import android.content.SharedPreferences
import androidx.core.content.edit
import com.myplaygroup.app.core.data.remote.BasicAuthInterceptor
import com.myplaygroup.app.core.util.Constants
import com.myplaygroup.app.core.util.Constants.KEY_ACCESS_TOKEN
import com.myplaygroup.app.core.util.Constants.KEY_PROFILE_NAME
import com.myplaygroup.app.core.util.Constants.KEY_REFRESH_TOKEN
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
        val currentRefreshToken = sharedPref.getString(KEY_REFRESH_TOKEN, NO_VALUE) ?: NO_VALUE
        val profileName = sharedPref.getString(KEY_PROFILE_NAME, NO_VALUE) ?: NO_VALUE

        if(currentAccessToken == NO_VALUE || currentRefreshToken == NO_VALUE || profileName == NO_VALUE){
            sharedPref.edit {
                putString(KEY_ACCESS_TOKEN, NO_VALUE)
                putString(KEY_REFRESH_TOKEN, NO_VALUE)
                apply()
            }
            return false
        }

        basicAuthInterceptor.accessToken = currentAccessToken
        return true
    }
}
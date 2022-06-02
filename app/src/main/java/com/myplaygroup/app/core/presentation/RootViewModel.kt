package com.myplaygroup.app.core.presentation

import com.myplaygroup.app.core.data.remote.BasicAuthInterceptor
import com.myplaygroup.app.core.data.settings.UserSettings
import com.myplaygroup.app.core.domain.settings.UserSettingsManager
import com.myplaygroup.app.core.util.Constants.NO_VALUE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import javax.inject.Inject

@HiltViewModel
class RootViewModel @Inject constructor(
    private val basicAuthInterceptor: BasicAuthInterceptor,
    private val userSettingsManager: UserSettingsManager
) : BaseViewModel() {

    suspend fun getUserSettingsWithRole(): UserSettings? {

        val userSettings = userSettingsManager.getFlow().first()

        if(userSettings.accessToken == NO_VALUE || userSettings.refreshToken == NO_VALUE || userSettings.username == NO_VALUE || userSettings.userRole == NO_VALUE || userSettings.profileName == NO_VALUE){
            userSettingsManager.updateTokens(
                accessToken = NO_VALUE,
                refreshToken = NO_VALUE
            )
            return null
        }

        basicAuthInterceptor.accessToken = userSettings.accessToken
        return userSettings
    }
}
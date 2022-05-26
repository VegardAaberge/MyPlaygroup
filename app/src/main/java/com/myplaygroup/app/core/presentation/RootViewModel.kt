package com.myplaygroup.app.core.presentation

import androidx.datastore.core.DataStore
import com.myplaygroup.app.core.data.pref.UserSettings
import com.myplaygroup.app.core.data.remote.BasicAuthInterceptor
import com.myplaygroup.app.core.util.Constants.NO_VALUE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

@HiltViewModel
class RootViewModel @Inject constructor(
    private val basicAuthInterceptor: BasicAuthInterceptor,
    private val dataStore: DataStore<UserSettings>
) : BaseViewModel() {

    suspend fun isAuthenticated(): Boolean {

        val userSettings = dataStore.data.first()

        if(userSettings.accessToken == NO_VALUE || userSettings.refreshToken == NO_VALUE || userSettings.username == NO_VALUE || userSettings.profileName == NO_VALUE){
            dataStore.updateData {
                it.copy(
                    accessToken = NO_VALUE,
                    refreshToken = NO_VALUE
                )
            }
            return false
        }

        basicAuthInterceptor.accessToken = userSettings.accessToken
        return true
    }
}
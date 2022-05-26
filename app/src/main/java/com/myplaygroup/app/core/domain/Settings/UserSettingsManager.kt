package com.myplaygroup.app.core.domain.Settings

import com.myplaygroup.app.core.data.settings.UserSettings
import kotlinx.coroutines.flow.Flow

interface UserSettingsManager {
    suspend fun updateProfileInfo(profileName: String, email: String, phoneNumber: String)

    suspend fun updateUsername(username: String)

    suspend fun updateTokens(accessToken: String, refreshToken: String)

    suspend fun clearData()

    fun getFlow() : Flow<UserSettings>

    fun getFlow (mapData: (data: Flow<UserSettings>) -> Flow<String>): Flow<String>
}
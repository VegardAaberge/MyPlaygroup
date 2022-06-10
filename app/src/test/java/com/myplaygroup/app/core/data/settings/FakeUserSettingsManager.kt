package com.myplaygroup.app.core.data.settings

import com.myplaygroup.app.core.domain.settings.UserSettingsManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

class FakeUserSettingsManager : UserSettingsManager {

    var userSettings: UserSettings = UserSettings()

    override suspend fun updateProfileInfo(
        profileName: String,
        phoneNumber: String
    ) {
        userSettings = userSettings.copy(
            profileName = profileName,
            phoneNumber = phoneNumber
        )
    }

    override suspend fun updateUsernameAndRole(username: String, userRole: String) {
        userSettings = userSettings.copy(
            username = username,
            userRole = userRole,
        )
    }

    override suspend fun updateTokens(accessToken: String, refreshToken: String) {
        userSettings = userSettings.copy(
            accessToken = accessToken,
            refreshToken = refreshToken,
        )
    }

    override suspend fun clearData() {
        userSettings = UserSettings()
    }

    override fun getFlow(): Flow<UserSettings> {
        return flow { emit(userSettings) }
    }

    override fun getFlow(mapData: (data: Flow<UserSettings>) -> Flow<String>): Flow<String> {
        return flow {
            val userSettingsValue = mapData(getFlow()).first()
            emit(userSettingsValue)
        }
    }
}
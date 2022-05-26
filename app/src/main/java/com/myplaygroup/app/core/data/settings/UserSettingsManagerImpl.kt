package com.myplaygroup.app.core.data.settings

import android.util.Log
import androidx.datastore.core.DataStore
import com.myplaygroup.app.core.domain.Settings.UserSettingsManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserSettingsManagerImpl @Inject constructor(
    private val dataStore: DataStore<UserSettings>
) : UserSettingsManager {

    val data = dataStore.data

    override suspend fun updateProfileInfo(
        profileName: String,
        email: String,
        phoneNumber: String
    ) {
        dataStore.updateData {
            it.copy(
                profileName = profileName,
                phoneNumber = phoneNumber,
                email = email,
            )
        }
    }

    override suspend fun updateUsername(username: String) {
        dataStore.updateData {
            it.copy(
                username = username
            )
        }
    }

    override suspend fun updateTokens(access_token: String, refresh_token: String) {
        dataStore.updateData {
            it.copy(
                accessToken = access_token,
                refreshToken = refresh_token
            )
        }
    }

    override suspend fun clearData() {
        dataStore.updateData {
            UserSettings()
        }
    }

    override fun getFlow(mapData: (data: Flow<UserSettings>) -> Flow<String>): Flow<String> {
        return mapData(
            getFlow()
        )
    }

    override fun getFlow(): Flow<UserSettings> {
        return dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    Log.d("DataStoreRepository", exception.message.toString())
                    emit(UserSettings())
                } else {
                    throw exception
                }
            }
    }
}
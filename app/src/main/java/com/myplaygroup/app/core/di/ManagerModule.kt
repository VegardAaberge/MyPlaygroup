package com.myplaygroup.app.core.di

import com.myplaygroup.app.core.data.settings.UserSettingsManagerImpl
import com.myplaygroup.app.core.domain.Settings.UserSettingsManager
import com.myplaygroup.app.feature_login.data.repository.LoginRepositoryImpl
import com.myplaygroup.app.feature_login.domain.repository.LoginRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ManagerModule {

    @Binds
    @Singleton
    abstract fun bindUserSettingsManager(
        userSettingsManager: UserSettingsManagerImpl
    ): UserSettingsManager
}
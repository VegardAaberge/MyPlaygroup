package com.myplaygroup.app.core.di

import com.myplaygroup.app.core.data.settings.UserSettingsManagerImpl
import com.myplaygroup.app.core.domain.settings.UserSettingsManager
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
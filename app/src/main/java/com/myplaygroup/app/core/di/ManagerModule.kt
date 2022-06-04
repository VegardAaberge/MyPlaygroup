package com.myplaygroup.app.core.di

import com.myplaygroup.app.core.data.settings.UserSettingsManagerImpl
import com.myplaygroup.app.core.domain.settings.UserSettingsManager
import com.myplaygroup.app.feature_profile.domain.use_cases.ProfileUseCases
import dagger.Binds
import dagger.Module
import dagger.Provides
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
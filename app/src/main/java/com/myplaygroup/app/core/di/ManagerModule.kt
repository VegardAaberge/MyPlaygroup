package com.myplaygroup.app.core.di

import com.myplaygroup.app.core.data.settings.UserSettingsManagerImpl
import com.myplaygroup.app.core.domain.settings.UserSettingsManager
import com.myplaygroup.app.feature_main.domain.interactors.ChatInteractor
import com.myplaygroup.app.feature_main.domain.interactors.EditParametersInteractor
import com.myplaygroup.app.feature_main.domain.interactors.MainDaoInteractor
import com.myplaygroup.app.feature_main.domain.interactors.impl.ChatInteractorImpl
import com.myplaygroup.app.feature_main.domain.interactors.impl.EditParametersInteractorImpl
import com.myplaygroup.app.feature_main.domain.interactors.impl.MainDaoInteractorImpl
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

    @Binds
    @Singleton
    abstract fun bindMainDaoInteractor(
        mainDaoInteractor: MainDaoInteractorImpl
    ): MainDaoInteractor

    @Binds
    @Singleton
    abstract fun bindEditParametersInteractor(
        editParametersInteractor: EditParametersInteractorImpl
    ): EditParametersInteractor

    @Binds
    @Singleton
    abstract fun bindChatInteractor(
        chatInteractor: ChatInteractorImpl
    ): ChatInteractor
}
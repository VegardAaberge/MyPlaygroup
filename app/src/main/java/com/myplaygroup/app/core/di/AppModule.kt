package com.myplaygroup.app.core.di

import android.app.Application
import com.myplaygroup.app.feature_login.data.repository.LoginRepositoryImpl
import com.myplaygroup.app.feature_login.domain.repository.LoginRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

}
package com.myplaygroup.app.core.di

import android.app.Application
import com.myplaygroup.app.feature_login.data.repository.LoginRepositoryImpl
import com.myplaygroup.app.feature_login.domain.repository.LoginRepository
import com.myplaygroup.app.feature_login.domain.use_case.Authenticate
import com.myplaygroup.app.feature_login.domain.use_case.LoginUseCases
import com.myplaygroup.app.feature_login.domain.use_case.ResetPassword
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideLoginRepository(): LoginRepository {
        return LoginRepositoryImpl()
    }

    @Provides
    @Singleton
    fun provideLoginUseCases(repository: LoginRepository): LoginUseCases {
        return LoginUseCases(
            authenticate = Authenticate(repository),
            resetPassword = ResetPassword(repository),
        )
    }
}
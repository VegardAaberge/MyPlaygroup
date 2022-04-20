package com.myplaygroup.app.core.di

import com.myplaygroup.app.core.data.repository.ImageRepositoryImpl
import com.myplaygroup.app.core.domain.repository.ImageRepository
import com.myplaygroup.app.feature_login.data.repository.LoginRepositoryImpl
import com.myplaygroup.app.feature_login.domain.repository.LoginRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindLoginRepository(
        loginRepository: LoginRepositoryImpl
    ): LoginRepository

    @Binds
    @Singleton
    abstract fun bindImageRepositoryRepository(
        imageRepository: ImageRepositoryImpl
    ): ImageRepository
}
package com.devkt.adminguninevigation.di

import com.devkt.adminguninevigation.api.ApiBuilder
import com.devkt.adminguninevigation.repo.Repo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HiltModule {
    @Provides
    @Singleton
    fun provideApi(): ApiBuilder{
        return ApiBuilder
    }

    @Provides
    @Singleton
    fun provideRepo(api: ApiBuilder): Repo{
        return Repo(api)
    }
}
package com.example.quitplace.di

import com.example.quitplace.data.repository.PostRepositoryImpl
import com.example.quitplace.domain.repository.PostRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providePostRepository(): PostRepository {
        return PostRepositoryImpl  // ← УБРАЛ СКОБКИ ()!
    }
}
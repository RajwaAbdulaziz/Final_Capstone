package com.tuwaiq.finalcapstone.di

import com.tuwaiq.finalcapstone.data.remote.memeApi.repo.ApiRepo
import com.tuwaiq.finalcapstone.data.repo.AuthRepoImpl
import com.tuwaiq.finalcapstone.data.repo.ChatRepoImpl
import com.tuwaiq.finalcapstone.data.repo.MoodRepoImpl
import com.tuwaiq.finalcapstone.data.repo.UserRepoImpl
import com.tuwaiq.finalcapstone.domain.repo.AuthRepo
import com.tuwaiq.finalcapstone.domain.repo.ChatRepo
import com.tuwaiq.finalcapstone.domain.repo.MoodRepo
import com.tuwaiq.finalcapstone.domain.repo.UserRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideAuthRepo(): AuthRepo = AuthRepoImpl()

    @Singleton
    @Provides
    fun provideMoodRepo(): MoodRepo = MoodRepoImpl()

    @Singleton
    @Provides
    fun provideUserRepo(): UserRepo = UserRepoImpl()

    @Singleton
    @Provides
    fun provideChatRepo(): ChatRepo = ChatRepoImpl()

//    @Singleton
//    @Provides
//    fun provideMemeApi():
}
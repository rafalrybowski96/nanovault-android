package com.nanovault.android.di

import com.nanovault.android.engine.AICoreManager
import com.nanovault.android.engine.AICoreManagerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AICoreModule {

    @Binds
    @Singleton
    abstract fun bindAICoreManager(
        impl: AICoreManagerImpl
    ): AICoreManager
}
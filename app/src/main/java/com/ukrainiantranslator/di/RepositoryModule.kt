package com.ukrainiantranslator.di

import com.ukrainiantranslator.data.repository.TranslationRepository
import com.ukrainiantranslator.data.repository.TranslationRepositoryImpl
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
    abstract fun bindTranslationRepository(
        impl: TranslationRepositoryImpl
    ): TranslationRepository
}

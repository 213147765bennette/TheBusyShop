package com.ikhokha.techcheck.presentation.di.core


import com.ikhokha.techcheck.data.datasource.item.ItemLocalDataSource
import com.ikhokha.techcheck.data.repository.ItemLocalRepositoryImpl
import com.ikhokha.techcheck.domain.repository.ItemsDataRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    /*internal means that the declarations are visible inside a module.
    A module in kotlin is a set of Kotlin files compiled together.*/

    @Singleton
    @Provides
    fun provideItemLocalRepository(
        itemLocalDataSource: ItemLocalDataSource
    ): ItemsDataRepository {
        return ItemLocalRepositoryImpl(itemLocalDataSource)
    }

}
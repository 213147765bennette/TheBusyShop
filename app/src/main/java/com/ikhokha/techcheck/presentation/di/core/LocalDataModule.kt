package com.ikhokha.techcheck.presentation.di.core


import com.ikhokha.techcheck.data.datasource.item.ItemLocalDataSource
import com.ikhokha.techcheck.data.datasource.item.ItemLocalDataSourceImpl
import com.ikhokha.techcheck.data.localstorage.dao.ItemDAO
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class LocalDataModule {

    @Provides
    @Singleton
    fun provideItemLocalDatSource(itemDAO: ItemDAO) : ItemLocalDataSource {
        return ItemLocalDataSourceImpl(itemDAO)
    }

}
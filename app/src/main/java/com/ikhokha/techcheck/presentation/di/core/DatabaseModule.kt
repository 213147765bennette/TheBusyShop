package com.ikhokha.techcheck.presentation.di.core

import android.app.Application
import androidx.room.Room
import com.ikhokha.techcheck.data.localstorage.dao.ItemDAO
import com.ikhokha.techcheck.data.localstorage.db.DatabaseService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    /*internal means that the declarations are visible inside a module.
     A module in kotlin is a set of Kotlin files compiled together.*/

    @Singleton
    @Provides
    fun provideBusyShopDatabase(app: Application): DatabaseService {
        return Room.databaseBuilder(app, DatabaseService::class.java,"busy_shop")
            .fallbackToDestructiveMigration()
            .build()

    }

    @Singleton
    @Provides
    fun provideItemDao(databaseService: DatabaseService): ItemDAO {
        return databaseService.itemDao()
    }


}
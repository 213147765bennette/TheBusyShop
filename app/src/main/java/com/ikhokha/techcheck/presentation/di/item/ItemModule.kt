package com.ikhokha.techcheck.presentation.di.item

import com.ikhokha.techcheck.domain.repository.ItemsDataRepository
import com.ikhokha.techcheck.presentation.dashboard.DashboardViewModelFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ItemModule {

    @Singleton
    @Provides
    fun provideDashboardViewModelFactory(
        itemLocalRepository: ItemsDataRepository
    ): DashboardViewModelFactory {
        return DashboardViewModelFactory(
            itemLocalRepository
        )
    }


}
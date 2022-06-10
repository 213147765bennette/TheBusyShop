package com.ikhokha.techcheck.presentation.di.core

import com.ikhokha.techcheck.domain.repository.ItemsDataRepository
import com.ikhokha.techcheck.domain.usecase.DeleteItemsDataUseCase
import com.ikhokha.techcheck.domain.usecase.GetItemsDataUseCase
import com.ikhokha.techcheck.domain.usecase.SaveItemsDataUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class UseCaseModule {

    /*internal means that the declarations are visible inside a module.
     A module in kotlin is a set of Kotlin files compiled together.*/

    @Singleton
    @Provides
    fun providesGetItemsDataUseCase(itemsDataRepository: ItemsDataRepository): GetItemsDataUseCase {
        return GetItemsDataUseCase(itemsDataRepository)
    }

    @Singleton
    @Provides
    fun providesSaveItemsDataUseCase(itemsDataRepository: ItemsDataRepository): SaveItemsDataUseCase {
        return SaveItemsDataUseCase(itemsDataRepository)
    }

    @Singleton
    @Provides
    fun providesDeleteItemsDataUseCase(itemsDataRepository: ItemsDataRepository): DeleteItemsDataUseCase {
        return DeleteItemsDataUseCase(itemsDataRepository)
    }

}
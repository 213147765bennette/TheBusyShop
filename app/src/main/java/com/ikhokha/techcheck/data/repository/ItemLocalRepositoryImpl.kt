package com.ikhokha.techcheck.data.repository

import com.ikhokha.techcheck.data.datasource.item.ItemLocalDataSource
import com.ikhokha.techcheck.data.entity.CartEntity
import com.ikhokha.techcheck.domain.repository.ItemsDataRepository
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

/**
 * Created by Bennette Molepo on 04/06/2022.
 */
class ItemLocalRepositoryImpl(
    private val itemLocalDataSource: ItemLocalDataSource
) :ItemsDataRepository{
    override  fun insertCartItem(cartEntity: CartEntity) {
        itemLocalDataSource.insertCartItem(cartEntity)
    }

    override fun deleteCartItem(cartEntity: CartEntity) :Single<Int>{
        return itemLocalDataSource.deleteCartItem(cartEntity)
    }

    override suspend fun deleteAll() :Int{
        return itemLocalDataSource.deleteAll()
    }

    override suspend fun getCartItems(): List<CartEntity> {
        return itemLocalDataSource.getCartItems()
    }

    override fun getAllCartItems(): Observable<List<CartEntity>> {
        return itemLocalDataSource.getAllCartItems()
    }

    override suspend fun updateItem(cartEntity: CartEntity) {
        return itemLocalDataSource.updateItem(cartEntity)
    }

}
package com.ikhokha.techcheck.domain.repository

import com.ikhokha.techcheck.data.entity.CartEntity
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

/**
 * Created by Bennette Molepo on 04/06/2022.
 */
interface ItemsDataRepository {

    fun insertCartItem(cartEntity: CartEntity)
    fun deleteCartItem(cartEntity: CartEntity): Single<Int>
    suspend fun deleteAll(): Int
    suspend fun getCartItems(): List<CartEntity>
    fun getAllCartItems(): Observable<List<CartEntity>>
    suspend fun updateItem(cartEntity: CartEntity)
}
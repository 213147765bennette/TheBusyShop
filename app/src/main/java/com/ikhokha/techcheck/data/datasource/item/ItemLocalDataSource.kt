package com.ikhokha.techcheck.data.datasource.item

import com.ikhokha.techcheck.data.entity.CartEntity
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

/**
 * Created by Bennette Molepo on 04/06/2022.
 */
interface ItemLocalDataSource {

    fun insertCartItem(cartEntity: CartEntity)
    fun deleteCartItem(cartEntity: CartEntity): Single<Int>
    suspend fun deleteAll():Int
    suspend fun getCartItems(): List<CartEntity>
    fun getAllCartItems(): Observable<List<CartEntity>>

}
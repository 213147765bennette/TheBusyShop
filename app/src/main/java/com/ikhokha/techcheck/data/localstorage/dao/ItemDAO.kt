package com.ikhokha.techcheck.data.localstorage.dao

import androidx.room.*
import com.ikhokha.techcheck.data.entity.CartEntity
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

/**
 * Created by Bennette Molepo on 04/06/2022.
 */
@Dao
interface ItemDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCartItem(cartEntity: CartEntity)

    @Delete
    fun deleteCartItem(cartEntity: CartEntity): Single<Int>

    @Query("DELETE FROM cart")
    suspend fun deleteAll(): Int

    @Query("SELECT * from cart ORDER BY id DESC")
    fun getAllCartItems(): Observable<List<CartEntity>>

    @Query("SELECT * FROM cart")
    suspend fun getCartItems(): List<CartEntity>

    @Update
    suspend fun updateItem(cartEntity: CartEntity)

}
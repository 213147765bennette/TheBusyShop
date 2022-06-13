package com.ikhokha.techcheck.data.datasource.item

import com.ikhokha.techcheck.data.entity.CartEntity
import com.ikhokha.techcheck.data.localstorage.dao.ItemDAO
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Created by Bennette Molepo on 04/06/2022.
 */
class ItemLocalDataSourceImpl(
    private val itemDAO: ItemDAO
    ):ItemLocalDataSource {
    override  fun insertCartItem(cartEntity: CartEntity) {

        //To run this in a background worker thread am using :Dispatchers.IO
        CoroutineScope(Dispatchers.IO).launch {
            itemDAO.insertCartItem(cartEntity)
        }
    }

    override  fun deleteCartItem(cartEntity: CartEntity) : Single<Int> {
        //To run this in a background worker thread am using :Dispatchers.IO
        /*CoroutineScope(Dispatchers.IO).launch {

        }*/
        return itemDAO.deleteCartItem(cartEntity)

    }

    override suspend fun deleteAll():Int {
        return itemDAO.deleteAll()
    }

    override suspend fun getCartItems(): List<CartEntity> {
        //Room execute this query in a background thread,
        // so we don't need to explicitly write code for background processing
        return itemDAO.getCartItems()
    }

    override fun getAllCartItems(): Observable<List<CartEntity>> {
        return  itemDAO.getAllCartItems()
    }

    override suspend fun updateItem(cartEntity: CartEntity) {
        //To run this in a background worker thread am using :Dispatchers.IO
        CoroutineScope(Dispatchers.IO).launch {
            itemDAO.updateItem(cartEntity)
        }
    }
}
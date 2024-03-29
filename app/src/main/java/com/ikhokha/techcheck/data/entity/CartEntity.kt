package com.ikhokha.techcheck.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by Bennette Molepo on 04/06/2022.
 */
@Entity(tableName = "cart")
data class CartEntity (
    @PrimaryKey(autoGenerate = true)
    var id : Long = 0L,
    @ColumnInfo(name = "itemCode")
    var itemCode: String,
    @ColumnInfo(name = "description")
    var description: String,
    @ColumnInfo(name = "image")
    var image: String,
    @ColumnInfo(name = "price")
    var price: Double,
    @ColumnInfo(name = "quantity")
    var quantity: Int
 )
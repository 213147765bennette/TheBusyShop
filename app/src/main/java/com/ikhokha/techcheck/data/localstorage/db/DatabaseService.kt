package com.ikhokha.techcheck.data.localstorage.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ikhokha.techcheck.data.entity.CartEntity
import com.ikhokha.techcheck.data.localstorage.dao.ItemDAO

@Database(
    entities = [CartEntity::class],
    version = 1,
    exportSchema = false
)
abstract class DatabaseService : RoomDatabase(){
    abstract fun itemDao(): ItemDAO
}
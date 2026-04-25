package com.example.api.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [FavoritePost::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteDao(): FavoriteDao
}
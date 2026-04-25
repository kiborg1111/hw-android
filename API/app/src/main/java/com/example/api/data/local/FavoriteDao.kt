package com.example.api.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {

    @Query("SELECT * FROM favorites")
    fun getAll(): Flow<List<FavoritePost>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(post: FavoritePost)

    @Delete
    suspend fun delete(post: FavoritePost)
}
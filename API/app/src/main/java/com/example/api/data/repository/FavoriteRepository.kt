package com.example.api.data.repository

import com.example.api.data.local.FavoriteDao
import com.example.api.data.local.FavoritePost
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FavoriteRepository @Inject constructor(
    private val dao: FavoriteDao
) {

    fun getFavorites(): Flow<List<FavoritePost>> = dao.getAll()

    suspend fun add(post: FavoritePost) {
        dao.insert(post)
    }

    suspend fun remove(post: FavoritePost) {
        dao.delete(post)
    }
}
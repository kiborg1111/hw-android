package com.example.api.data.repository

import com.example.api.data.remote.ApiService
import com.example.api.domain.model.Post
import javax.inject.Inject

class PostRepository @Inject constructor(
    private val api: ApiService
) {

    suspend fun getPosts(): List<Post> {
        return api.getPosts().map {
            Post(it.id, it.title, it.body)
        }
    }

    suspend fun getPost(id: Int): Post {
        val dto = api.getPost(id)
        return Post(dto.id, dto.title, dto.body)
    }
}
package com.example.api.data.repository

import com.example.api.data.remote.RetrofitInstance
import com.example.api.domain.model.Post

class PostRepository {

    private val api = RetrofitInstance.api

    suspend fun getPosts(): List<Post> {
        return api.getPosts().map {
            Post(
                id = it.id,
                title = it.title,
                description = it.body
            )
        }
    }

    suspend fun getPost(id: Int): Post {
        val dto = api.getPost(id)
        return Post(dto.id, dto.title, dto.body)
    }
}


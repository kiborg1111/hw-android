package com.example.api.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
data class FavoritePost(
    @PrimaryKey val id: Int,
    val title: String,
    val description: String
)
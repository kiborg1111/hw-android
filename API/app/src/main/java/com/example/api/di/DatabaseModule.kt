package com.example.api.di

import android.content.Context
import androidx.room.Room
import com.example.api.data.local.AppDatabase
import com.example.api.data.local.FavouriteDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): AppDatabase =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "hw3_db"
        ).build()

    @Provides
    fun provideFavouriteDao(db: AppDatabase): FavouriteDao =
        db.favouriteDao()
}
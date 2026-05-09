package com.example.api.data

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.api.data.local.AppDatabase
import com.example.api.data.local.FavouriteEntity
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RoomIntegrationTest {

    private lateinit var db: AppDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun `favourite is saved and retrieved correctly`() = runTest {
        val entity = FavouriteEntity(1, "Rick", "Alive", "Human", "img")
        db.favouriteDao().upsert(entity)

        val favourites = db.favouriteDao().getFavourites()
        assertEquals(1, favourites.size)
        assertEquals("Rick", favourites[0].name)
    }

    @Test
    fun `no duplicates in Room database`() = runTest {
        val entity = FavouriteEntity(1, "Rick", "Alive", "Human", "img")
        db.favouriteDao().upsert(entity)
        db.favouriteDao().upsert(entity) // повтор

        assertEquals(1, db.favouriteDao().getFavourites().size)
    }
}
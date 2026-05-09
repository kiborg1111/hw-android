package com.example.api.repository

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.api.data.CharacterRepository
import com.example.api.data.local.AppDatabase
import com.example.api.data.remote.FakeCharacterApi
import com.example.api.model.Character
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CharacterRepositoryIntegrationTest {

    private lateinit var db: AppDatabase
    private lateinit var repository: CharacterRepository
    private val fakeApi = FakeCharacterApi()

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        repository = CharacterRepository(fakeApi, db.favouriteDao())
    }

    @After
    fun teardown() {
        db.close()
    }

    @Test
    fun `toggle favourite persists and reflects in search`() = runTest {
        val character = Character(1, "Rick Sanchez", "Alive", "Human", "", false)

        repository.toggleFavourite(character)
        val favourites = repository.getFavourites()

        assertEquals(1, favourites.size)
        assertTrue(favourites[0].isFavourite)
    }
}
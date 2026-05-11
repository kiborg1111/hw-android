package com.example.api.repository

import com.example.api.data.CharacterRepository
import com.example.api.data.local.FavouriteDao
import com.example.api.data.remote.CharacterApi
import com.example.api.data.remote.CharacterDto
import com.example.api.data.remote.CharacterResponse
import com.example.api.model.Character
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException

class CharacterRepositoryTest {

    private lateinit var repository: CharacterRepository
    private val api: CharacterApi = mockk()
    private val favouriteDao: FavouriteDao = mockk(relaxed = true)

    @Before
    fun setup() {
        repository = CharacterRepository(api, favouriteDao)
    }

    @Test
    fun `searchCharacters successfully maps data and favourite status`() = runTest {
        val dto = CharacterDto(1, "Rick Sanchez", "Alive", "Human", "https://...")
        coEvery { api.getCharacters("rick", 1) } returns CharacterResponse(listOf(dto))
        coEvery { favouriteDao.getFavouritesIds() } returns listOf(1)

        val result = repository.searchCharacters("rick", 1)

        assertEquals(1, result.size)
        assertEquals("Rick Sanchez", result[0].name)
        assertTrue(result[0].isFavourite)
    }

    @Test
    fun `searchCharacters returns empty list on 404 Not Found`() = runTest {
        val httpException: HttpException = mockk()
        coEvery { httpException.code() } returns 404
        coEvery { api.getCharacters(any(), any()) } throws httpException

        val result = repository.searchCharacters("unknown", 1)
        assertTrue(result.isEmpty())
    }

    @Test
    fun `toggleFavourite adds when not favourite and removes when favourite`() = runTest {
        val character = Character(1, "Rick", "Alive", "Human", "", false)

        repository.toggleFavourite(character)
        coVerify { favouriteDao.upsert(any()) }

        val favCharacter = character.copy(isFavourite = true)
        repository.toggleFavourite(favCharacter)
        coVerify { favouriteDao.deleteById(1) }
    }

    @Test
    fun `getFavourites returns correct domain models`() = runTest {
        coEvery { favouriteDao.getFavourites() } returns emptyList()

        val result = repository.getFavourites()
        assertTrue(result.isEmpty())
    }

    @Test
    fun `no duplicate favourites when toggled twice`() = runTest {
        val character = Character(1, "Rick", "", "", "", false)
        repository.toggleFavourite(character)
        repository.toggleFavourite(character.copy(isFavourite = true)) // повтор

        coVerify(exactly = 1) { favouriteDao.upsert(any()) }
        coVerify(exactly = 1) { favouriteDao.deleteById(any()) }
    }
}
package com.example.api.data.remote

class FakeCharacterApi : CharacterApi {
    override suspend fun getCharacters(name: String, page: Int): CharacterResponse {
        return CharacterResponse(
            results = listOf(
                CharacterDto(
                    id = 1,
                    name = "Rick Sanchez",
                    status = "Alive",
                    species = "Human",
                    image = "https://rickandmortyapi.com/api/character/avatar/1.jpeg"
                )
            )
        )
    }

    override suspend fun getCharacterById(id: Int): CharacterDto {
        return CharacterDto(
            id = id,
            name = "Rick Sanchez",
            status = "Alive",
            species = "Human",
            image = "https://rickandmortyapi.com/api/character/avatar/1.jpeg"
        )
    }
}
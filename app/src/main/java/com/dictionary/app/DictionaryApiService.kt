package com.dictionary.app

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

data class Meaning(
    val partOfSpeech: String,
    val definitions: List<Definition>
)

data class Definition(
    val definition: String,
    val example: String?
)

data class WordResponse(
    val word: String,
    val meanings: List<Meaning>
)

interface DictionaryApiService {
    @GET("entries/en/{word}")
    fun getWord(@Path("word") word: String): Call<List<WordResponse>>
}

package br.edu.utfpr.app_livros.network

import br.edu.utfpr.app_livros.data.usuario.network.ApiUsuarioService
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

private val json = Json { ignoreUnknownKeys = true }
private val jsonConverterFactory = json.asConverterFactory("application/json".toMediaType())

private const val API_LIVROS_BASE_URL = "http://192.168.3.6:8080"
private val apiLivrosClient = Retrofit.Builder()
    .addConverterFactory(jsonConverterFactory)
    .baseUrl(API_LIVROS_BASE_URL)
    .build()


object ApiService {
    val usuarios: ApiUsuarioService by lazy {
        apiLivrosClient.create(ApiUsuarioService::class.java)
    }
}
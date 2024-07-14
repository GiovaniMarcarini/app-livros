package br.edu.utfpr.app_livros.data.usuario.network

import br.edu.utfpr.app_livros.data.usuario.Usuario
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiUsuarioService {
    @GET("usuarios")
    suspend fun findAll(): List<Usuario>

    @GET("usuarios/{id}")
    suspend fun findById(@Path("id") id: Int): Usuario

    @GET("usuarios/{cpf}/{telefone}")
    suspend fun findByCpfAndTelefone( @Path("cpf") cpf: String,@Path("telefone") telefone: String): List<Usuario>

    @DELETE("usuarios/{id}")
    suspend fun delete(@Path("id") id: Int)

    @POST("usuarios")
    suspend fun save(@Body cliente: Usuario): Response<Usuario>
}
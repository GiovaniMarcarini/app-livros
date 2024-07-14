package br.edu.utfpr.app_livros.data.usuario

import kotlinx.serialization.Serializable

@Serializable
data class Usuario(
    val id: Int = 0,
    val nome: String = "",
    val cpf: String = "",
    val telefone: String = "",
)
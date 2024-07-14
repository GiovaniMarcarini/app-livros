package br.edu.utfpr.app_livros.ui.usuario.form.list

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.edu.utfpr.app_livros.data.usuario.Usuario
import br.edu.utfpr.app_livros.network.ApiService
import kotlinx.coroutines.launch

data class UsuariosListUiState(
    val loading: Boolean = false,
    val hasError: Boolean = false,
    val usuarios: List<Usuario> = listOf()
) {
    val success get(): Boolean = !loading && !hasError
}

class UsuariosListViewModel : ViewModel() {
    private val tag: String = "UsuariosListViewModel"
    var uiState: UsuariosListUiState by mutableStateOf(UsuariosListUiState())

    init {
        load()
    }

    fun load() {
        uiState = uiState.copy(
            loading = true,
            hasError = false
        )
        viewModelScope.launch {
            uiState = try {
                val usuarios = ApiService.usuarios.findAll()
                uiState.copy(
                    usuarios = usuarios,
                    loading = false
                )
            } catch (ex: Exception) {
                Log.d(tag, "Erro ao carregar lista de usuarios", ex)
                uiState.copy(
                    hasError = true,
                    loading = false
                )
            }
        }
    }
}
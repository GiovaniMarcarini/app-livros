package br.edu.utfpr.app_livros.ui.login

import android.net.http.HttpException
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.edu.utfpr.app_livros.data.usuario.Usuario
import br.edu.utfpr.app_livros.network.ApiService
import kotlinx.coroutines.launch
import retrofit2.http.HTTP

data class LoginFormState(
    val cpf: String = "",
    val telefone: String = ""
) {
    val isValid: Boolean
        get() = cpf.isNotBlank() && telefone.isNotBlank()
}

data class LoginUiState(
    val isLoading: Boolean = false,
    val hasError: Boolean = false,
    val errorMessage: String? = null,
    val formState: LoginFormState = LoginFormState(),
    val loginSuccessful: Boolean = false
)

class LoginViewModel : ViewModel() {
    var uiState by mutableStateOf(LoginUiState())
        private set

    fun onCpfChanged(cpf: String) {
        uiState = uiState.copy(formState = uiState.formState.copy(cpf = cpf))
    }

    fun onTelefoneChanged(telefone: String) {
        uiState = uiState.copy(formState = uiState.formState.copy(telefone = telefone))
    }

    fun login(onLoginSuccess: () -> Unit) {
        if (!uiState.formState.isValid) {
            uiState = uiState.copy(hasError = true, errorMessage = "CPF e telefone são obrigatórios")
            return
        }
        uiState = uiState.copy(isLoading = true, hasError = false, errorMessage = null)
        viewModelScope.launch {
            try {
                Log.d("LoginViewModel", "Iniciando chamada à API")
                val usuarios = ApiService.usuarios.findByCpfAndTelefone(uiState.formState.cpf, uiState.formState.telefone)
                Log.d("LoginViewModel", "Resposta da API: $usuarios")

                if (usuarios.isNotEmpty()) {
                    uiState = uiState.copy(isLoading = false, loginSuccessful = true)
                    onLoginSuccess()
                } else {
                    uiState = uiState.copy(isLoading = false, hasError = true, errorMessage = "Usuário não encontrado")
                }
            } catch (e: retrofit2.HttpException) {
                Log.e("LoginViewModel", "HttpException: ${e.message}")
                if (e.code() == 404) {
                    uiState = uiState.copy(isLoading = false, hasError = true, errorMessage = "Usuário não encontrado")
                } else {
                    uiState = uiState.copy(isLoading = false, hasError = true, errorMessage = e.localizedMessage)
                }
            } catch (e: Exception) {
                Log.e("LoginViewModel", "Exception: ${e.message}", e)
                uiState = uiState.copy(isLoading = false, hasError = true, errorMessage = e.localizedMessage)
            }
        }
    }
}
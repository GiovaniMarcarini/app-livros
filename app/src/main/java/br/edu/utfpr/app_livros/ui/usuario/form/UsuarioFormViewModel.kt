package br.edu.utfpr.app_livros.ui.usuario.form

import android.util.Log
import androidx.annotation.StringRes
import androidx.compose.runtime.getValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.edu.utfpr.app_livros.network.ApiService
import kotlinx.coroutines.launch
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import br.edu.utfpr.app_livros.R
import br.edu.utfpr.app_livros.data.usuario.Usuario

data class FormField(
    val value: String = "",
    @StringRes
    val errorMessageCode: Int? = null
)

data class FormState(
    val nome: FormField = FormField(),
    val cpf: FormField = FormField(),
    val telefone: FormField = FormField(),

) {
    val isValid get(): Boolean = nome.errorMessageCode == null &&
            cpf.errorMessageCode == null &&
            telefone.errorMessageCode == null
}

data class UsuarioFormUiState(
    val usuarioId: Int = 0,
    val isLoading: Boolean = false,
    val hasErrorLoading: Boolean = false,
    val formState: FormState = FormState(),
    val isSaving: Boolean = false,
    val hasErrorSaving: Boolean = false,
    val usuarioSaved: Boolean = false,
    val apiValidationError: String = ""
) {
    val isNewusuario get(): Boolean = usuarioId <= 0
    val isSuccessLoading get(): Boolean = !isLoading && !hasErrorLoading
}

class UsuarioFormViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val tag: String = "UsuarioFormViewModel"
    private val usuarioId: Int = savedStateHandle.get<String>("id")?.toIntOrNull() ?: 0

    var uiState: UsuarioFormUiState by mutableStateOf(UsuarioFormUiState())

    init {
        if (usuarioId > 0) {
            loadUsuario()
        }
    }

    fun loadUsuario() {
        uiState = uiState.copy(
            isLoading = true,
            hasErrorLoading = false,
            usuarioId = usuarioId
        )
        viewModelScope.launch {
            uiState = try {
                val usuario = ApiService.usuarios.findById(usuarioId)
                uiState.copy(
                    isLoading = false,
                    formState = FormState(
                        nome = FormField(usuario.nome),
                        cpf = FormField(usuario.cpf),
                        telefone = FormField(usuario.telefone),
                    )
                )
            } catch (ex: Exception) {
                Log.d(tag, "Erro ao carregar os dados do usuario com código $usuarioId", ex)
                uiState.copy(
                    isLoading = false,
                    hasErrorLoading = true
                )
            }
        }
    }

    fun onNomeChanged(value: String) {
        if (uiState.formState.nome.value != value) {
            uiState = uiState.copy(
                formState = uiState.formState.copy(
                    nome = uiState.formState.nome.copy(
                        value = value,
                        errorMessageCode = validateNome(value)
                    )
                )
            )
        }
    }

    @StringRes
    private fun validateNome(nome: String): Int? = if (nome.isBlank()) {
        R.string.nome_obrigatorio
    } else {
        null
    }

    fun onCpfChanged(value: String) {
        val newCpf = value.replace(Regex("\\D"), "")
        if (newCpf.length <= 11 && uiState.formState.cpf.value != newCpf) {
            uiState = uiState.copy(
                formState = uiState.formState.copy(
                    cpf = uiState.formState.cpf.copy(
                        value = newCpf,
                        errorMessageCode = validateCpf(newCpf)
                    )
                )
            )
        }
    }

    @StringRes
    private fun validateCpf(cpf: String): Int? = if (cpf.isBlank()) {
        R.string.cpf_obrigatorio
    } else if (cpf.length != 11) {
        R.string.cpf_invalido
    } else {
        null
    }

    fun onTelefoneChanged(value: String) {
        val newTelefone = value.replace(Regex("\\D"), "")
        if (newTelefone.length <= 11 && uiState.formState.telefone.value != newTelefone) {
            uiState = uiState.copy(
                formState = uiState.formState.copy(
                    telefone = uiState.formState.telefone.copy(
                        value = newTelefone,
                        errorMessageCode = validateTelefone(newTelefone)
                    )
                )
            )
        }
    }

    @StringRes
    private fun validateTelefone(telefone: String): Int? = if (telefone.isBlank()) {
        R.string.telefone_obrigatorio
    } else if (telefone.length < 10 || telefone.length > 11) {
        R.string.telefone_invalido
    } else {
        null
    }

    @StringRes
    private fun validateCep(cep: String): Int? = if (cep.isBlank()) {
        R.string.cep_obrigatorio
    } else if (cep.length != 8) {
        R.string.cep_invalido
    } else {
        null
    }
    

    fun save() {
        if (!isValidForm()) {
            return
        }
        uiState = uiState.copy(
            isSaving = true,
            hasErrorSaving = false
        )
        viewModelScope.launch {
            val usuario = Usuario(
                id = usuarioId,
                nome = uiState.formState.nome.value,
                cpf = uiState.formState.cpf.value,
                telefone = uiState.formState.telefone.value,
            )
            uiState = try {
                val response = ApiService.usuarios.save(usuario)
                if (response.isSuccessful) {
                    uiState.copy(
                        isSaving = false,
                        usuarioSaved = true
                    )
                } else if (response.code() == 400) {
                    val error = Json.parseToJsonElement(response.errorBody()!!.string())
                    val jsonObject = error.jsonObject
                    val apiValidationError =
                        jsonObject.keys.joinToString("\n") {
                            jsonObject[it].toString().replace("\"", "")
                        }
                    uiState.copy(
                        isSaving = false,
                        apiValidationError = apiValidationError
                    )
                } else {
                    uiState.copy(
                        isSaving = false,
                        hasErrorSaving = true
                    )
                }
            } catch (ex: Exception) {
                Log.d(tag, "Erro ao salvar usuario com código $usuarioId", ex)
                uiState.copy(
                    isSaving = false,
                    hasErrorSaving = true
                )
            }
        }
    }

    private fun isValidForm(): Boolean {
        uiState = uiState.copy(
            formState = uiState.formState.copy(
                nome = uiState.formState.nome.copy(
                    errorMessageCode = validateNome(uiState.formState.nome.value)
                ),
                cpf = uiState.formState.cpf.copy(
                    errorMessageCode = validateCpf(uiState.formState.cpf.value)
                ),
                telefone = uiState.formState.telefone.copy(
                    errorMessageCode = validateTelefone(uiState.formState.telefone.value)
                ),
            )
        )
        return uiState.formState.isValid
    }

    fun dismissInformationDialog() {
        uiState = uiState.copy(
            apiValidationError = ""
        )
    }
}
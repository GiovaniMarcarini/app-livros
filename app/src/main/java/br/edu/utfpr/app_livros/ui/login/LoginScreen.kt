package br.edu.utfpr.app_livros.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import br.edu.utfpr.app_livros.ui.login.LoginViewModel
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    modifier: Modifier = Modifier
) {
    val loginViewModel: LoginViewModel = viewModel()


    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Login", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = loginViewModel.uiState.formState.cpf,
            onValueChange = { loginViewModel.onCpfChanged(it) },
            label = { Text("CPF") },
            isError = loginViewModel.uiState.hasError && loginViewModel.uiState.formState.cpf.isBlank(),
            modifier = Modifier.fillMaxWidth()
        )
        if (loginViewModel.uiState.hasError && loginViewModel.uiState.formState.cpf.isBlank()) {
            Text(
                text = "CPF é obrigatório",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.align(Alignment.Start)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = loginViewModel.uiState.formState.telefone,
            onValueChange = { loginViewModel.onTelefoneChanged(it) },
            label = { Text("Telefone") },
            isError = loginViewModel.uiState.hasError && loginViewModel.uiState.formState.telefone.isBlank(),
            modifier = Modifier.fillMaxWidth()
        )
        if (loginViewModel.uiState.hasError && loginViewModel.uiState.formState.telefone.isBlank()) {
            Text(
                text = "Telefone é obrigatório",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.align(Alignment.Start)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                loginViewModel.login(onLoginSuccess)
            },
            enabled = loginViewModel.uiState.formState.isValid && !loginViewModel.uiState.isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Login")
        }

        if (loginViewModel.uiState.hasError && loginViewModel.uiState.errorMessage != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = loginViewModel.uiState.errorMessage ?: "",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}
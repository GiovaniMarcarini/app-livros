package br.edu.utfpr.app_livros

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import br.edu.utfpr.app_livros.ui.AppLivros
import br.edu.utfpr.app_livros.ui.theme.ApplivrosTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ApplivrosTheme {
                AppLivros()
            }
        }
    }
}

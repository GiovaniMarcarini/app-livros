package br.edu.utfpr.app_livros.ui.utils.visualtransformations

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import br.edu.utfpr.app_livros.extensions.toFormattedTelefone

class TelefoneVisualTransformation : VisualTransformation {

    override fun filter(text: AnnotatedString): TransformedText {
        val telefone = text.text
        val formattedTelefone = telefone.toFormattedTelefone()

        return TransformedText(
            AnnotatedString(formattedTelefone),
            TelefoneOffsetMapping
        )
    }

    object TelefoneOffsetMapping : OffsetMapping {
        override fun originalToTransformed(offset: Int): Int {
            return when {
                offset > 6 -> offset + 4
                offset > 2 -> offset + 3
                offset > 0 -> offset + 1
                else -> offset
            }
        }

        override fun transformedToOriginal(offset: Int): Int {
            return when {
                offset > 6 -> offset - 4
                offset > 2 -> offset - 3
                offset > 0 -> offset - 1
                else -> offset
            }
        }
    }
}
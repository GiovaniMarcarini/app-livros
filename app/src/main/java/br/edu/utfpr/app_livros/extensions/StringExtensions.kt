package br.edu.utfpr.app_livros.extensions

import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

fun String.toFormattedCep(): String = this.mapIndexed { index, char ->
    if (index == 5) {
        "-$char"
    } else {
        char
    }
}.joinToString("")

fun String.toFormattedCpf(): String = this.mapIndexed { index, char ->
    when (index) {
        3, 6 -> ".$char"
        9 -> "-$char"
        else -> char
    }
}.joinToString("")

fun String.toFormattedTelefone(): String = this.mapIndexed { index, char ->
    when {
        index == 0 -> "($char"
        index == 2 -> ") $char"
        (index == 6 && this.length < 11) ||
                (index == 7 && this.length == 11) -> "-$char"
        else -> char
    }
}.joinToString("")

fun String.toFormat(pattern: String): String = try {
    ZonedDateTime.parse(this).format(DateTimeFormatter.ofPattern(pattern))
} catch (ex: Exception) {
    this
}
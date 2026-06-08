package br.com.mobapps.fisiocontrol.domain.utils

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

class DateMaskTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val digits = text.text
        val formatted = buildString {
            digits.forEachIndexed { i, c ->
                if (i == 2 || i == 4) append('/')
                append(c)
            }
        }
        val offsetMap = object : OffsetMapping {
            override fun originalToTransformed(offset: Int) = when {
                offset <= 2 -> offset
                offset <= 4 -> offset + 1
                else -> minOf(offset + 2, formatted.length)
            }
            override fun transformedToOriginal(offset: Int) = when {
                offset <= 2 -> offset
                offset <= 5 -> offset - 1
                else -> minOf(offset - 2, digits.length)
            }
        }
        return TransformedText(AnnotatedString(formatted), offsetMap)
    }
}
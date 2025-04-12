package com.walkyriasys.pyme.facturacion.ui.components

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

@Composable
fun MoneyOutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: @Composable (() -> Unit)? = null
) {
    var textFieldValue by remember { mutableStateOf(TextFieldValue(value)) }

    OutlinedTextField(
        value = textFieldValue,
        onValueChange = { newValue ->
            // Filter input to allow only digits and one decimal point
            val newText = newValue.text.replace(Regex("[^0-9.]"), "")
            if (isValidMoneyInput(newText)) {
                textFieldValue = newValue.copy(text = newText)
                onValueChange(newText)
            }
        },
        label = label,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
        visualTransformation = CurrencyVisualTransformation(),
    )
}

// Validate money input (e.g., max 2 decimal places, one decimal point)
private fun isValidMoneyInput(input: String): Boolean {
    val parts = input.split(".")
    return when {
        parts.size > 2 -> false // More than one decimal point
        parts.size == 2 -> parts[1].length <= 2 // Max 2 decimal places
        else -> true
    }
}

// Format input as currency (e.g., 1234.56 -> $1,234.56)
class CurrencyVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val original = text.text
        val formatted = formatAsCurrency(original)

        return TransformedText(
            text = AnnotatedString(formatted),
            offsetMapping = object : OffsetMapping {
                override fun originalToTransformed(offset: Int): Int {
                    if (offset > original.length) return formatted.length
                    val commasBeforeOffset = original.take(offset).count { it == ',' }
                    return (offset + commasBeforeOffset + 1).coerceIn(0, formatted.length) // +1 for "$"
                }

                override fun transformedToOriginal(offset: Int): Int {
                    if (offset > formatted.length) return original.length
                    val commasBeforeOffset = formatted.take(offset).count { it == ',' }
                    return (offset - commasBeforeOffset - 1).coerceIn(0, original.length) // -1 for "$"
                }
            }
        )
    }

    private fun formatAsCurrency(input: String): String {
        val number = input.toDoubleOrNull() ?: return ""
        return String.format("$%,.2f", number)
    }
}
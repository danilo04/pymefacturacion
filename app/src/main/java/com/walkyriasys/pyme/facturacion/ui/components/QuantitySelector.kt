import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun QuantitySelector(
    value: Int,
    onValueChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    minValue: Int = 0,
    maxValue: Int = Int.MAX_VALUE
) = Row(
    modifier = modifier,
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.spacedBy(8.dp)
) {
    // Decrement button
    IconButton(
        onClick = { if (value > minValue) onValueChange(value - 1) },
        enabled = value > minValue
    ) {
        Icon(
            imageVector = Icons.Default.Remove,
            contentDescription = "Decrease quantity"
        )
    }

    // Text field for manual input
    BasicTextField(
        value = value.toString(),
        onValueChange = { newValue ->
            val parsedValue = newValue.toIntOrNull()
            if (parsedValue != null && parsedValue in minValue..maxValue) {
                onValueChange(parsedValue)
            }
        },
        modifier = Modifier.width(60.dp),
        textStyle = TextStyle(
            textAlign = TextAlign.Center,
            fontSize = 16.sp
        ),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
        ),
        singleLine = true,
        decorationBox = { innerTextField ->
            Box(
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(4.dp)
                    )
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                contentAlignment = Alignment.Center
            ) {
                innerTextField()
            }
        }
    )

    // Increment button
    IconButton(
        onClick = { if (value < maxValue) onValueChange(value + 1) },
        enabled = value < maxValue
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "Increase quantity"
        )
    }
}
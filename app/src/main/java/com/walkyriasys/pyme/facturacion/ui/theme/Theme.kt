package com.walkyriasys.pyme.facturacion.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF00A843), // Darker green
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFF388E3C),
    onPrimaryContainer = Color(0xFFB9F6CA),
    secondary = Color(0xFFAB47BC), // Softer purple
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFF4A148C),
    onSecondaryContainer = Color(0xFFE1BEE7),
    tertiary = Color(0xFFCE93D8),
    onTertiary = Color(0xFF000000),
    tertiaryContainer = Color(0xFF6A1B9A),
    onTertiaryContainer = Color(0xFFF3E5F5),
    background = Color(0xFF121212),
    onBackground = Color(0xFFE0E0E0),
    surface = Color(0xFF121212),
    onSurface = Color(0xFFE0E0E0),
    error = Color(0xFFEF5350),
    onError = Color(0xFFFFFFFF)
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF00C853), // Vibrant green
    onPrimary = Color(0xFFFFFFFF), // White
    primaryContainer = Color(0xFFB9F6CA), // Pale green
    onPrimaryContainer = Color(0xFF1B5E20), // Forest green
    secondary = Color(0xFF6A1B9A), // Deep purple
    onSecondary = Color(0xFFFFFFFF), // White
    secondaryContainer = Color(0xFFE1BEE7), // Light lavender
    onSecondaryContainer = Color(0xFF4A148C), // Deep purple
    tertiary = Color(0xFFAB47BC), // Softer purple
    onTertiary = Color(0xFFFFFFFF), // White
    tertiaryContainer = Color(0xFFF3E5F5), // Faint purple
    onTertiaryContainer = Color(0xFF6A1B9A), // Deep purple
    background = Color(0xFFFFFFFF), // White
    onBackground = Color(0xFF212121), // Dark gray
    surface = Color(0xFFF5F5F5), // Light gray
    onSurface = Color(0xFF212121), // Dark gray
    error = Color(0xFFD32F2F), // Red
    onError = Color(0xFFFFFFFF) // White
)

@Composable
fun PymefacturacionTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
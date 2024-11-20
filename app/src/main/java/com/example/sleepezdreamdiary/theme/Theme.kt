package com.example.sleepezdreamdiary.theme

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
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight

// App Theme Colors
private val LightColors = lightColorScheme(
    primary = Color(0xFF9FA8DA),  // Soft lavender
    onPrimary = Color.White,
    primaryContainer = Color(0xFFC5CAE9),  // Lighter lavender
    secondary = Color(0xFFFFAB91),  // Peach
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFFFCCBC),  // Lighter peach
    background = Color(0xFFE3F2FD),  // Light sky blue
    onBackground = Color(0xFF1E1E1E),
    surface = Color(0xFFF8BBD0),  // Light pink
    onSurface = Color(0xFF1E1E1E),
    error = Color(0xFFEF9A9A),
    onError = Color.White
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFF7986CB),  // Soft lavender
    onPrimary = Color.Black,
    primaryContainer = Color(0xFF3949AB),  // Darker lavender
    secondary = Color(0xFFD84315),  // Dark peach
    onSecondary = Color.Black,
    secondaryContainer = Color(0xFF8D6E63),  // Brownish accent
    background = Color(0xFF121212),
    onBackground = Color.White,
    surface = Color(0xFF303F9F),  // Deep blue
    onSurface = Color.White,
    error = Color(0xFFCF6679),
    onError = Color.Black
)

val GreatVibesFontFamily = FontFamily(
    Font(com.example.sleepezdreamdiary.R.font.great_vibes_regular, FontWeight.Normal)
)


@Composable
fun SleepEZDreamDiaryTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColors
        else -> LightColors
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
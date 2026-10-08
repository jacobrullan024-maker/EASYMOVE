package com.example.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val EasyMoveDarkColorScheme = darkColorScheme(
    primary = EmeraldPrimary,
    onPrimary = OnEmerald,
    primaryContainer = EmeraldContainer,
    onPrimaryContainer = EmeraldLight,
    secondary = EmeraldLight,
    onSecondary = OnEmerald,
    secondaryContainer = SlateSurfaceVariant,
    onSecondaryContainer = SlateText,
    tertiary = GoldAccent,
    background = SlateBg,
    onBackground = SlateText,
    surface = SlateSurface,
    onSurface = SlateText,
    surfaceVariant = SlateSurfaceVariant,
    onSurfaceVariant = SlateSubtext,
    outline = SlateBorder,
    error = RedAccent
)

private val EasyMoveLightColorScheme = lightColorScheme(
    primary = EmeraldDark,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFD1FAE5),
    onPrimaryContainer = EmeraldDeepBg,
    secondary = EmeraldPrimary,
    onSecondary = Color.White,
    background = Color(0xFFF8FAFC),
    onBackground = Color(0xFF0F172A),
    surface = Color.White,
    onSurface = Color(0xFF0F172A),
    surfaceVariant = Color(0xFFF1F5F9),
    onSurfaceVariant = Color(0xFF475569),
    outline = Color(0xFFCBD5E1),
    error = RedAccent
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true, // Default to sleek Easy Move Dark Slate & Emerald theme
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) EasyMoveDarkColorScheme else EasyMoveLightColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            if (window != null) {
                window.statusBarColor = colorScheme.background.toArgb()
                window.navigationBarColor = colorScheme.background.toArgb()
                WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
                WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = !darkTheme
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

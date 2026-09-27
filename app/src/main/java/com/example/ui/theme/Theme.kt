package com.example.ui.theme

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

private val AcademicLightColorScheme = lightColorScheme(
    primary = AcademicPrimary,
    onPrimary = AcademicOnPrimary,
    primaryContainer = AcademicPrimaryContainer,
    onPrimaryContainer = AcademicOnPrimaryContainer,
    inversePrimary = AcademicInversePrimary,

    secondary = AcademicSecondary,
    onSecondary = AcademicOnSecondary,
    secondaryContainer = AcademicSecondaryContainer,
    onSecondaryContainer = AcademicOnSecondaryContainer,

    tertiary = AcademicTertiary,
    onTertiary = AcademicOnTertiary,
    tertiaryContainer = AcademicTertiaryContainer,
    onTertiaryContainer = AcademicOnTertiaryContainer,

    background = AcademicBackground,
    onBackground = AcademicOnBackground,

    surface = AcademicSurface,
    onSurface = AcademicOnSurface,
    surfaceVariant = AcademicSurfaceVariant,
    onSurfaceVariant = AcademicOnSurfaceVariant,

    surfaceDim = AcademicSurfaceDim,
    surfaceBright = AcademicSurfaceBright,
    surfaceContainerLowest = AcademicSurfaceContainerLowest,
    surfaceContainerLow = AcademicSurfaceContainerLow,
    surfaceContainer = AcademicSurfaceContainer,
    surfaceContainerHigh = AcademicSurfaceContainerHigh,
    surfaceContainerHighest = AcademicSurfaceContainerHighest,

    outline = AcademicOutline,
    outlineVariant = AcademicOutlineVariant,

    error = AcademicError,
    onError = AcademicOnError,
    errorContainer = AcademicErrorContainer,
    onErrorContainer = AcademicOnErrorContainer,
)

private val AcademicDarkColorScheme = darkColorScheme(
    primary = AcademicInversePrimary,
    onPrimary = AcademicPrimary,
    primaryContainer = AcademicPrimaryContainer,
    onPrimaryContainer = AcademicInversePrimary,

    secondary = AcademicElectricViolet,
    onSecondary = AcademicOnSecondary,
    secondaryContainer = AcademicSecondaryContainer,
    onSecondaryContainer = AcademicOnSecondaryContainer,

    background = AcademicPrimary,
    onBackground = AcademicSurface,

    surface = Color(0xFF141233),
    onSurface = AcademicSurface,
    surfaceVariant = Color(0xFF262354),
    onSurfaceVariant = Color(0xFFC8C5D0),

    outline = AcademicOutline,
    outlineVariant = AcademicOutlineVariant,

    error = CriticalAlertCrimson,
    onError = AcademicOnError
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Enforce Cognitive Academic Minimal brand palette
    content: @Composable () -> Unit,
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> AcademicDarkColorScheme
        else -> AcademicLightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AcademicTypography,
        content = content
    )
}

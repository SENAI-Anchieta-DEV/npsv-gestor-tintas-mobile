package com.senai.npsv_gestor_tintas_mobile.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = PrimaryPurpleBlue,
    onPrimary = NeutralWhite,
    secondary = AccentOrange,
    onSecondary = NeutralWhite,
    tertiary = MediumBlue,
    background = NeutralWhite,
    surface = NeutralWhite,
    onBackground = NeutralBlack,
    onSurface = NeutralBlack,
    error = ErrorRed,
    onError = NeutralWhite
)

@Composable
fun GestorTintasTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        content = content
    )
}
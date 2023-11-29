package com.softwareoverflow.hiit_trainer.ui.theme

import android.content.pm.ActivityInfo
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.softwareoverflow.hiit_trainer.ui.utils.compose.LockScreenOrientation

private val DarkColorScheme = darkColors(
    primary = greenPrimary,
    secondary = pinkSecondary,
    error = Color.Red,
    onPrimary = Color.White,
    onSurface = Color.Black,
    onSecondary = Color.White,
    onBackground = Color.Black,
    background = background,
    surface = background
)

private val LightColorScheme = lightColors(

    primary = greenPrimary,
    secondary = pinkSecondary,
    error = Color.Red,
    onPrimary = Color.White,
    onSurface = Color.Black,
    onSecondary = Color.White,
    onBackground = Color.Black,
    background = background,
    surface = background

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorScheme
    } else {
        LightColorScheme
    }

    LockScreenOrientation(orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)

    CompositionLocalProvider(LocalSpacing provides Spacing()) {
        MaterialTheme(
            colors = colors,
            typography = Typography,
            content = {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    content()
                }
            }
        )
    }
}
package io.groovin.sampleapp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController

private val LightColorPalette = lightColors(
    primary = Primary,
    primaryVariant = PrimaryVariant,
    onPrimary = OnPrimary,
    secondary = Secondary,
    secondaryVariant = SecondaryVariant,
    onSecondary = OnSecondary,
    surface = Surface,
    onSurface = OnSurface,
    background = Surface,
    onBackground = OnSurface
)

private val DarkColorPalette = darkColors(
    primary = DarkPrimary,
    primaryVariant = DarkPrimaryVariant,
    onPrimary = DarkOnPrimary,
    secondary = DarkSecondary,
    secondaryVariant = DarkSecondaryVariant,
    onSecondary = DarkOnSecondary,
    surface = DarkSurface,
    onSurface = DarkOnSurface,
    background = DarkSurface,
    onBackground = DarkOnSurface
)

private val GroovinClockColorPalette = lightColors(
    primary = Color.LightGray,
    primaryVariant = Color.Gray,
    secondary = Color.Blue,
    background = Color.Black,
    surface = Color.Black,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Color.White,
    onSurface = Color.White
)

@Composable
fun GroovinTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    val systemUiController = rememberSystemUiController()
    SideEffect {
        systemUiController.setStatusBarColor(
            color = colors.primary,
            darkIcons = false
        )
        systemUiController.setNavigationBarColor(
            color = colors.primary,
            darkIcons = false
        )
    }

    MaterialTheme(
            colors = colors,
            typography = Typography,
            shapes = Shapes) {
        Surface(
            color = colors.surface,
            contentColor = colors.onSurface
        ) {
            content()
        }
    }
}

@Composable
fun GroovinClockTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colors = GroovinClockColorPalette,
        typography = Typography,
        shapes = Shapes) {
        Surface(
            color = GroovinClockColorPalette.surface,
            contentColor = GroovinClockColorPalette.onPrimary
        ) {
            content()
        }
    }
}
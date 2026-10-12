package org.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val LightBackground = Color(0xFFEAF9E7)
private val LightContainer = Color(0xFFC0E6BA)
private val LightPrimary = Color(0xFF4CA771)
private val LightOnPrimary = Color(0xFF013237)
private val LightText = Color(0xFF013237)
private val LightMuted = Color(0xFF42665A)
private val DarkBackground = Color(0xFF06141B)
private val DarkSurface = Color(0xFF11212D)
private val DarkSurfaceVariant = Color(0xFF253745)
private val DarkPrimary = Color(0xFFA5C0C7)
private val DarkMuted = Color(0xFF9BA8AB)
private val DarkText = Color(0xFFCCD0CF)
private val Outline = Color(0xFF668078)
private val OutlineDark = Color(0xFF71838B)
private val Error = Color(0xFFBA1A1A)
private val ErrorDark = Color(0xFFFFB4AB)

private val LightColors = lightColorScheme(
    primary = LightPrimary,
    onPrimary = Color.White,
    primaryContainer = LightContainer,
    onPrimaryContainer = LightText,
    secondary = LightMuted,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFD9F0D4),
    onSecondaryContainer = LightText,
    background = LightBackground,
    onBackground = LightText,
    surface = Color.White,
    onSurface = LightText,
    surfaceVariant = Color(0xFFD9EED6),
    onSurfaceVariant = LightMuted,
    outline = Outline,
    error = Error,
    onError = Color.White
)

private val DarkColors = darkColorScheme(
    primary = DarkPrimary,
    onPrimary = DarkBackground,
    primaryContainer = DarkSurfaceVariant,
    onPrimaryContainer = DarkText,
    secondary = DarkMuted,
    onSecondary = DarkBackground,
    secondaryContainer = DarkSurface,
    onSecondaryContainer = DarkText,
    background = DarkBackground,
    onBackground = DarkText,
    surface = DarkSurface,
    onSurface = DarkText,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = DarkMuted,
    outline = OutlineDark,
    error = ErrorDark,
    onError = Color(0xFF690005)
)

private val ProfessionalSans = FontFamily.SansSerif

val DoomTypography = Typography(
    displaySmall = TextStyle(
        fontFamily = ProfessionalSans,
        fontWeight = FontWeight.Bold,
        fontSize = 36.sp,
        lineHeight = 43.sp,
        letterSpacing = 0.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = ProfessionalSans,
        fontWeight = FontWeight.Bold,
        fontSize = 34.sp,
        lineHeight = 41.sp
    ),
    titleLarge = TextStyle(
        fontFamily = ProfessionalSans,
        fontWeight = FontWeight.SemiBold,
        fontSize = 22.sp,
        lineHeight = 28.sp
    ),
    titleMedium = TextStyle(
        fontFamily = ProfessionalSans,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        lineHeight = 24.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = ProfessionalSans,
        fontSize = 17.sp,
        lineHeight = 26.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = ProfessionalSans,
        fontSize = 16.sp,
        lineHeight = 24.sp
    ),
    bodySmall = TextStyle(
        fontFamily = ProfessionalSans,
        fontSize = 13.sp,
        lineHeight = 18.sp
    ),
    labelLarge = TextStyle(
        fontFamily = ProfessionalSans,
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp
    ),
    labelMedium = TextStyle(
        fontFamily = ProfessionalSans,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
)

object DoomShapes {
    val Large = 24.dp
    val Medium = 18.dp
    val Small = 12.dp
    val Pill = 50.dp
}

object DoomElevations {
    val None = 0.dp
    val Card = 1.dp
    val Raised = 3.dp
    val Dialog = 6.dp
}

@Composable
fun DoomScrollTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        typography = DoomTypography,
        content = content
    )
}

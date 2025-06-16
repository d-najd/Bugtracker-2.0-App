package io.dnajd.bugtracker.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColors = lightColorScheme(
	primary = bugtracker_theme_light_primary,
	onPrimary = bugtracker_theme_light_onPrimary,
	primaryContainer = bugtracker_theme_light_primaryContainer,
	onPrimaryContainer = bugtracker_theme_light_onPrimaryContainer,
	secondary = bugtracker_theme_light_secondary,
	onSecondary = bugtracker_theme_light_onSecondary,
	secondaryContainer = bugtracker_theme_light_secondaryContainer,
	onSecondaryContainer = bugtracker_theme_light_onSecondaryContainer,
	tertiary = bugtracker_theme_light_tertiary,
	onTertiary = bugtracker_theme_light_onTertiary,
	tertiaryContainer = bugtracker_theme_light_tertiaryContainer,
	onTertiaryContainer = bugtracker_theme_light_onTertiaryContainer,
	error = bugtracker_theme_light_error,
	onError = bugtracker_theme_light_onError,
	errorContainer = bugtracker_theme_light_errorContainer,
	onErrorContainer = bugtracker_theme_light_onErrorContainer,
	outline = bugtracker_theme_light_outline,
	background = bugtracker_theme_light_background,
	onBackground = bugtracker_theme_light_onBackground,
	surface = bugtracker_theme_light_surface,
	onSurface = bugtracker_theme_light_onSurface,
	surfaceVariant = bugtracker_theme_light_surfaceVariant,
	onSurfaceVariant = bugtracker_theme_light_onSurfaceVariant,
	inverseSurface = bugtracker_theme_light_inverseSurface,
	inverseOnSurface = bugtracker_theme_light_inverseOnSurface,
	inversePrimary = bugtracker_theme_light_inversePrimary,
	surfaceTint = bugtracker_theme_light_surfaceTint,
	outlineVariant = bugtracker_theme_light_outlineVariant,
	scrim = bugtracker_theme_light_scrim,
)


private val DarkColors = darkColorScheme(
	primary = bugtracker_theme_dark_primary,
	onPrimary = bugtracker_theme_dark_onPrimary,
	primaryContainer = bugtracker_theme_dark_primaryContainer,
	onPrimaryContainer = bugtracker_theme_dark_onPrimaryContainer,
	secondary = bugtracker_theme_dark_secondary,
	onSecondary = bugtracker_theme_dark_onSecondary,
	secondaryContainer = bugtracker_theme_dark_secondaryContainer,
	onSecondaryContainer = bugtracker_theme_dark_onSecondaryContainer,
	tertiary = bugtracker_theme_dark_tertiary,
	onTertiary = bugtracker_theme_dark_onTertiary,
	tertiaryContainer = bugtracker_theme_dark_tertiaryContainer,
	onTertiaryContainer = bugtracker_theme_dark_onTertiaryContainer,
	error = bugtracker_theme_dark_error,
	onError = bugtracker_theme_dark_onError,
	errorContainer = bugtracker_theme_dark_errorContainer,
	onErrorContainer = bugtracker_theme_dark_onErrorContainer,
	outline = bugtracker_theme_dark_outline,
	background = bugtracker_theme_dark_background,
	onBackground = bugtracker_theme_dark_onBackground,
	surface = bugtracker_theme_dark_surface,
	onSurface = bugtracker_theme_dark_onSurface,
	surfaceVariant = bugtracker_theme_dark_surfaceVariant,
	onSurfaceVariant = bugtracker_theme_dark_onSurfaceVariant,
	inverseSurface = bugtracker_theme_dark_inverseSurface,
	inverseOnSurface = bugtracker_theme_dark_inverseOnSurface,
	inversePrimary = bugtracker_theme_dark_inversePrimary,
	surfaceTint = bugtracker_theme_dark_surfaceTint,
	outlineVariant = bugtracker_theme_dark_outlineVariant,
	scrim = bugtracker_theme_dark_scrim,
)

@Composable
fun BugtrackerTheme(content: @Composable () -> Unit) {    // val context = LocalContext.current
	// val layoutDirection = LocalLayoutDirection.current

	val colors = if (!isSystemInDarkTheme()) {
		LightColors
	} else {
		DarkColors
	}

	MaterialTheme(
		colorScheme = colors,
		content = content,
	)
}

/*
@Composable
fun BugtrackerTheme(
  useDarkTheme: Boolean = isSystemInDarkTheme(),
  content: @Composable() () -> Unit
) {
  val colors = if (!useDarkTheme) {
    LightColors
  } else {
    DarkColors
  }

  MaterialTheme(
    colorScheme = colors,
    content = content
  )
}
 */
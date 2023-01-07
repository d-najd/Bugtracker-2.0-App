package io.dnajd.presentation.util

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import com.bluelinelabs.conductor.Router

val LocalRouter: ProvidableCompositionLocal<Router?> = staticCompositionLocalOf { null }

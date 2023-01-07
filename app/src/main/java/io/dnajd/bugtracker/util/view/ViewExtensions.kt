package io.dnajd.bugtracker.util.view

import android.view.ViewGroup
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionContext
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.core.view.children
import androidx.core.view.descendants
import io.dnajd.bugtracker.theme.BugtrackerTheme

inline fun ComposeView.setComposeContent(crossinline content: @Composable () -> Unit) {
    consumeWindowInsets = false
    setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
    setContent {
        BugtrackerTheme {
            CompositionLocalProvider(
                LocalTextStyle provides MaterialTheme.typography.bodySmall,
                LocalContentColor provides MaterialTheme.colorScheme.onBackground,
            ) {
                content()
            }
        }
    }
}

@Suppress("unused")
inline fun ComponentActivity.setComposeContent(
    parent: CompositionContext? = null,
    crossinline content: @Composable () -> Unit,
) {
    setContent(parent) {
        BugtrackerTheme {
            CompositionLocalProvider(
                LocalTextStyle provides MaterialTheme.typography.bodySmall,
                LocalContentColor provides MaterialTheme.colorScheme.onBackground,
            ) {
                content()
            }
        }
    }
}

@Suppress("unused")
inline fun <reified T> ViewGroup.findChild(): T? {
    return children.find { it is T } as? T
}


/**
 * Returns this ViewGroup's first descendant of specified class
 */
@Suppress("unused")
inline fun <reified T> ViewGroup.findDescendant(): T? {
    return descendants.find { it is T } as? T
}
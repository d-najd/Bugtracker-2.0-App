package io.dnajd.presentation.util

import android.graphics.Rect
import android.view.ViewTreeObserver
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalView

@Composable
fun rememberKeyboardState(): State<Boolean> {
	val view = LocalView.current
	val isKeyboardOpen = remember { mutableStateOf(false) }

	DisposableEffect(view) {
		val listener = ViewTreeObserver.OnGlobalLayoutListener {
			val rect = Rect().apply { view.getWindowVisibleDisplayFrame(this) }
			val screenHeight = view.rootView.height
			val keypadHeight = screenHeight - rect.bottom

			// 15% threshold to account for navigation bars
			isKeyboardOpen.value = keypadHeight > screenHeight * 0.15
		}

		view.viewTreeObserver.addOnGlobalLayoutListener(listener)
		onDispose {
			view.viewTreeObserver.removeOnGlobalLayoutListener(listener)
		}
	}

	return isKeyboardOpen
}

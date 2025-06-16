@file:Suppress("unused")

package io.dnajd.util

import android.content.Context
import android.util.DisplayMetrics
import android.widget.Toast
import androidx.annotation.StringRes

/** gets app context *//*
fun mAppContext(): Context {
    return Injekt.get<ContextHolder>().appContext
}
 */

/**
 * This method converts dp unit to equivalent pixels, depending on device density.
 *
 * @param dp A value in dp (density independent pixels) unit. Which we need to convert into pixels
 * @return A float value to represent px equivalent to dp depending on device density
 */
fun Context.convertDpToPixel(dp: Float): Float {
	return dp * (resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
}

/**
 * This method converts device specific pixels to density independent pixels.
 *
 * @param px A value in px (pixels) unit. Which we need to convert into db
 * @return A float value to represent dp equivalent to px value
 */
fun Context.convertPixelsToDp(px: Float): Float {
	return px / (resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
}

/**
 * Display a toast in this context.
 *
 * @param resource the text resource.
 * @param duration the duration of the toast. Defaults to short.
 */
fun Context.toast(
	@StringRes resource: Int,
	duration: Int = Toast.LENGTH_SHORT,
	block: (Toast) -> Unit = {},
): Toast {
	return toast(
		getString(resource),
		duration,
		block
	)
}

/**
 * Display a toast in this context.
 *
 * @param text the text to display.
 * @param duration the duration of the toast. Defaults to short.
 */
fun Context.toast(
	text: String?,
	duration: Int = Toast.LENGTH_SHORT,
	block: (Toast) -> Unit = {},
): Toast {
	return Toast
		.makeText(
			applicationContext,
			text.orEmpty(),
			duration
		)
		.also {
			block(it)
			it.show()
		}
}

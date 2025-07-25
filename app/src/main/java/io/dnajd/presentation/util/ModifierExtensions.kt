package io.dnajd.presentation.util

import android.annotation.SuppressLint
import androidx.compose.material3.DividerDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp

@SuppressLint(
	"ModifierFactoryUnreferencedReceiver",
	"ComposableModifierFactory",
)
@Composable
fun Modifier.bottomBorder(
	strokeWidth: Dp = DividerDefaults.Thickness,
	color: Color = DividerDefaults.color,
) = composed(
	factory = {
		val density = LocalDensity.current

		val strokeWidthPx = density.run { strokeWidth.toPx() }

		Modifier.drawBehind {
			val width = size.width
			val height = size.height - strokeWidthPx / 2

			drawLine(
				color = color,
				start = Offset(
					x = 0f,
					y = height,
				),
				end = Offset(
					x = width,
					y = height,
				),
				strokeWidth = strokeWidthPx,
			)
		}
	},
)

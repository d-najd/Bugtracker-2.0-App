package io.dnajd.util

import java.math.RoundingMode

fun Double.includeDecimals(
	decimals: Int,
	roundingMode: RoundingMode = RoundingMode.HALF_UP,
): Double = toBigDecimal()
	.setScale(
		decimals,
		roundingMode,
	)
	.toDouble()

fun Float.includeDecimals(
	decimals: Int,
	roundingMode: RoundingMode = RoundingMode.HALF_UP,
): Float = toBigDecimal()
	.setScale(
		decimals,
		roundingMode,
	)
	.toFloat()

fun Float.conditionalIncludeDecimals(
	condition: Boolean,
	ifTrue: Int = 1,
	ifFalse: Int = 0,
	roundingModeTrue: RoundingMode = RoundingMode.HALF_UP,
	roundModeFalse: RoundingMode = RoundingMode.HALF_UP,
): Float = when (condition) {
	true -> this.includeDecimals(
		ifTrue,
		roundingModeTrue,
	)

	false -> this.includeDecimals(
		ifFalse,
		roundModeFalse,
	)
}

fun Double.conditionalIncludeDecimals(
	condition: Boolean,
	ifTrue: Int = 1,
	ifFalse: Int = 0,
	roundingModeTrue: RoundingMode = RoundingMode.HALF_UP,
	roundModeFalse: RoundingMode = RoundingMode.HALF_UP,
): Double = when (condition) {
	true -> this.includeDecimals(
		ifTrue,
		roundingModeTrue,
	)

	false -> this.includeDecimals(
		ifFalse,
		roundModeFalse,
	)
}

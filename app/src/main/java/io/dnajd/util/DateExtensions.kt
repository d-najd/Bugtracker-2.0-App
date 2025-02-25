package io.dnajd.util

import java.util.Calendar
import java.util.Date

/**
 * Converts given date to the very beginning of the day down to the last millisecond
 *
 * example { 2022-09-06 14:46:38:541 -> 2022-09-06 00:00:00:000 }
 */
fun Date.toStartOfDay(): Date {
	val date = Calendar.getInstance()
	date.time = this
	date.set(Calendar.HOUR_OF_DAY, 0)
	date.set(Calendar.MINUTE, 0)
	date.set(Calendar.SECOND, 0)
	date.set(Calendar.MILLISECOND, 0)

	return date.time
}

/**
 * Converts given date to the very end of the day down to the last millisecond
 *
 * example { 2022-09-06 14:46:38:541 -> 2022-09-06 23:59:59:999 }
 */
fun Date.toEndOfDay(): Date {
	val date = Calendar.getInstance()
	date.time = this
	date.set(Calendar.HOUR_OF_DAY, 23)
	date.set(Calendar.MINUTE, 59)
	date.set(Calendar.SECOND, 59)
	date.set(Calendar.MILLISECOND, 999)

	return date.time
}


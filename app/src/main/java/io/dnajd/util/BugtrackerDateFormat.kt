package io.dnajd.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.res.pluralStringResource
import io.dnajd.bugtracker.R
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.math.roundToInt

class BugtrackerDateFormat {
	companion object {

		/** for displaying data */
		fun defaultDateFormat(): SimpleDateFormat = SimpleDateFormat(
			DEFAULT_APP_DATE_FORMAT,
			Locale.getDefault(),
		)

		/** for parsing data from the api */
		fun defaultRequestDateFormat(): SimpleDateFormat = SimpleDateFormat(
			DEFAULT_API_DATE_FORMAT,
			Locale.getDefault(),
		)

		/**
		 * Generates string from given seconds,
		 *
		 * The generated string will contain either: hour/s, minute/s or second/s, if the return value is
		 * less than 10 it will be rounded to 1 decimal, else it will be rounded to Int, examples:
		 * "5.2 hours", "24 minutes", "1 second"
		 *
		 * @param timeSeconds given seconds, must be { seconds >= 0 }
		 * @throws IllegalArgumentException if { [timeSeconds] < 0 }
		 * @return string representation of given [timeSeconds] consisting of either: hours, minutes or
		 * seconds containing at most 1 decimal space
		 */
		@Composable
		@OptIn(ExperimentalComposeUiApi::class)
		fun generateStringFromTimeWithDecimal(timeSeconds: Long): String {
			return when (timeSeconds) {
				in 3600..Long.MAX_VALUE -> {
					val hours = timeSeconds / 3600f
					"${hours.conditionalIncludeDecimals(condition = hours < 10)} " + pluralStringResource(
						id = R.plurals.hours,
						count = hours.roundToInt(),
					)
				}

				in 60..3599 -> {
					val minutes = timeSeconds / 60f
					"${minutes.conditionalIncludeDecimals(condition = minutes < 10)} " + pluralStringResource(
						id = R.plurals.minutes,
						count = (minutes).roundToInt(),
					)
				}

				in 0..59 -> {
					"$timeSeconds " + pluralStringResource(
						id = R.plurals.seconds,
						count = timeSeconds.toInt(),
					)
				}

				else -> {
					throw IllegalArgumentException("Given seconds must be { seconds >= 0 }")
				}
			}
		}

		@Composable
		@OptIn(ExperimentalComposeUiApi::class)
		fun generateStringFromTime(timeSeconds: Long): String {
			return when (timeSeconds) {
				in 86400..Long.MAX_VALUE -> {
					val days = timeSeconds / 86400f
					"${days.roundToInt()} ${
						pluralStringResource(
							id = R.plurals.days,
							count = days.roundToInt(),
						)
					}"
				}

				in 3600..86399 -> {
					val hours = timeSeconds / 3600f
					"${hours.roundToInt()} ${
						pluralStringResource(
							id = R.plurals.hours,
							count = hours.roundToInt(),
						)
					}"
				}

				in 60..3599 -> {
					val minutes = timeSeconds / 60f
					"${minutes.roundToInt()} ${
						pluralStringResource(
							id = R.plurals.minutes,
							count = minutes.roundToInt(),
						)
					}"
				}

				in 0..59 -> {
					"$timeSeconds ${
						pluralStringResource(
							id = R.plurals.seconds,
							count = timeSeconds.toInt(),
						)
					}"
				}

				else -> {
					throw IllegalArgumentException("Given seconds must be { seconds >= 0 }")
				}
			}
		}

		private const val DEFAULT_APP_DATE_FORMAT = "d MMM yyyy"
		private const val DEFAULT_API_DATE_FORMAT = "yyyy-MM-d HH:mm:ss"
	}
}

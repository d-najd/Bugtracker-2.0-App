package io.dnajd.util

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import java.lang.reflect.Type
import java.text.DateFormat
import java.text.ParseException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.TimeZone

/**
 * https://stackoverflow.com/questions/26044881/java-date-to-utc-using-gson/26046107#26046107
 *
 * TODO [Date] is outdated, use [LocalDateTime] instead and instead pf [DateFormat] use [DateTimeFormatter],
 * the refactoring needs to be done project wide and in backend as well, they have easier modification and
 * conversion, have better support for time zones, are thread safe.
 *
 * https://stackoverflow.com/questions/28730136/should-i-use-java-util-date-or-switch-to-java-time-localdate
 * https://www.baeldung.com/migrating-to-java-8-date-time-api
 */
class GsonUTCDateAdapter(private val dateFormat: DateFormat) : JsonSerializer<Date>, JsonDeserializer<Date> {
	init {
		dateFormat.timeZone = TimeZone.getTimeZone("UTC")
	}

	@Synchronized
	override fun serialize(
		src: Date,
		typeOfSrc: Type,
		context: JsonSerializationContext,
	): JsonElement {
		return JsonPrimitive(dateFormat.format(src))
	}

	@Synchronized
	override fun deserialize(
		json: JsonElement,
		typeOfT: Type,
		context: JsonDeserializationContext,
	): Date {
		return try {
			dateFormat.parse(json.asString)
		} catch (e: ParseException) {
			throw JsonParseException(e)
		}
	}
}

package io.dnajd.domain.preference.service

import io.dnajd.domain.preference.model.Preference

interface PreferenceStore {

	fun getString(
		key: String,
		defaultValue: String = "",
	): Preference<String>

	fun getLong(
		key: String,
		defaultValue: Long = 0,
	): Preference<Long>

	fun getInt(
		key: String,
		defaultValue: Int = 0,
	): Preference<Int>

	fun getFloat(
		key: String,
		defaultValue: Float = 0f,
	): Preference<Float>

	fun getBoolean(
		key: String,
		defaultValue: Boolean = false,
	): Preference<Boolean>

	fun getStringSet(
		key: String,
		defaultValue: Set<String> = emptySet(),
	): Preference<Set<String>>

	fun <T> getObject(
		key: String,
		defaultValue: T,
		serializer: (T) -> String,
		deserializer: (String) -> T,
	): Preference<T>

	fun getAll(): Map<String, *>
}

inline fun <reified T : Enum<T>> PreferenceStore.getEnum(
	key: String,
	defaultValue: T,
): Preference<T> {
	return getObject(
		key = key,
		defaultValue = defaultValue,
		serializer = { it.name },
		deserializer = {
			try {
				enumValueOf(it)
			} catch (e: IllegalArgumentException) {
				defaultValue
			}
		},
	)
}
package io.dnajd.data.preference

import android.content.Context
import android.content.SharedPreferences
import io.dnajd.domain.preference.model.AndroidPreference
import io.dnajd.domain.preference.model.Preference
import io.dnajd.domain.preference.service.PreferenceStore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow

class AndroidPreferenceStore(
	context: Context,
	private val sharedPreferences: SharedPreferences = context.getSharedPreferences(
		"MyPreferences",
		Context.MODE_PRIVATE,
	),
) : PreferenceStore {

	private val keyFlow = sharedPreferences.keyFlow

	override fun getString(
		key: String,
		defaultValue: String,
	): Preference<String> {
		return AndroidPreference.StringPrimitive(
			sharedPreferences,
			keyFlow,
			key,
			defaultValue,
		)
	}

	override fun getLong(
		key: String,
		defaultValue: Long,
	): Preference<Long> {
		return AndroidPreference.LongPrimitive(
			sharedPreferences,
			keyFlow,
			key,
			defaultValue,
		)
	}

	override fun getInt(
		key: String,
		defaultValue: Int,
	): Preference<Int> {
		return AndroidPreference.IntPrimitive(
			sharedPreferences,
			keyFlow,
			key,
			defaultValue,
		)
	}

	override fun getFloat(
		key: String,
		defaultValue: Float,
	): Preference<Float> {
		return AndroidPreference.FloatPrimitive(
			sharedPreferences,
			keyFlow,
			key,
			defaultValue,
		)
	}

	override fun getBoolean(
		key: String,
		defaultValue: Boolean,
	): Preference<Boolean> {
		return AndroidPreference.BooleanPrimitive(
			sharedPreferences,
			keyFlow,
			key,
			defaultValue,
		)
	}

	override fun getStringSet(
		key: String,
		defaultValue: Set<String>,
	): Preference<Set<String>> {
		return AndroidPreference.StringSetPrimitive(
			sharedPreferences,
			keyFlow,
			key,
			defaultValue,
		)
	}

	override fun <T> getObject(
		key: String,
		defaultValue: T,
		serializer: (T) -> String,
		deserializer: (String) -> T,
	): Preference<T> {
		return AndroidPreference.Object(
			preferences = sharedPreferences,
			keyFlow = keyFlow,
			key = key,
			defaultValue = defaultValue,
			serializer = serializer,
			deserializer = deserializer,
		)
	}

	override fun getAll(): Map<String, *> {
		return sharedPreferences.all ?: emptyMap<String, Any>()
	}
}

private val SharedPreferences.keyFlow
	get() = callbackFlow {
		val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, key: String? ->
			trySend(
				key,
			)
		}
		registerOnSharedPreferenceChangeListener(listener)
		awaitClose {
			unregisterOnSharedPreferenceChangeListener(listener)
		}
	}

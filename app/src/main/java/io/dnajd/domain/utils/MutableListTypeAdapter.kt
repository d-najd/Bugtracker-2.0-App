package io.dnajd.domain.utils

import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.google.gson.TypeAdapterFactory
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import java.io.IOException


class MutableListTypeAdapter<T>(private val delegate: TypeAdapter<MutableList<T>>?) :
	TypeAdapter<MutableList<T>>() {

	@Throws(IOException::class)
	override fun write(
		out: JsonWriter?,
		value: MutableList<T>?,
	) {
		delegate?.write(out, value)
	}

	@Throws(IOException::class)
	override fun read(reader: JsonReader?): MutableList<T> {
		if (reader?.peek() === JsonToken.NULL) {
			reader.nextNull()
			return ArrayList()
		}
		return delegate?.read(reader) ?: ArrayList()
	}
}

internal class MutableListTypeAdapterFactory : TypeAdapterFactory {
	override fun <T> create(
		gson: Gson,
		type: TypeToken<T>,
	): TypeAdapter<T>? {
		val delegate = gson.getDelegateAdapter(this, type)
		val rawType = type.rawType as? Class<T>
		return when (rawType) {
			List::class.java -> {
				val mutableListDelegate = delegate as? TypeAdapter<MutableList<Any>>
				MutableListTypeAdapter(mutableListDelegate) as? TypeAdapter<T>
			}

			else -> {
				null
			}
		}
	}
}
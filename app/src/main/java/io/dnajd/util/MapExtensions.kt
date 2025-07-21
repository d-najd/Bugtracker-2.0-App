package io.dnajd.util

/**
 * Puts the value in the map if it no value with given condition exists or if value with condition
 * exists replaces it instead
 * @throws IllegalArgumentException if more than 1 key matches the given condition
 */
fun <K, V> MutableMap<K, V>.putIfNoneOrReplaceIf(
	key: K,
	value: V,
	condition: (K, V) -> Boolean,
): MutableMap<K, V> {
	val matchingKeys = this.filter { (k, v) ->
		condition(
			k,
			v
		)
	}.keys

	if (matchingKeys.size > 1) {
		throw IllegalArgumentException("More than 1 keys match the given condition")
	}

	if (matchingKeys.isEmpty()) {

		// No match exists, insert new
		this[key] = value
	} else {

		// Remove all matching entries and insert new one
		matchingKeys.forEach { this.remove(it) }
		this[key] = value
	}
	return this
}

/*
fun <K, V> MutableMap<K, V>.replaceIfCondition(
	key: K,
	value: V,
	condition: (V) -> Boolean,
) {


	val current = this[key]
	if (current == null || condition(current)) {
		this[key] = value
	}
}
 */
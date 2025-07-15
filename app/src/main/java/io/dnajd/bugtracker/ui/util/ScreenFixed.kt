package io.dnajd.bugtracker.ui.util

import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import kotlin.random.Random

/**
 * Voyager has a bug with screens not being disposed properly if the same screen is disposed and
 * re-added in the same frame, interface does not seem viable here since we need an unique value each
 * time a screen is initialized, composition seems error prone as well.
 *
 * Altering the key multiple times within the same screen causes it to be recreated during each
 * modification.
 *
 * [https://github.com/adrielcafe/voyager/issues/437]
 */
abstract class ScreenFixed(
	private val screenKey: ScreenKey = "ScreenKey-${Random(System.currentTimeMillis()).nextLong()}",
) : Screen {
	override val key: ScreenKey
		get() = screenKey
}
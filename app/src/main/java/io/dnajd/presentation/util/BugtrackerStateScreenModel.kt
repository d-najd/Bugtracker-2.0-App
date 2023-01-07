package io.dnajd.presentation.util

import android.content.Context
import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import io.dnajd.bugtracker.util.view.ContextHolder
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.get

/**
 * TODO according to google passing context to screen-model is a bad idea, should find alternative
 */
open class BugtrackerStateScreenModel<S> (context: Context, initialState: S):
	StateScreenModel<S>(initialState = initialState) {
	init {
		Injekt.get<ContextHolder>().composeContext = context
		Injekt.get<ContextHolder>().composeCoroutineScope = coroutineScope
	}
}

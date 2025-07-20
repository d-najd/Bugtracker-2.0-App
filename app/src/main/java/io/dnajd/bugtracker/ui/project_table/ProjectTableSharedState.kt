package io.dnajd.bugtracker.ui.project_table

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow

@Deprecated("") object ProjectTableSharedState {
	private val _events: Channel<Event> = Channel(Int.MAX_VALUE)
	val events: Flow<Event> = _events.receiveAsFlow()

	/**
	 * Use this from external screen to notify that one or more tables have been changed and that
	 * they need to be re-fetched
	 */
	suspend fun notifyTableOrTaskAltered() {
		_events.send(Event.TableOrTaskAltered)
	}

	sealed class Event {
		data object TableOrTaskAltered : Event()
	}
}


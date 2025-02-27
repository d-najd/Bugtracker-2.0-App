package io.dnajd.bugtracker.ui.project

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import cafe.adriel.voyager.core.model.coroutineScope
import io.dnajd.bugtracker.R
import io.dnajd.domain.project.model.Project
import io.dnajd.domain.project.service.ProjectRepository
import io.dnajd.presentation.util.BugtrackerStateScreenModel
import io.dnajd.util.launchIO
import io.dnajd.util.launchUI
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import logcat.LogPriority
import logcat.logcat
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.get

class ProjectScreenModel(
	context: Context,

	private val projectRepository: ProjectRepository = Injekt.get(),
) : BugtrackerStateScreenModel<ProjectScreenState>(context, ProjectScreenState.Loading) {

	private val _events: Channel<ProjectEvent> = Channel(Int.MAX_VALUE)
	val events: Flow<ProjectEvent> = _events.receiveAsFlow()

	private var busy = false

	init {
		requestProjects("user1")
	}

	private fun requestProjects(username: String) {
		coroutineScope.launchIO {
			projectRepository.getAll(username).onSuccess { result ->
				mutableState.update {
					ProjectScreenState.Success(
						projects = result.data,
					)
				}
			}.onFailure {
				it.printStackTrace()
				_events.send(ProjectEvent.FailedToRetrieveProjects)
			}
		}
	}

	/**
	 * Must be called from state [ProjectScreenState.Success] or when busy
	 * @throws IllegalStateException if called from state other than [ProjectScreenState.Success] and not busy
	 */
	fun createProject(project: Project) {
		if (busy) return
		if (mutableState.value !is ProjectScreenState.Success) {
			throw IllegalStateException("Must be called from state ${ProjectScreenState.Success::class.simpleName} but was called with state ${mutableState.value}")
		}
		val successState = mutableState.value as ProjectScreenState.Success

		busy = true
		coroutineScope.launchIO {
			projectRepository.create(project).onSuccess { result ->
				mutableState.update {
					val projects = successState.projects.toMutableList()
					projects.add(result)
					successState.copy(projects = projects)
				}
				dismissDialog()

				busy = false
			}.onFailure {
				it.printStackTrace()
				_events.send(ProjectEvent.FailedToCreateProject)

				busy = false
			}
		}
	}

	/**
	 * Must be called from state [ProjectScreenState.Success] or when busy
	 * @throws IllegalStateException if called from state other than [ProjectScreenState.Success] and not busy
	 */
	fun showDialog(dialog: ProjectDialog) {
		if (busy) return
		if (mutableState.value !is ProjectScreenState.Success) {
			throw IllegalStateException("Must be called from state ${ProjectScreenState.Success::class.simpleName} but was called with state ${mutableState.value}")
		}
		val successState = mutableState.value as ProjectScreenState.Success

		busy = true
		when (dialog) {
			is ProjectDialog.CreateProject -> {
				coroutineScope.launchUI {
					mutableState.update {
						successState.copy(
							dialog = dialog,
						)
					}

					busy = false
				}
			}
		}
	}

	fun dismissDialog() {
		if (busy) return
		if (mutableState.value !is ProjectScreenState.Success) {
			logcat(LogPriority.WARN) { "Dismiss dialog called from state ${mutableState.value}, this is probably a mistake" }
		}
		busy = true
		mutableState.update {
			when (it) {
				is ProjectScreenState.Success -> it.copy(dialog = null)
				else -> it
			}
		}
		busy = false
	}
}

sealed class ProjectScreenState {

	@Immutable
	object Loading : ProjectScreenState()

	@Immutable
	data class Success(
		val projects: List<Project>,
		val dialog: ProjectDialog? = null,
	) : ProjectScreenState()
}

sealed class ProjectDialog {
	data class CreateProject(val title: String = "") : ProjectDialog()
}

sealed class ProjectEvent {
	sealed class LocalizedMessage(@StringRes val stringRes: Int) : ProjectEvent()

	object FailedToRetrieveProjects : LocalizedMessage(R.string.failed_to_retrieve_projects)
	object FailedToCreateProject : LocalizedMessage(R.string.failed_to_create_project)
}
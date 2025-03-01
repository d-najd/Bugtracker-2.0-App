package io.dnajd.bugtracker.ui.project

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import io.dnajd.bugtracker.R
import io.dnajd.domain.project.model.Project
import io.dnajd.domain.project.service.ProjectRepository
import io.dnajd.util.launchIO
import io.dnajd.util.launchIONoQueue
import io.dnajd.util.launchUINoQueue
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.sync.Mutex
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.get

class ProjectScreenModel(
	private val projectRepository: ProjectRepository = Injekt.get(),
) : StateScreenModel<ProjectScreenState>(ProjectScreenState.Loading) {

	private val _events: Channel<ProjectEvent> = Channel(Int.MAX_VALUE)
	val events: Flow<ProjectEvent> = _events.receiveAsFlow()
	private var mutex = Mutex()

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
	 * @throws AssertionError if called from state other than [ProjectScreenState.Success] and not busy
	 */
	fun createProject(project: Project) {
		mutex.launchIONoQueue(coroutineScope) {
			val successState = mutableState.value as ProjectScreenState.Success

			projectRepository.create(project).onSuccess { result ->
				mutableState.update {
					val projects = successState.projects.toMutableList()
					projects.add(result)
					successState.copy(projects = projects)
				}
				dismissDialog()
			}.onFailure {
				it.printStackTrace()
				_events.send(ProjectEvent.FailedToCreateProject)
			}
		}
	}

	/**
	 * Must be called from state [ProjectScreenState.Success] or when busy
	 * @throws AssertionError if called from state other than [ProjectScreenState.Success] and not busy
	 */
	fun showDialog(dialog: ProjectDialog) {
		mutex.launchUINoQueue(coroutineScope) {
			val successState = mutableState.value as ProjectScreenState.Success

			when (dialog) {
				is ProjectDialog.CreateProject -> {
					mutableState.update {
						successState.copy(
							dialog = dialog,
						)
					}
				}
			}
		}
	}

	/**
	 * Must be called from state [ProjectScreenState.Success] or when busy
	 * @throws AssertionError if called from state other than [ProjectScreenState.Success] and not busy
	 */
	fun dismissDialog() {
		mutex.launchUINoQueue(coroutineScope) {
			assert(mutableState.value is ProjectScreenState.Success)

			mutableState.update {
				when (it) {
					is ProjectScreenState.Success -> it.copy(dialog = null)
					else -> it
				}
			}
		}
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

	object FailedToRetrieveProjects : LocalizedMessage(R.string.error_failed_to_retrieve_projects)
	object FailedToCreateProject : LocalizedMessage(R.string.error_failed_to_create_project)
}
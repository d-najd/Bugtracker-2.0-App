package io.dnajd.bugtracker.ui.project

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import io.dnajd.bugtracker.R
import io.dnajd.domain.project.model.Project
import io.dnajd.domain.project.service.ProjectRepository
import io.dnajd.domain.utils.onFailureWithStackTrace
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

	private val mutex = Mutex()

	init {
		requestProjects()
	}

	private fun requestProjects() {
		coroutineScope.launchIO {
			val projects = projectRepository
				.getAll()
				.onFailureWithStackTrace {
					_events.send(ProjectEvent.FailedToRetrieveProjects)
					return@launchIO
				}
				.getOrThrow()

			mutableState.update { ProjectScreenState.Success(projects = projects.data) }
		}
	}

	fun createProject(project: Project) {
		mutex.launchIONoQueue(coroutineScope) {
			val successState = mutableState.value as ProjectScreenState.Success

			val projects = projectRepository
				.createProject(project)
				.onFailureWithStackTrace {
					_events.send(ProjectEvent.FailedToCreateProject)
					return@launchIONoQueue
				}
				.getOrThrow()

			val projectsMutable = successState.projects.toMutableList()
			projectsMutable.add(projects)

			mutableState.update { successState.copy(projects = projectsMutable) }
			dismissDialog()
		}
	}

	fun showDialog(dialog: ProjectDialog) {
		mutex.launchUINoQueue(coroutineScope) {
			val successState = mutableState.value as ProjectScreenState.Success

			when (dialog) {
				is ProjectDialog.CreateProject -> {
					mutableState.update { successState.copy(dialog = dialog) }
				}
			}
		}
	}

	fun dismissDialog() {
		mutex.launchUINoQueue(coroutineScope) {
			val successState = mutableState.value as ProjectScreenState.Success

			mutableState.update { successState.copy(dialog = null) }
		}
	}
}

sealed class ProjectScreenState {

	@Immutable data object Loading : ProjectScreenState()

	@Immutable data class Success(
		val projects: List<Project>,
		val dialog: ProjectDialog? = null,
	) : ProjectScreenState()
}

sealed class ProjectDialog {
	data class CreateProject(val title: String = "") : ProjectDialog()
}

sealed class ProjectEvent {
	sealed class LocalizedMessage(@StringRes val stringRes: Int) : ProjectEvent()

	data object FailedToRetrieveProjects :
		LocalizedMessage(R.string.error_failed_to_retrieve_projects)

	data object FailedToCreateProject : LocalizedMessage(R.string.error_failed_to_create_project)
}
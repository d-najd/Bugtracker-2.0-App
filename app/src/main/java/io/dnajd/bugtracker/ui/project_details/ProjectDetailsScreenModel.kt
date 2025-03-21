package io.dnajd.bugtracker.ui.project_details

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
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.sync.Mutex
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.get

class ProjectDetailsScreenModel(
	projectId: Long,

	private val projectRepository: ProjectRepository = Injekt.get(),
) : StateScreenModel<ProjectDetailsScreenState>(ProjectDetailsScreenState.Loading) {
	private val _events: Channel<ProjectDetailsEvent> = Channel(Int.MAX_VALUE)
	val events: Flow<ProjectDetailsEvent> = _events.receiveAsFlow()

	private val mutex = Mutex()

	init {
		coroutineScope.launchIO {
			val project = projectRepository.getById(projectId).onFailureWithStackTrace {
				_events.send(ProjectDetailsEvent.FailedToRetrieveProjectData)
				return@launchIO
			}.getOrThrow()

			mutableState.update { ProjectDetailsScreenState.Success(project = project) }
		}
	}

	fun deleteProject() {
		mutex.launchIONoQueue(coroutineScope) {
			val successState = mutableState.value as ProjectDetailsScreenState.Success

			projectRepository.deleteById(successState.project.id).onSuccess {
				_events.send(ProjectDetailsEvent.DeleteProject)
			}.onFailureWithStackTrace {
				_events.send(ProjectDetailsEvent.FailedToDeleteProject)
			}
		}
	}

	fun renameProject(title: String) {
		mutex.launchIONoQueue(coroutineScope) {
			val successState = mutableState.value as ProjectDetailsScreenState.Success
			val renamedProject = successState.project.copy(title = title)

			val persistedProject =
				projectRepository.updateProject(renamedProject).onFailureWithStackTrace {
					_events.send(ProjectDetailsEvent.FailedToRenameProject)
					return@launchIONoQueue
				}.getOrThrow()

			mutableState.update {
				successState.copy(project = persistedProject)
			}
		}
	}
}

sealed class ProjectDetailsScreenState {
	@Immutable
	data object Loading : ProjectDetailsScreenState()

	@Immutable
	data class Success(
		val project: Project,
	) : ProjectDetailsScreenState()
}

sealed class ProjectDetailsEvent {
	sealed class LocalizedMessage(@StringRes val stringRes: Int) : ProjectDetailsEvent()

	data object FailedToRetrieveProjectData :
		LocalizedMessage(R.string.error_failed_to_retrieve_project_data)

	data object FailedToDeleteProject :
		LocalizedMessage(R.string.error_failed_to_delete_project)

	data object FailedToRenameProject :
		LocalizedMessage(R.string.error_failed_to_rename_project)

	data object DeleteProject : ProjectDetailsEvent()
}
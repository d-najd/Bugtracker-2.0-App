package io.dnajd.bugtracker.ui.project_details

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import io.dnajd.bugtracker.R
import io.dnajd.data.project.repository.ProjectRepository
import io.dnajd.domain.project.model.Project
import io.dnajd.domain.project.service.ProjectApiService
import io.dnajd.domain.utils.onFailureWithStackTrace
import io.dnajd.util.launchIONoQueue
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.sync.Mutex
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.get

class ProjectDetailsScreenModel(
	projectId: Long,

	private val projectApiService: ProjectApiService = Injekt.get(),
) : StateScreenModel<ProjectDetailsScreenState>(ProjectDetailsScreenState.Loading(projectId)) {
	private val _events: MutableSharedFlow<ProjectDetailsEvent> = MutableSharedFlow()
	val events: SharedFlow<ProjectDetailsEvent> = _events.asSharedFlow()

	private val mutex = Mutex()

	init {
		mutex.launchIONoQueue(coroutineScope) {
			ProjectRepository
				.fetchAllIfStale()
				.onFailureWithStackTrace {
					_events.emit(ProjectDetailsEvent.FailedToRetrieveProjectData)
					return@launchIONoQueue
				}

			mutableState.update { ProjectDetailsScreenState.Success(projectId) }
		}
	}

	fun deleteProject() {
		mutex.launchIONoQueue(coroutineScope) {
			val successState = mutableState.value as ProjectDetailsScreenState.Success

			projectApiService
				.deleteById(successState.projectId)
				.onFailureWithStackTrace {
					_events.emit(ProjectDetailsEvent.FailedToDeleteProject)
					return@launchIONoQueue
				}
				.onSuccess {
					_events.emit(ProjectDetailsEvent.DeleteProject(projectId = successState.projectId))
				}
		}
	}

	fun renameProject(title: String) {
		mutex.launchIONoQueue(coroutineScope) {
			val successState = mutableState.value as ProjectDetailsScreenState.Success

			val projectToRename = ProjectRepository.dataById(successState.projectId)!!
			val renamedProject = projectToRename.copy(title = title)

			val persistedProject = projectApiService
				.updateProject(renamedProject)
				.onFailureWithStackTrace {
					_events.emit(ProjectDetailsEvent.FailedToRenameProject)
					return@launchIONoQueue
				}
				.getOrThrow()

			val combinedData = ProjectRepository.combineForUpdate(persistedProject)
			ProjectRepository.update(combinedData)
		}
	}
}

sealed class ProjectDetailsScreenState(
	projectId: Long,
) {
	@Immutable data class Loading(val projectId: Long) : ProjectDetailsScreenState(projectId)

	@Immutable data class Success(
		val projectId: Long,
	) : ProjectDetailsScreenState(projectId) {
		@Composable
		fun project(): Project = ProjectRepository.dataKeyCollectedById(projectId)!!
	}
}

sealed class ProjectDetailsEvent {
	sealed class LocalizedMessage(@StringRes val stringRes: Int) : ProjectDetailsEvent()

	data object FailedToRetrieveProjectData : LocalizedMessage(R.string.error_failed_to_retrieve_project_data)

	data object FailedToDeleteProject : LocalizedMessage(R.string.error_failed_to_delete_project)

	data object FailedToRenameProject : LocalizedMessage(R.string.error_failed_to_rename_project)

	data class DeleteProject(val projectId: Long) : ProjectDetailsEvent()
}

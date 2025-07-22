package io.dnajd.bugtracker.ui.project_details

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import io.dnajd.bugtracker.R
import io.dnajd.data.project.repository.ProjectRepository
import io.dnajd.domain.project.service.ProjectApiService
import io.dnajd.domain.utils.onFailureWithStackTrace
import io.dnajd.util.launchIONoQueue
import io.dnajd.util.putOrReplaceIf
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.sync.Mutex
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.get
import java.util.Date

class ProjectDetailsScreenModel(
	projectId: Long,

	private val projectApiService: ProjectApiService = Injekt.get(),
) : StateScreenModel<ProjectDetailsScreenState>(ProjectDetailsScreenState.Loading(projectId)) {
	private val _events: Channel<ProjectDetailsEvent> = Channel(Int.MAX_VALUE)
	val events: Flow<ProjectDetailsEvent> = _events.receiveAsFlow()

	private val mutex = Mutex()

	init {
		mutex.launchIONoQueue(coroutineScope) {
			ProjectRepository
				.fetchAllIfStale()
				.onFailureWithStackTrace {
					_events.send(ProjectDetailsEvent.FailedToRetrieveProjectData)
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
					_events.send(ProjectDetailsEvent.FailedToDeleteProject)
					return@launchIONoQueue
				}
				.onSuccess {
					_events.send(ProjectDetailsEvent.DeleteProject(projectId = successState.projectId))
				}
		}
	}

	fun renameProject(title: String) {
		mutex.launchIONoQueue(coroutineScope) {
			val successState = mutableState.value as ProjectDetailsScreenState.Success
			val projects = ProjectRepository.data()
			val projectToRename = projects.keys.find { it.id == successState.projectId }!!
			val renamedProject = projectToRename.copy(title = title)

			val persistedProject = projectApiService
				.updateProject(renamedProject)
				.onFailureWithStackTrace {
					_events.send(ProjectDetailsEvent.FailedToRenameProject)
					return@launchIONoQueue
				}
				.getOrThrow()

			val projectsModified = projects
				.toMutableMap()
				.putOrReplaceIf(
					persistedProject,
					Date(),
				) { k, _ ->
					k.id == persistedProject.id
				}

			ProjectRepository.update(projectsModified)
		}
	}
}

sealed class ProjectDetailsScreenState(
	projectId: Long,
) {
	@Immutable data class Loading(val projectId: Long) : ProjectDetailsScreenState(projectId)

	@Immutable data class Success(
		val projectId: Long,
	) : ProjectDetailsScreenState(projectId)
}

sealed class ProjectDetailsEvent {
	sealed class LocalizedMessage(@StringRes val stringRes: Int) : ProjectDetailsEvent()

	data object FailedToRetrieveProjectData :
		LocalizedMessage(R.string.error_failed_to_retrieve_project_data)

	data object FailedToDeleteProject : LocalizedMessage(R.string.error_failed_to_delete_project)

	data object FailedToRenameProject : LocalizedMessage(R.string.error_failed_to_rename_project)

	data class DeleteProject(val projectId: Long) : ProjectDetailsEvent()
}
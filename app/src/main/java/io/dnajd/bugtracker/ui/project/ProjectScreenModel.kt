package io.dnajd.bugtracker.ui.project

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import io.dnajd.bugtracker.R
import io.dnajd.data.project.repository.ProjectRepository
import io.dnajd.data.project_icon.repository.ProjectIconRepository
import io.dnajd.domain.base.onFailureWithStackTrace
import io.dnajd.domain.project.model.Project
import io.dnajd.domain.project.service.ProjectApiService
import io.dnajd.domain.project_icon.model.ProjectIcon
import io.dnajd.domain.project_icon.service.ProjectIconApiService
import io.dnajd.util.launchIONoQueue
import io.dnajd.util.launchUINoQueue
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.sync.Mutex
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.get

class ProjectScreenModel(
	private val projectApiService: ProjectApiService = Injekt.get(),
	private val projectIconApiService: ProjectIconApiService = Injekt.get(),
) : StateScreenModel<ProjectScreenState>(ProjectScreenState.Loading) {
	private val _events: MutableSharedFlow<ProjectEvent> = MutableSharedFlow()
	val events: SharedFlow<ProjectEvent> = _events.asSharedFlow()

	private val mutex = Mutex()

	init {
		mutex.launchIONoQueue(coroutineScope) {
			val projects = ProjectRepository
				.fetchAllIfStale()
				.onFailureWithStackTrace {
					_events.emit(ProjectEvent.FailedToRetrieveProjects)
					return@launchIONoQueue
				}
				.getOrThrow()

			val projectIds = projects.map { it.id }

			ProjectIconRepository.fetchByProjectIdsIfStale(*projectIds.toLongArray())
				.onFailureWithStackTrace {
					_events.emit(ProjectEvent.FailedToRetrieveProjects)
					return@launchIONoQueue
				}

			mutableState.update { ProjectScreenState.Success() }
		}
	}

	fun createProject(project: Project) = mutex.launchIONoQueue(coroutineScope) {
		val persistedProject = projectApiService
			.createProject(project)
			.onFailureWithStackTrace {
				_events.emit(ProjectEvent.FailedToCreateProject)
				return@launchIONoQueue
			}
			.getOrThrow()

		val combinedData = ProjectRepository.combineForUpdate(persistedProject)
		ProjectRepository.update(combinedData)

		dismissDialog()
	}

	fun showDialog(dialog: ProjectDialog) = mutex.launchUINoQueue(coroutineScope) {
		val successState = mutableState.value as ProjectScreenState.Success

		when (dialog) {
			is ProjectDialog.CreateProject -> {
				mutableState.update { successState.copy(dialog = dialog) }
			}
		}
	}

	fun dismissDialog() = mutex.launchUINoQueue(coroutineScope) {
		val successState = mutableState.value as ProjectScreenState.Success

		mutableState.update { successState.copy(dialog = null) }
	}
}

sealed class ProjectScreenState {

	@Immutable
	data object Loading : ProjectScreenState()

	@Immutable
	data class Success(
		val dialog: ProjectDialog? = null,
	) : ProjectScreenState() {
		@Composable
		fun projects(): Set<Project> = ProjectRepository.dataKeysCollected()

		@Composable
		fun projectIcons(): Set<ProjectIcon> = ProjectIconRepository.dataKeysCollected()
	}
}

sealed class ProjectDialog {
	data class CreateProject(val title: String = "") : ProjectDialog()
}

sealed class ProjectEvent {
	sealed class LocalizedMessage(@StringRes val stringRes: Int) : ProjectEvent()

	data object FailedToRetrieveProjects : LocalizedMessage(R.string.error_failed_to_retrieve_projects)

	data object FailedToCreateProject : LocalizedMessage(R.string.error_failed_to_create_project)
}

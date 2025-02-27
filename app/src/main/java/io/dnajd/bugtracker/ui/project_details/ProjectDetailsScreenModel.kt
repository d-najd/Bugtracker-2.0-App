package io.dnajd.bugtracker.ui.project_details

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import cafe.adriel.voyager.core.model.coroutineScope
import io.dnajd.bugtracker.R
import io.dnajd.domain.project.model.Project
import io.dnajd.domain.project.service.ProjectRepository
import io.dnajd.presentation.util.BugtrackerStateScreenModel
import io.dnajd.util.launchIO
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.get

class ProjectDetailsScreenModel(
	context: Context,
	projectId: Long,

	private val projectRepository: ProjectRepository = Injekt.get(),
) : BugtrackerStateScreenModel<ProjectDetailsScreenState>(
	context,
	ProjectDetailsScreenState.Loading
) {
	private val _events: Channel<ProjectDetailsEvent> = Channel(Int.MAX_VALUE)
	val events: Flow<ProjectDetailsEvent> = _events.receiveAsFlow()

	private var isBusy = false

	init {
		coroutineScope.launchIO {
			projectRepository.get(projectId).onSuccess { result ->
				mutableState.update {
					ProjectDetailsScreenState.Success(
						project = result,
					)
				}
			}.onFailure {
				it.printStackTrace()
				_events.send(ProjectDetailsEvent.FailedToRetrieveProjectData)
			}
		}
	}

	/**
	 * Must be called from state [ProjectDetailsScreenState.Success] or when busy
	 * @throws IllegalStateException if called from state other than [ProjectDetailsScreenState.Success] and not busy
	 */
	fun deleteProject() {
		if (isBusy) return
		if (mutableState.value !is ProjectDetailsScreenState.Success) {
			throw IllegalStateException("Must be called from state ${ProjectDetailsScreenState.Success::class.simpleName} but was called with state ${mutableState.value}")
		}
		val successState = mutableState.value as ProjectDetailsScreenState.Success
		isBusy = true

		coroutineScope.launchIO {
			projectRepository.delete(successState.project.id).onSuccess {
				_events.send(ProjectDetailsEvent.DeleteProject)
				isBusy = false
			}.onFailure {
				_events.send(ProjectDetailsEvent.FailedToDeleteProject)
				isBusy = false
			}
		}
	}

	/**
	 * Must be called from state [ProjectDetailsScreenState.Success] or when busy
	 * @throws IllegalStateException if called from state other than [ProjectDetailsScreenState.Success] and not busy
	 */
	fun renameProject(title: String) {
		if (isBusy) return
		if (mutableState.value !is ProjectDetailsScreenState.Success) {
			throw IllegalStateException("Must be called from state ${ProjectDetailsScreenState.Success::class.simpleName} but was called with state ${mutableState.value}")
		}
		val successState = mutableState.value as ProjectDetailsScreenState.Success
		isBusy = true

		coroutineScope.launchIO {
			projectRepository.updateNoBody(
				id = successState.project.id,
				title = title
			).onSuccess {
				val renamedProject = successState.project.copy(
					title = title,
				)
				mutableState.update {
					successState.copy(
						project = renamedProject,
					)
				}
				isBusy = false
			}.onFailure {
				it.printStackTrace()
				_events.send(ProjectDetailsEvent.FailedToRenameProject)
				isBusy = false
			}
		}
	}
}

sealed class ProjectDetailsScreenState {
	@Immutable
	object Loading : ProjectDetailsScreenState()

	@Immutable
	data class Success(
		val project: Project,
	) : ProjectDetailsScreenState()
}

sealed class ProjectDetailsEvent {
	sealed class LocalizedMessage(@StringRes val stringRes: Int) : ProjectDetailsEvent()

	object FailedToRetrieveProjectData :
		LocalizedMessage(R.string.error_failed_to_retrieve_project_data)

	object FailedToDeleteProject :
		LocalizedMessage(R.string.error_failed_to_delete_project)

	object FailedToRenameProject :
		LocalizedMessage(R.string.error_failed_to_rename_project)

	object DeleteProject : ProjectDetailsEvent()
}
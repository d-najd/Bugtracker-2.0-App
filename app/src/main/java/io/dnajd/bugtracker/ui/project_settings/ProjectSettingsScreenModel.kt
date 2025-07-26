package io.dnajd.bugtracker.ui.project_settings

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

class ProjectSettingsScreenModel(
	val projectId: Long,

	private val projectApiService: ProjectApiService = Injekt.get(),
) : StateScreenModel<ProjectSettingsScreenState>(ProjectSettingsScreenState.Loading(projectId)) {
	private val _events: MutableSharedFlow<ProjectSettingsEvent> = MutableSharedFlow()
	val events: SharedFlow<ProjectSettingsEvent> = _events.asSharedFlow()

	private val mutex = Mutex()

	init {
		mutex.launchIONoQueue(coroutineScope) {
			ProjectRepository
				.fetchOneIfStale(
					projectId,
				)
				.onFailureWithStackTrace {
					_events.emit(ProjectSettingsEvent.FailedToRetrieveProjectData)
					return@launchIONoQueue
				}

			mutableState.update { ProjectSettingsScreenState.Success(projectId) }
		}
	}
}

sealed class ProjectSettingsEvent {
	sealed class LocalizedMessage(@StringRes val stringRes: Int) : ProjectSettingsEvent()

	data object FailedToRetrieveProjectData : LocalizedMessage(R.string.error_failed_to_retrieve_project_data)
}

sealed class ProjectSettingsScreenState(open val projectId: Long) {

	@Immutable data class Loading(override val projectId: Long) : ProjectSettingsScreenState(projectId)

	@Immutable data class Success(
		override val projectId: Long,
	) : ProjectSettingsScreenState(projectId) {
		@Composable
		fun project(): Project = ProjectRepository.dataKeyCollectedById(projectId)!!
	}
}

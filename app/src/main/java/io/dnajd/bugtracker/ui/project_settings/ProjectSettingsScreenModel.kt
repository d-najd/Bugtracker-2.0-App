package io.dnajd.bugtracker.ui.project_settings

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import io.dnajd.bugtracker.R
import io.dnajd.domain.project.model.Project
import io.dnajd.domain.project.service.ProjectRepository
import io.dnajd.domain.utils.onFailureWithStackTrace
import io.dnajd.util.launchIO
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.get

class ProjectSettingsScreenModel(
	val projectId: Long,

	private val projectRepository: ProjectRepository = Injekt.get(),
) : StateScreenModel<ProjectSettingsScreenState>(ProjectSettingsScreenState.Loading) {
	private val _events: Channel<ProjectSettingsEvent> = Channel(Int.MAX_VALUE)
	val events: Flow<ProjectSettingsEvent> = _events.receiveAsFlow()

	init {
		coroutineScope.launchIO {
			val project = projectRepository.get(projectId).onFailureWithStackTrace {
				_events.send(ProjectSettingsEvent.FailedToRetrieveProjectData)
				return@launchIO
			}.getOrThrow()

			mutableState.update { ProjectSettingsScreenState.Success(project = project) }
		}
	}
}

sealed class ProjectSettingsEvent {
	sealed class LocalizedMessage(@StringRes val stringRes: Int) : ProjectSettingsEvent()

	object FailedToRetrieveProjectData :
		LocalizedMessage(R.string.error_failed_to_retrieve_project_data)
}

sealed class ProjectSettingsScreenState {

	@Immutable
	object Loading : ProjectSettingsScreenState()

	@Immutable
	data class Success(
		val project: Project,
	) : ProjectSettingsScreenState()
}
package io.dnajd.bugtracker.ui.project_settings

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

class ProjectSettingsScreenModel(
	context: Context,
	val projectId: Long,

	private val projectRepository: ProjectRepository = Injekt.get(),
) : BugtrackerStateScreenModel<ProjectSettingsScreenState>(
	context,
	ProjectSettingsScreenState.Loading
) {
	private val _events: Channel<ProjectSettingsEvent> = Channel(Int.MAX_VALUE)
	val events: Flow<ProjectSettingsEvent> = _events.receiveAsFlow()

	init {
		coroutineScope.launchIO {

			projectRepository.get(projectId).onSuccess { result ->
				mutableState.update {
					ProjectSettingsScreenState.Success(
						project = result,
					)
				}
				_events.send(ProjectSettingsEvent.ProjectModified(result))
			}.onFailure {
				it.printStackTrace()
				_events.send(ProjectSettingsEvent.InvalidProjectId)
			}
		}
	}
}

sealed class ProjectSettingsEvent {
	sealed class LocalizedMessage(@StringRes val stringRes: Int) : ProjectSettingsEvent()

	object InvalidProjectId : LocalizedMessage(R.string.error_invalid_project_id)
	data class ProjectModified(val project: Project) : ProjectSettingsEvent()
}

sealed class ProjectSettingsScreenState {

	@Immutable
	object Loading : ProjectSettingsScreenState()

	@Immutable
	data class Success(
		val project: Project,
	) : ProjectSettingsScreenState()
}
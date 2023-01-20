package io.dnajd.bugtracker.ui.project_settings

import android.content.Context
import androidx.compose.runtime.Immutable
import cafe.adriel.voyager.core.model.coroutineScope
import io.dnajd.domain.project.model.Project
import io.dnajd.domain.project_table.interactor.*
import io.dnajd.domain.project_table.model.ProjectTable
import io.dnajd.presentation.util.BugtrackerStateScreenModel
import io.dnajd.util.launchUI
import kotlinx.coroutines.flow.update

class ProjectSettingsScreenModel(
    context: Context,
    project: Project,
) : BugtrackerStateScreenModel<ProjectSettingsScreenState>(context,
    ProjectSettingsScreenState.Loading(project)
) {
    fun showDialog(dialog: ProjectSettingsDialog) {
        @Suppress("UNUSED_EXPRESSION")
        when (dialog) {
            else -> {
                coroutineScope.launchUI {
                    mutableState.update {
                        (mutableState.value as ProjectSettingsScreenState.Success).copy(
                            dialog = dialog,
                        )
                    }
                }
            }
        }
    }

    fun dismissDialog() {
        mutableState.update {
            when (it) {
                is ProjectSettingsScreenState.Success -> it.copy(dialog = null)
                else -> it
            }
        }
    }
}

sealed class ProjectSettingsDialog {
    /*
    data class CreateSettings(val title: String = "") : ProjectSettingsDialog()
    data class RenameSettings(val id: Long, val title: String = "") : ProjectSettingsDialog()
     */
}

sealed class ProjectSettingsScreenState(
    open val project: Project,
) {
    
    @Immutable
    data class Loading(
        override val project: Project,
    ) : ProjectSettingsScreenState(project)

    /**
     * TODO find a way to get rid of taskMoved, using events does not work properly
     */
    @Immutable
    data class Success(
        override val project: Project,
        val dialog: ProjectSettingsDialog? = null,
    ): ProjectSettingsScreenState(project)

}
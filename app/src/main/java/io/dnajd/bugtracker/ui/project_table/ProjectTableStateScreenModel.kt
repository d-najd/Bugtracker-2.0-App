package io.dnajd.bugtracker.ui.project_table

import android.content.Context
import androidx.compose.runtime.Immutable
import cafe.adriel.voyager.core.model.coroutineScope
import io.dnajd.domain.project_table.interactor.GetProjectTables
import io.dnajd.domain.project_table.model.ProjectTable
import io.dnajd.presentation.util.BugtrackerStateScreenModel
import io.dnajd.util.launchIO
import kotlinx.coroutines.flow.update
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.get

class ProjectTableScreenModel(
    context: Context,
    projectId: Long,

    private val getProjectTables: GetProjectTables = Injekt.get(),
) : BugtrackerStateScreenModel<ProjectTableScreenState>(context, ProjectTableScreenState.Loading) {

    init {
        requestProjectTables(projectId)
    }

    private fun requestProjectTables(projectId: Long) {
        coroutineScope.launchIO {
            val projectTables = getProjectTables.await(projectId)
            mutableState.update {
                ProjectTableScreenState.Success(
                    projectTables = projectTables.sortedBy { it.position },
                )
            }
        }
    }

}

sealed class ProjectTableScreenState {
    
    @Immutable
    object Loading : ProjectTableScreenState()

    @Immutable
    data class Success(
        val projectTables: List<ProjectTable>,
    ): ProjectTableScreenState()

}
package io.dnajd.presentation.project_user_management

import androidx.activity.compose.BackHandler
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import io.dnajd.bugtracker.ui.base.ProjectTableSelectedTab
import io.dnajd.bugtracker.ui.project_settings.ProjectSettingsScreenState
import io.dnajd.bugtracker.ui.project_user_management.ProjectUserManagementScreenState
import io.dnajd.domain.user_authority.model.UserAuthority
import io.dnajd.presentation.components.LoadingScreen
import io.dnajd.presentation.project_settings.components.ProjectSettingsContent
import io.dnajd.presentation.project_settings.components.ProjectSettingsTopAppBar
import io.dnajd.presentation.project_user_management.components.ProjectUserManagementContent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectUserManagementScreenContent(
    state: ProjectUserManagementScreenState.Success,
    onBackClicked: () -> Unit,

    onInvertAuthorityClicked: (UserAuthority) -> Unit,
) {
    Scaffold { contentPadding ->
        BackHandler { onBackClicked() }

        ProjectUserManagementContent(
            state = state,
            contentPadding = contentPadding,
            onInvertAuthorityClicked = onInvertAuthorityClicked,
        )
    }
}

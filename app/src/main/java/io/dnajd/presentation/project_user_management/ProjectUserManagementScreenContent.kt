package io.dnajd.presentation.project_user_management

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.dnajd.bugtracker.R
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
    onAddUserToProjectClicked: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { onBackClicked() }) {
                        Icon(
                            modifier = Modifier.padding(horizontal = 8.dp),
                            imageVector = Icons.Rounded.ArrowBack,
                            contentDescription = ""
                        )
                    }
                },
                title = {
                    Text(
                        text = stringResource(R.string.field_user_management),
                    )
                },
                actions = {
                    IconButton(onClick = { onAddUserToProjectClicked() }) {
                        Icon(
                            modifier = Modifier
                                .padding(horizontal = 6.dp),
                            imageVector = Icons.Rounded.Add,
                            contentDescription = ""
                        )
                    }
                }
            )
        }
    ) { contentPadding ->
        BackHandler { onBackClicked() }

        ProjectUserManagementContent(
            state = state,
            contentPadding = contentPadding,
            onInvertAuthorityClicked = onInvertAuthorityClicked,
        )
    }
}

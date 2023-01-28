package io.dnajd.bugtracker.ui.project_user_management

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.currentOrThrow
import io.dnajd.presentation.components.LoadingScreen
import io.dnajd.presentation.project_user_management.ProjectUserManagementScreenContent
import io.dnajd.presentation.project_user_management.dialogs.AddUserToProjectDialog
import io.dnajd.presentation.project_user_management.dialogs.ConfirmLastAuthorityRemovalDialog
import io.dnajd.presentation.util.LocalRouter

class ProjectUserManagementScreen(
    private val projectId: Long
) : Screen {
    @Composable
    override fun Content() {
        // val navigator = LocalNavigator.currentOrThrow
        val router = LocalRouter.currentOrThrow
        val context = LocalContext.current
        val screenModel = rememberScreenModel { ProjectUserManagementScreenModel(context, projectId) }

        val state by screenModel.state.collectAsState()

        if (state is ProjectUserManagementScreenState.Loading) {
            LoadingScreen()
            return
        }

        val successState = state as ProjectUserManagementScreenState.Success

        ProjectUserManagementScreenContent(
            state = successState,
            onBackClicked = router::popCurrentController,
            onInvertAuthorityClicked = screenModel::invertAuthority,
            onAddUserToProjectClicked = { screenModel.showDialog(ProjectUserManagementDialog.AddUserToProject) }
        )

        when(val dialog = successState.dialog) {
            null -> {}
            is ProjectUserManagementDialog.ConfirmLastAuthorityRemoval -> {
                ConfirmLastAuthorityRemovalDialog(
                    onDismissRequest = screenModel::dismissDialog,
                    onConfirmClicked = {
                        screenModel.deleteAuthority(
                            userAuthority = dialog.userAuthority,
                            agreed = true
                        )
                        screenModel.dismissDialog()
                    }
                )
            }
            is ProjectUserManagementDialog.AddUserToProject -> {
                AddUserToProjectDialog(
                    onDismissRequest = screenModel::dismissDialog,
                    projectId = successState.projectId,
                    onConfirmClicked = {

                    }
                )
            }
        }
    }
}

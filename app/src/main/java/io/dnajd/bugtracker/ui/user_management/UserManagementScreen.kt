package io.dnajd.bugtracker.ui.user_management

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import io.dnajd.presentation.components.LoadingScreen
import io.dnajd.presentation.user_management.ProjectUserManagementScreenContent
import io.dnajd.presentation.user_management.dialogs.AddUserToProjectDialog
import io.dnajd.presentation.user_management.dialogs.ConfirmLastAuthorityRemovalDialog
import io.dnajd.util.toast
import kotlinx.coroutines.flow.collectLatest

class UserManagementScreen(
	private val projectId: Long,
) : Screen {
	@Composable
	override fun Content() {
		val navigator = LocalNavigator.currentOrThrow
		val context = LocalContext.current
		val screenModel = rememberScreenModel { UserManagementScreenModel(projectId) }

		LaunchedEffect(screenModel.events) {
			screenModel.events.collectLatest { event ->
				when (event) {
					is UserManagementEvent.FailedToRetrieveUserAuthorities -> {
						context.toast(event.stringRes)
						navigator.pop()
					}

					is UserManagementEvent.LocalizedMessage -> {
						context.toast(event.stringRes)
					}
				}
			}
		}

		val state by screenModel.state.collectAsState()

		if (state is UserManagementScreenState.Loading) {
			LoadingScreen()
			return
		}

		val successState = state as UserManagementScreenState.Success

		ProjectUserManagementScreenContent(
			state = successState,
			onBackClicked = navigator::pop,
			onInvertAuthorityClicked = { screenModel.modifyAuthority(it) },
			onAddUserToProjectClicked = { screenModel.showDialog(UserManagementDialog.AddUserToProject) })

		when (val dialog = successState.dialog) {
			null -> {}
			is UserManagementDialog.ConfirmLastAuthorityRemoval -> {
				ConfirmLastAuthorityRemovalDialog(
					onDismissRequest = screenModel::dismissDialog,
					onConfirmClicked = {
						screenModel.deleteAuthority(
							userAuthority = dialog.userAuthority,
							agreed = true
						)
						screenModel.dismissDialog()
					})
			}

			is UserManagementDialog.AddUserToProject -> {
				AddUserToProjectDialog(
					onDismissRequest = screenModel::dismissDialog,
					projectId = successState.projectId,
					onConfirmClicked = {
						screenModel.modifyAuthority(
							it,
							true
						)
					})
			}
		}
	}
}

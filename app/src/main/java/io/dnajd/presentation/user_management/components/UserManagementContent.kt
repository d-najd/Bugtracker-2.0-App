package io.dnajd.presentation.user_management.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import io.dnajd.bugtracker.ui.user_management.UserManagementScreenState
import io.dnajd.domain.user_authority.model.UserAuthority

@Composable
fun ProjectUserManagementContent(
	state: UserManagementScreenState.Success,
	contentPadding: PaddingValues,

	onInvertAuthorityClicked: (UserAuthority) -> Unit,
) {
	Column(
		modifier = Modifier
			.padding(contentPadding)
			.verticalScroll(rememberScrollState()),
	) {
		var usersWithAuthorities by remember { mutableStateOf(state.getUsersWithAuthorities()) }
		LaunchedEffect(state.authorities) {
			usersWithAuthorities = state.getUsersWithAuthorities()
		}

		for (userWithAuthorities in usersWithAuthorities) {
			ProjectUserManagementItemContent(
				state = state,
				userWithAuthorities = userWithAuthorities,
				onInvertAuthorityClicked = onInvertAuthorityClicked,
			)
		}
	}
}

package io.dnajd.presentation.user_management.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import io.dnajd.bugtracker.ui.user_management.UserManagementScreenState
import io.dnajd.domain.user_authority.model.UserAuthority
import io.dnajd.domain.user_authority.model.UserAuthorityType
import io.dnajd.presentation.components.BugtrackerIconPairField

@Composable
fun ProjectUserManagementAuthoritiesContent(
	state: UserManagementScreenState.Success,
	modifier: Modifier = Modifier,
	username: String,
	authorities: List<UserAuthority>,
	selfAuthorities: List<UserAuthority>,
	onInvertAuthorityClicked: (UserAuthority) -> Unit,
) {
	Column(
		modifier = modifier
	) {
		val isSelfOwner = selfAuthorities.any { it.authority == UserAuthorityType.project_owner }
		val isSelfManager =
			selfAuthorities.any { it.authority == UserAuthorityType.project_manage_users } || isSelfOwner
		val isCurrentUserSelf = username == state.selfUsername

		val containsOwnerAuthority = authorities.any {
			it.authority == UserAuthorityType.project_owner
		}
		for (authorityType in UserAuthorityType.entries) {
			var enabled = when (authorityType) {
				UserAuthorityType.project_view,
				UserAuthorityType.project_create,
				UserAuthorityType.project_edit,
				UserAuthorityType.project_delete,
					-> {

					// managing of basic roles is available to admins if they have said role or owner
					isSelfOwner || (isSelfManager && selfAuthorities.any { it.authority == authorityType })
				}

				UserAuthorityType.project_manage_users -> {

					// Adding admins is only allowed to owners
					isSelfOwner
				}

				UserAuthorityType.project_owner -> false
			}

			// Don't allow managing of self
			enabled = enabled && !isCurrentUserSelf

			val checked =
				authorities.any { it.authority == authorityType } || containsOwnerAuthority
			val description = stringResource(authorityType.descriptionResId)
			var localModifier = Modifier
				.fillMaxWidth()
				.padding(vertical = 4.dp)
			if (enabled) {
				localModifier = localModifier.clickable {
					onInvertAuthorityClicked(
						UserAuthority(
							username = username,
							projectId = state.projectId,
							authority = authorityType,
						)
					)
				}
			}

			Card(
				modifier = localModifier,
				shape = RoundedCornerShape(4.dp),
			) {
				BugtrackerIconPairField(
					modifier = Modifier.padding(
						horizontal = 8.dp,
						vertical = 10.dp
					),
					titleContent = {
						Text(
							modifier = Modifier.padding(
								start = 2.dp,
								bottom = 6.dp
							),
							text = stringResource(authorityType.titleResId),
							fontWeight = FontWeight.SemiBold,
						)
					},
					iconContent = {
						Checkbox(
							enabled = enabled,
							modifier = Modifier.size(24.dp),
							checked = checked,
							onCheckedChange = null,
						)
					},
					textContent = {
						Text(
							modifier = Modifier.padding(start = 2.dp),
							text = description
						)
					},
				)
			}
		}
	}
}

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
import io.dnajd.domain.user_authority.model.UserAuthority
import io.dnajd.domain.user_authority.model.UserAuthorityType
import io.dnajd.presentation.components.BugtrackerIconPairField

@Composable
fun ProjectUserManagementAuthoritiesContent(
	modifier: Modifier = Modifier,
	projectId: Long,
	username: String,
	authorities: List<UserAuthority>,
	onInvertAuthorityClicked: (UserAuthority) -> Unit,
) {
	Column(
		modifier = modifier
	) {
		val containsOwnerAuthority = authorities.find {
			it.authority == UserAuthorityType.project_owner
		}
		for (authorityType in UserAuthorityType.values()) {
			val enabled =
				(containsOwnerAuthority == null || authorityType == UserAuthorityType.project_owner)
			val checked =
				authorities.find { it.authority == authorityType } != null || containsOwnerAuthority != null
			val description = stringResource(authorityType.descriptionResId)
			var localModifier = Modifier
				.fillMaxWidth()
				.padding(vertical = 4.dp)
			if (enabled) {
				localModifier = localModifier.clickable {
					onInvertAuthorityClicked(
						UserAuthority(
							username = username,
							projectId = projectId,
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
						horizontal = 8.dp, vertical = 10.dp
					),
					titleContent = {
						Text(
							modifier = Modifier.padding(
								start = 2.dp, bottom = 6.dp
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
							modifier = Modifier.padding(start = 2.dp), text = description
						)
					},
				)
			}
		}
	}
}

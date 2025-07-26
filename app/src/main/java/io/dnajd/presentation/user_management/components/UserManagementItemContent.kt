package io.dnajd.presentation.user_management.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.dnajd.bugtracker.ui.user_management.UserManagementScreenState
import io.dnajd.domain.user_authority.model.UserAuthority
import io.dnajd.presentation.components.BugtrackerExpandableMenu
import io.dnajd.presentation.components.BugtrackerIconPairField

@Composable
fun ProjectUserManagementItemContent(
	state: UserManagementScreenState.Success,
	userWithAuthorities: Map.Entry<String, List<UserAuthority>>,
	selfAuthorities: List<UserAuthority>,

	onInvertAuthorityClicked: (UserAuthority) -> Unit,
) {
	var expanded by remember { mutableStateOf(false) }
	BugtrackerExpandableMenu(
		modifier = Modifier
			.padding(
				horizontal = 12.dp,
				vertical = 4.dp,
			)
			.fillMaxWidth(),
		onClick = { expanded = !expanded },
		expanded = expanded,
		displayMainDivider = false,
		menuContent = {
			BugtrackerIconPairField(
				modifier = Modifier
					.fillMaxWidth()
					.height(42.dp),
				iconContent = {
					Icon(
						modifier = Modifier.size(28.dp),
						imageVector = Icons.Default.AccountCircle,
						contentDescription = "",
					)
				},
				textContent = {
					Text(
						modifier = Modifier.padding(start = 2.dp),
						text = userWithAuthorities.key,
						color = MaterialTheme.colorScheme.onSurface,
						fontSize = (13.75).sp,
						fontFamily = FontFamily.SansSerif,
						fontWeight = FontWeight.Light,
					)

					/**
					 * TODO
					 * there should be multiple roles,
					 * Viewer - only view
					 * Editor - view along with atleast one of create edit delete
					 * Manager - view, manage users along with atleast one of create edit delete
					 * Owner - The owner of the project
					 */
					Text(
						modifier = Modifier.padding(start = 2.dp),
						text = "ROLE-CUSTOM",
						color = MaterialTheme.colorScheme.onSurface.copy(0.5f),
						fontSize = 12.sp,
						fontWeight = FontWeight.ExtraLight,
						fontFamily = FontFamily.SansSerif,
					)
				},
			)
		},
		expandableContent = {

			ProjectUserManagementAuthoritiesContent(
				state = state,
				authorities = userWithAuthorities.value,
				username = userWithAuthorities.key,
				selfAuthorities = selfAuthorities,
				onInvertAuthorityClicked = onInvertAuthorityClicked,
			)
		},
	)
}

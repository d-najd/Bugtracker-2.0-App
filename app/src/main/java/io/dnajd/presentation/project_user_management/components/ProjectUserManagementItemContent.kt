package io.dnajd.presentation.project_user_management.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.dnajd.domain.user_authority.model.UserAuthority
import io.dnajd.domain.user_authority.model.UserAuthorityType
import io.dnajd.presentation.components.BugtrackerExpandableMenu
import io.dnajd.presentation.components.BugtrackerIconPairField

@Composable
fun ProjectUserManagementItemContent(
	userWithAuthorities: Map.Entry<String, List<UserAuthority>>,
) {
	var expanded by remember { mutableStateOf(false) }
	BugtrackerExpandableMenu(
		modifier = Modifier
			.padding(
				horizontal = 12.dp,
				vertical = 4.dp
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
						contentDescription = ""
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

					Text(
						modifier = Modifier.padding(start = 2.dp),
						text = "Display role CHANGE THIS",
						color = MaterialTheme.colorScheme.onSurface.copy(0.5f),
						fontSize = 12.sp,
						fontWeight = FontWeight.ExtraLight,
						fontFamily = FontFamily.SansSerif,
					)
				}
			)
		},
		expandableContent = {
			for(authorityType in UserAuthorityType.values()) {
				ProjectUserManagementExpandableCardContent(
					title = stringResource(authorityType.titleResId),
					description = stringResource(authorityType.descriptionResId),
					checked = userWithAuthorities.value.find { it.authority == authorityType } != null,
					onClick = { },
				)
			}
		},
	)
}

@Composable
private fun ProjectUserManagementExpandableCardContent(
	title: String,
	description: String,
	checked: Boolean,
	onClick: () -> Unit,
) {
	Card(
		modifier = Modifier
			.fillMaxWidth()
			.padding(vertical = 4.dp)
			.clickable { onClick() },
		shape = RoundedCornerShape(4.dp),
	) {
		BugtrackerIconPairField(
			modifier = Modifier.padding(horizontal = 8.dp, vertical = 10.dp),
			titleContent = {
				Text(
					modifier = Modifier.padding(start = 2.dp, bottom = 6.dp),
					text = title,
					fontWeight = FontWeight.SemiBold,
				)
			},
			iconContent = {
				Checkbox(
					modifier = Modifier
						.size(24.dp),
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
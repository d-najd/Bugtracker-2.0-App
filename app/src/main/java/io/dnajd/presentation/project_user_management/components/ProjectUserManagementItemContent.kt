package io.dnajd.presentation.project_user_management.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.dnajd.domain.user_authority.model.UserAuthority
import io.dnajd.presentation.components.BugtrackerIconPairField
import io.dnajd.presentation.components.ProjectIconFactory

@Composable
fun ProjectUserManagementItemContent(
	authority: UserAuthority
) {
	BugtrackerIconPairField(
		modifier = Modifier
			.padding(horizontal = 12.dp, vertical = 4.dp)
			.fillMaxWidth()
			.height(42.dp)
			.clickable { },
		iconContent = {
			Icon(
				imageVector = Icons.Default.AccountCircle,
				contentDescription = ""
			)
		},
		textContent = {
			Text(
				modifier = Modifier.padding(start = 2.dp),
				text = authority.username,
				color = MaterialTheme.colorScheme.onSurface,
				fontSize = (13.75).sp,
				fontFamily = FontFamily.SansSerif,
				fontWeight = FontWeight.Light,
			)

			Text(
				modifier = Modifier.padding(start = 2.dp),
				text = authority.authority.name,
				color = MaterialTheme.colorScheme.onSurface.copy(0.5f),
				fontSize = 12.sp,
				fontWeight = FontWeight.ExtraLight,
				fontFamily = FontFamily.SansSerif,
			)
		}
	)
}
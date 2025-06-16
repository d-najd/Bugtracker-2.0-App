package io.dnajd.presentation.table_task.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.dnajd.bugtracker.R
import io.dnajd.bugtracker.ui.table_task.TableTaskScreenState
import io.dnajd.presentation.components.BugtrackerExpandableMenu

@Composable
fun TableTaskAssignedField(
	state: TableTaskScreenState.Success,
) {
	var expanded by remember { mutableStateOf(false) }
	BugtrackerExpandableMenu(
		modifier = Modifier.padding(top = 24.dp),
		expanded = expanded,
		onClick = { expanded = !expanded },
		menuContent = {
			Text(
				modifier = Modifier.padding(start = 3.5.dp),
				fontWeight = FontWeight.Bold,
				fontSize = 16.sp,
				text = stringResource(R.string.field_assigned),
			)

			Row(
				modifier = Modifier.fillMaxWidth(),
				horizontalArrangement = Arrangement.End,
				verticalAlignment = Alignment.CenterVertically,
			) {
				Text(
					text = state.task.assigned.size.toString(),
					color = MaterialTheme.colorScheme.onSurface.copy(0.5f),
				)

				Icon(
					modifier = Modifier.padding(start = 3.dp, end = 4.dp),
					tint = MaterialTheme.colorScheme.onSurface.copy(0.8f),
					imageVector = Icons.Default.ArrowDropDown,
					contentDescription = ""
				)
			}
		},
		expandableContent = {
			for (assigned in state.task.assigned) {
				Row(
					modifier = Modifier.padding(top = 12.dp, start = 16.dp, end = 16.dp),
					horizontalArrangement = Arrangement.Center,
					verticalAlignment = Alignment.CenterVertically,
				) {

					Row(
						modifier = Modifier.weight(1f),
						horizontalArrangement = Arrangement.Start,
						verticalAlignment = Alignment.CenterVertically,
					) {
						Icon(
							modifier = Modifier.size(28.dp),
							imageVector = Icons.Default.AccountCircle,
							contentDescription = ""
						)

						Text(
							modifier = Modifier.padding(start = 8.dp),
							text = assigned.assignerUsername
						)
					}

					Row(
						modifier = Modifier.weight(1f),
						horizontalArrangement = Arrangement.Center,
					) {
						Text(
							text = ">",
							color = colorResource(R.color.coral),
							fontFamily = FontFamily.SansSerif,
							fontSize = 26.sp,
						)
					}

					Row(
						modifier = Modifier.weight(1f),
						horizontalArrangement = Arrangement.End,
						verticalAlignment = Alignment.CenterVertically,
					) {

						Text(
							text = assigned.assignedUsername,
							modifier = Modifier.padding(end = 8.dp),
						)

						Icon(
							modifier = Modifier.size(28.dp),
							imageVector = Icons.Default.AccountCircle,
							contentDescription = ""
						)
					}
				}
			}
		})
}
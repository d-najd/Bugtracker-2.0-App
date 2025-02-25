package io.dnajd.presentation.project_settings.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.dnajd.bugtracker.R
import io.dnajd.bugtracker.ui.project_settings.ProjectSettingsScreenState
import io.dnajd.bugtracker.ui.util.ProjectTableSelectedTab
import io.dnajd.presentation.components.BugtrackerTwoAppBar
import io.dnajd.presentation.components.BugtrackerTwoAppBarTableBar

@Composable
fun ProjectSettingsTopAppBar(
	state: ProjectSettingsScreenState,
	onBackClicked: () -> Unit,

	onSwitchScreenTabClicked: (ProjectTableSelectedTab) -> Unit,
) {
	BugtrackerTwoAppBar(
		navigationIcon = {
			IconButton(onClick = { onBackClicked() }) {
				Icon(
					modifier = Modifier
						.padding(horizontal = 8.dp),
					imageVector = Icons.Rounded.ArrowBack,
					contentDescription = ""
				)
			}
		},
		title = {
			val title =
				if (state is ProjectSettingsScreenState.Success) state.project.title else
					stringResource(R.string.loading)
			Text(
				text = title,
			)
		},
		bottomContent = {
			BugtrackerTwoAppBarTableBar(
				selectedTab = ProjectTableSelectedTab.SETTINGS,
				onTabClicked = onSwitchScreenTabClicked,
			)
		}
	)
}
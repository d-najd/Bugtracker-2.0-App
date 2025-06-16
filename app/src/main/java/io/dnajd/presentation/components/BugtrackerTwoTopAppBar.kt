package io.dnajd.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.dnajd.bugtracker.ui.util.ProjectTableSelectedTab
import io.dnajd.presentation.util.bottomBorder

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BugtrackerTwoAppBar(
	modifier: Modifier = Modifier,
	title: @Composable () -> Unit,
	navigationIcon: @Composable () -> Unit = {},
	actions: @Composable RowScope.() -> Unit = {},
	bottomContent: @Composable RowScope.() -> Unit,
) {
	Column(
		modifier = modifier,
	) {
		TopAppBar(
			navigationIcon = navigationIcon,
			title = title,
			actions = actions,
		)
		Row(
			modifier = Modifier.fillMaxWidth(),
			horizontalArrangement = Arrangement.Center,
			verticalAlignment = Alignment.CenterVertically,
		) {
			bottomContent()
		}
		HorizontalDivider()
	}
}

@Composable
fun BugtrackerTwoAppBarTableBar(
	selectedTab: ProjectTableSelectedTab,
	onTabClicked: (ProjectTableSelectedTab) -> Unit,
) {
	val colorDisabled = MaterialTheme.colorScheme.onSurface.copy(.5f)
	val colorEnabled = MaterialTheme.colorScheme.primary
	for (tab in ProjectTableSelectedTab.values()) {
		val tabModifier = Modifier
			.clickable { onTabClicked(tab) }
			.padding(start = 8.dp, top = 2.dp, end = 8.dp)
			.composed {
				if (selectedTab == tab) {
					return@composed bottomBorder(
						strokeWidth = (1.5).dp, color = MaterialTheme.colorScheme.primary
					)
				}
				this
			}
		Column(
			modifier = tabModifier
		) {
			Text(
				color = if (selectedTab == tab) colorEnabled else colorDisabled,
				text = stringResource(tab.titleResId),
				fontSize = 15.sp,
			)
			Box(modifier = Modifier.height(6.dp))
		}
	}
}
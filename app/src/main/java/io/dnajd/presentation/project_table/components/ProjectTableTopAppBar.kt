package io.dnajd.presentation.project_table.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.FilterList
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.dnajd.bugtracker.ui.util.ProjectTableSelectedTab
import io.dnajd.bugtracker.ui.project_table.ProjectTableScreenState
import io.dnajd.presentation.components.BugtrackerTwoAppBar
import io.dnajd.presentation.components.BugtrackerTwoAppBarTableBar

@Composable
fun ProjectTableTopAppBar(
    state: ProjectTableScreenState,
    onBackClicked: () -> Unit,

    onCreateTableClicked: () -> Unit,
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
            Text(
                text = state.project.title,
            )
        },
        actions = {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    modifier = Modifier
                        .padding(horizontal = 6.dp),
                    imageVector = Icons.Rounded.FilterList,
                    contentDescription = ""
                )
            }
            IconButton(onClick = { onCreateTableClicked() }) {
                Icon(
                    modifier = Modifier
                        .padding(horizontal = 6.dp),
                    imageVector = Icons.Rounded.Add,
                    contentDescription = ""
                )
            }
        },
        bottomContent = {
            BugtrackerTwoAppBarTableBar(
                selectedTab = ProjectTableSelectedTab.BOARD,
                onTabClicked = onSwitchScreenTabClicked,
            )
        }
    )
}
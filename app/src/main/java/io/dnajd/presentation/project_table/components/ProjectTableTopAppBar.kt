package io.dnajd.presentation.project_table.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.FilterList
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.dnajd.bugtracker.R
import io.dnajd.bugtracker.ui.project_table.board.ProjectTableScreenState
import io.dnajd.bugtracker.ui.project_table.ProjectTableSelectedTab
import io.dnajd.presentation.util.bottomBorder

@Composable
fun ProjectTableTopAppBar(
    state: ProjectTableScreenState,
    onBackClicked: () -> Unit,

    onCreateTableClicked: () -> Unit,
) {
    Column {
        TopContent(
            state = state,
            onBackClicked = onBackClicked,
            onCreateTableClicked = onCreateTableClicked,
        )
        BottomContent(
            selectedTab = ProjectTableSelectedTab.BOARD,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopContent(
    state: ProjectTableScreenState,
    onBackClicked: () -> Unit,

    onCreateTableClicked: () -> Unit,
) {
    TopAppBar(
        modifier = Modifier.shadow(elevation = 4.dp),
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
    )
}

@Composable
private fun BottomContent(
    selectedTab: ProjectTableSelectedTab,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        val colorDisabled = MaterialTheme.colorScheme.onSurface.copy(.5f)
        val colorEnabled = MaterialTheme.colorScheme.primary

        val boardModifier = Modifier
            .clickable { }
            .padding(start = 8.dp, top = 2.dp, end = 8.dp)
            .composed {
                if(selectedTab == ProjectTableSelectedTab.BOARD) {
                    return@composed bottomBorder(strokeWidth = (1.5).dp, color = MaterialTheme.colorScheme.primary)
                }
                this
            }
        val settingsModifier = Modifier
            .clickable { }
            .padding(start = 8.dp, top = 2.dp, end = 8.dp)
            .composed {
                if(selectedTab == ProjectTableSelectedTab.SETTINGS) {
                    return@composed bottomBorder(strokeWidth = (1.5).dp, color = MaterialTheme.colorScheme.primary)
                }
                this
            }

        Column(
            modifier = boardModifier,
        ) {
            Text(
                color = if(selectedTab == ProjectTableSelectedTab.BOARD) colorEnabled else colorDisabled,
                text = stringResource(id = R.string.action_board),
                fontSize = 15.sp,
            )
            Box(modifier = Modifier.height(6.dp))
        }

        Column(
            modifier = settingsModifier
        ) {
            Text(
                color = if(selectedTab == ProjectTableSelectedTab.SETTINGS) colorEnabled else colorDisabled,
                text = stringResource(id = R.string.action_settings),
                fontSize = 15.sp,
            )
            Box(modifier = Modifier.height(6.dp))
        }
    }
    Divider()
}
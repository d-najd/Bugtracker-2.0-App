package io.dnajd.presentation.project_table.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.FilterList
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.dnajd.bugtracker.R
import io.dnajd.bugtracker.ui.project_table.ProjectTableScreenState
import io.dnajd.presentation.util.bottomBorder

@Composable
fun ProjectTableTopAppBar(
    state: ProjectTableScreenState.Success,
) {
    Column {
        TopContent(
            state = state
        )
        BottomContent(
            state = state
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopContent(
    state: ProjectTableScreenState.Success,
) {
    TopAppBar(
        modifier = Modifier.shadow(elevation = 4.dp),
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
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    modifier = Modifier
                        .padding(horizontal = 6.dp),
                    imageVector = Icons.Rounded.Add,
                    contentDescription = ""
                )
            }
        },
        navigationIcon = {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    modifier = Modifier
                        .padding(horizontal = 8.dp),
                    imageVector = Icons.Rounded.ArrowBack,
                    contentDescription = ""
                )
            }
        },
    )
}

@Composable
private fun BottomContent(
    state: ProjectTableScreenState.Success,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
    ) {
        val colorDisabled = MaterialTheme.colorScheme.onSurface.copy(.5f)
        val colorEnabled = MaterialTheme.colorScheme.primary

        TextButton(
            modifier = if(state.topBarSelectedIndex == 0) Modifier.bottomBorder(color = MaterialTheme.colorScheme.primary) else Modifier,
            colors = ButtonDefaults.textButtonColors(
                contentColor = if(state.topBarSelectedIndex == 0) colorEnabled else colorDisabled
            ),
            onClick = { /*TODO*/ }
        ) {
            Text(
                text = stringResource(id = R.string.action_board)
            )
        }

        TextButton(
            modifier = if(state.topBarSelectedIndex == 1)
                Modifier.bottomBorder(color = MaterialTheme.colorScheme.primary).padding(start = 12.dp)
            else Modifier.padding(start = 12.dp),
            colors = ButtonDefaults.textButtonColors(
                contentColor = if(state.topBarSelectedIndex == 1) colorEnabled else colorDisabled
            ),
            onClick = { /*TODO*/ }
        ) {
            Text(
                text = stringResource(id = R.string.action_settings)
            )
        }
    }
    Divider()
}
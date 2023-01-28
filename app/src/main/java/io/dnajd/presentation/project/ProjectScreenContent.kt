package io.dnajd.presentation.project

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.dnajd.bugtracker.R
import io.dnajd.bugtracker.ui.project.ProjectScreenState
import io.dnajd.presentation.project.components.ProjectContent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectScreenContent(
    state: ProjectScreenState.Success,
    onProjectClicked: (Long) -> Unit,
    onCreateProjectClicked: () -> Unit,
    onFilterByNameClicked: (String) -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.field_projects),
                    )
                },
                actions = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            modifier = Modifier
                                .padding(horizontal = 6.dp),
                            imageVector  = Icons.Rounded.Search,
                            contentDescription = ""
                        )
                    }
                    IconButton(onClick = { onCreateProjectClicked() }) {
                        Icon(
                            modifier = Modifier
                                .padding(horizontal = 6.dp),
                            imageVector = Icons.Rounded.Add,
                            contentDescription = ""
                        )
                    }
                }
            )
        }
    ) { contentPadding ->
        ProjectContent(
            state = state,
            contentPadding = contentPadding,
            onProjectClicked = onProjectClicked,
        )
    }
}

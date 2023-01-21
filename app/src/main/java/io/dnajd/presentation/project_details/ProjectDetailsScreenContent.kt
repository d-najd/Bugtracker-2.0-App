package io.dnajd.presentation.project_details

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.dnajd.bugtracker.R
import io.dnajd.bugtracker.ui.project_details.ProjectDetailsScreenState
import io.dnajd.presentation.project_details.components.ProjectDetailsContent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectDetailsScreenContent(
    state: ProjectDetailsScreenState.Success,
    onBackClicked: () -> Unit,

    onRenameProjectClicked: (String) -> Unit,
    onDeleteProjectClicked: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { onBackClicked() }) {
                        Icon(
                            modifier = Modifier.padding(horizontal = 8.dp),
                            imageVector = Icons.Rounded.ArrowBack,
                            contentDescription = ""
                        )
                    }
                },
                title = {
                    Text(
                        text = stringResource(R.string.field_project_details),
                    )
                },
            )
        }
    ) { contentPadding ->
        BackHandler { onBackClicked() }

        ProjectDetailsContent(
            state = state,
            contentPadding = contentPadding,
            onRenameProjectClicked = onRenameProjectClicked,
            onDeleteProjectClicked = onDeleteProjectClicked,
        )
    }
}

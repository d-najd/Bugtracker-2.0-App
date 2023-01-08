package io.dnajd.presentation.library

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.dnajd.bugtracker.R
import io.dnajd.bugtracker.ui.library.LibraryScreenState
import io.dnajd.presentation.library.components.LibraryContent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibraryScreenContent(
    presenter: LibraryScreenState.Success,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    titleContentColor = MaterialTheme.colorScheme.primary.copy(.9f),
                    actionIconContentColor = MaterialTheme.colorScheme.inversePrimary,
                ),
                title = {
                    Text(
                        text = stringResource(R.string.label_projects),
                    )
                },
                actions = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            modifier = Modifier
                                .padding(horizontal = 8.dp),
                            imageVector  = Icons.Rounded.Search,
                            contentDescription = ""
                        )
                    }
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            modifier = Modifier
                                .padding(horizontal = 8.dp),
                            imageVector = Icons.Rounded.Add,
                            contentDescription = ""
                        )
                    }
                }
            )
        }
    ) { contentPadding ->
        LibraryContent(
            state = presenter,
            contentPadding = contentPadding,
        )
    }
}

package io.dnajd.presentation.library

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.dnajd.bugtracker.ui.library.LibraryScreenState
import io.dnajd.presentation.library.components.LibraryContent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibraryScreenContent(
    presenter: LibraryScreenState.Success,
) {
    Scaffold { contentPadding ->
        LibraryContent(
            state = presenter,
            contentPadding = contentPadding,
        )
    }
}

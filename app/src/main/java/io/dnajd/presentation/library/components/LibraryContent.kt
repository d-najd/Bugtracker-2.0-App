package io.dnajd.presentation.library.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import io.dnajd.bugtracker.ui.library.LibraryScreenState

@Composable
fun LibraryContent(
    state: LibraryScreenState.Success,
    contentPadding: PaddingValues,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding)
            .background(Color.Red)
    ) {
        Text(
            text = "Hello World")
    }

}
package io.dnajd.presentation.table_task.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import io.dnajd.bugtracker.R

@Composable
fun TableTaskLeaveComment() {
	LocalDensity.current
	Column(
		modifier = Modifier.fillMaxWidth(),
		verticalArrangement = Arrangement.Bottom,
	) {
		HorizontalDivider()

		TextField(
			modifier = Modifier.fillMaxWidth(),
			value = "Test",
			onValueChange = { },
			placeholder = {
				Text(stringResource(R.string.info_comment_first))
			},
			colors = TextFieldDefaults.colors(
				focusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
				unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
				focusedIndicatorColor = Color.Transparent,
				unfocusedIndicatorColor = Color.Transparent,
			),
		)
	}
}

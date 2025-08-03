package io.dnajd.presentation.table_task

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.layout.findRootCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.unit.dp
import io.dnajd.bugtracker.ui.table_task.TableTaskScreenState
import io.dnajd.bugtracker.ui.table_task.TableTaskSheet
import io.dnajd.presentation.table_task.components.TableTaskContent
import io.dnajd.presentation.table_task.components.TableTaskLeaveComment
import io.dnajd.presentation.table_task.sheets.TableTaskBottomSheetContent

@OptIn(
	ExperimentalMaterial3Api::class,
)
@Composable
fun TableTaskScreenContent(
	state: TableTaskScreenState.Success,
	sheetState: SheetState,
	onBackClicked: () -> Unit,

	onRenameTaskClicked: (String) -> Unit,
	onChangeTableClicked: (Long) -> Unit,
	onChangeTableSheetClicked: () -> Unit,
	onAlterDescriptionSheetClicked: () -> Unit,
	onBottomSheetDismissed: () -> Unit,
) {
	Scaffold(
		bottomBar = {
			TableTaskLeaveComment()
		},
	) { contentPadding ->
		BackHandler { onBackClicked() }

		TableTaskContent(
			state = state,
			contentPadding = contentPadding,
			onRenameTaskClicked = onRenameTaskClicked,
			onChangeTableSheetClicked = onChangeTableSheetClicked,
			onAlterDescriptionSheetClicked = onAlterDescriptionSheetClicked,
		)
	}

	if (state.sheet is TableTaskSheet.BottomSheet || sheetState.isVisible) {
		ModalBottomSheet(
			containerColor = MaterialTheme.colorScheme.surface,
			contentColor = MaterialTheme.colorScheme.onSurface.copy(.32f),
			sheetState = sheetState,
			onDismissRequest = {
				onBottomSheetDismissed()
			},
		) {
			Column(
				modifier = Modifier
					.fillMaxWidth()
					.heightIn(min = 1.dp)
					.verticalScroll(rememberScrollState()),
			) {
				TableTaskBottomSheetContent(
					state = state,
					onChangeTableClicked = onChangeTableClicked,
				)
			}
		}
	}
}

/**
 * A custom Modifier that dynamically adjusts bottom padding so the Composable
 * stays above the on-screen keyboard (IME), even when default insets fail.
 *
 * Combines dynamic position-based padding with [Modifier.imePadding] for robustness.
 */
fun Modifier.positionAwareImePadding(): Modifier = composed {
	// Tracks the amount of padding (in pixels) to apply at the bottom
	var consumePadding by remember { mutableIntStateOf(0) }

	this
		// Gets the current position and size of the Composable on the screen
		.onGloballyPositioned { coordinates ->
			val root = coordinates.findRootCoordinates()

			// Calculates the bottom position of the Composable in the window
			val bottom = coordinates.positionInWindow().y + coordinates.size.height

			// Computes how much space is between the Composable's bottom and the screen's bottom
			// This value is then used as padding to keep it above the keyboard
			consumePadding = (root.size.height - bottom).toInt()
				.coerceAtLeast(0)
		}
		// Consumes the calculated padding (converted to dp) to shift the Composable upward
		.consumeWindowInsets(PaddingValues(bottom = consumePadding.dp))

		// Adds Compose’s built-in imePadding as a fallback for any remaining adjustments
		.imePadding()
}

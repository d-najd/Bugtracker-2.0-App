package io.dnajd.presentation.project_table.components

import android.annotation.SuppressLint
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.dnajd.presentation.util.rememberKeyboardState
import org.burnoutcrew.reorderable.ReorderableLazyListState
import org.burnoutcrew.reorderable.detectReorderAfterLongPress
import org.burnoutcrew.reorderable.rememberReorderableLazyListState

@Composable
fun ProjectTableCardContent(
	value: String,
	labels: List<String> = emptyList(),
	taskId: Long,
	reorderableState: ReorderableLazyListState = rememberReorderableLazyListState(onMove = { _, _ -> }),
	isDragging: Boolean = false,
	onTaskClicked: (Long) -> Unit,
) {
	ProjectTableCardContentLocal(
		value = value,
		labels = labels,
		taskId = taskId,
		reorderableState = reorderableState,
		isDragging = isDragging,
		onTaskClicked = onTaskClicked,
	)
}

@Composable
fun ProjectTableTextFieldCardContent(
	@SuppressLint("ModifierParameter") textModifier: Modifier = Modifier,
	value: String,
	onValueChange: (String) -> Unit,
	isKeyboardEnabled: ((Boolean) -> Unit)? = null,
	keyboardActions: KeyboardActions = KeyboardActions.Default,
	keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
) {
	ProjectTableCardContentLocal(
		textModifier = textModifier,
		value = value,
		onValueChange = onValueChange,
		isKeyboardEnabled = isKeyboardEnabled,
		keyboardOptions = keyboardOptions,
		keyboardActions = keyboardActions,
	)
}


@Composable
private fun ProjectTableCardContentLocal(
	@SuppressLint("ModifierParameter") textModifier: Modifier = Modifier,
	value: String,
	onValueChange: ((String) -> Unit)? = null,
	isKeyboardEnabled: ((Boolean) -> Unit)? = null,
	keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
	keyboardActions: KeyboardActions = KeyboardActions.Default,
	labels: List<String> = emptyList(),
	taskId: Long? = null,
	reorderableState: ReorderableLazyListState = rememberReorderableLazyListState(onMove = { _, _ -> }),
	isDragging: Boolean = false,
	onTaskClicked: ((Long) -> Unit)? = null,
) {
	val elevation = animateDpAsState(if (isDragging) 4.dp else 0.dp)
	val isKeyboardOpen by rememberKeyboardState()
	var wasFocused by remember { mutableStateOf(false) }

	Card(
		colors = CardDefaults.cardColors(
			containerColor = MaterialTheme.colorScheme.surface,
			contentColor = MaterialTheme.colorScheme.onSurface,
		),
		shape = RoundedCornerShape(4.dp),
		modifier = Modifier
			.fillMaxWidth()
			.heightIn(min = 65.dp)
			.padding(
				vertical = 2.dp,
				horizontal = 8.dp
			),
	) {
		var cardModifier = Modifier
			.fillMaxWidth()
			.shadow(elevation.value)
			.detectReorderAfterLongPress(reorderableState)
		if (onTaskClicked != null) {
			if (taskId != null) {
				cardModifier = cardModifier.clickable { onTaskClicked(taskId) }
			} else {
				throw IllegalArgumentException("if onTaskClicked is not null taskId must not be null as well")
			}
		}
		Column(
			modifier = cardModifier,
		) {
			if (onValueChange == null) {
				Text(
					modifier = Modifier.padding(
						top = 8.dp,
						start = 12.dp,
						end = 12.dp
					),
					text = value,
					maxLines = 2,
					fontSize = 14.sp,
				)
			} else {
				val focusRequester = remember { FocusRequester() }
				BasicTextField(
					modifier = textModifier
						.padding(
							top = 8.dp,
							start = 12.dp,
							end = 12.dp
						)
						.focusRequester(focusRequester)
						.onFocusChanged {
							if (it.isFocused) {
								wasFocused = true

								if (isKeyboardEnabled != null) {
									isKeyboardEnabled(true)
								}
							}
						},
					value = value,
					onValueChange = onValueChange,
					maxLines = 1,
					textStyle = TextStyle(
						fontSize = 14.sp,
						color = MaterialTheme.colorScheme.onSurface
					),
					keyboardActions = keyboardActions,
					keyboardOptions = keyboardOptions,
				)

				LaunchedEffect(isKeyboardOpen) {
					if (isKeyboardEnabled != null && wasFocused && !isKeyboardOpen) {
						isKeyboardEnabled(false)
					}
				}

				LaunchedEffect(Unit) {
					focusRequester.requestFocus()
				}
			}            /*
			if (labels.isNotEmpty()) {
				Row(
					modifier = Modifier
						.padding(top = 8.dp, start = 12.dp, end = 12.dp)
				) {
					for (i in 0 until minOf(5, labels.size)) {
						Text(
							modifier = Modifier
								.padding(end = 4.dp),
							text = labels[i],
							color = MaterialTheme.colorScheme.onSurface.copy(0.75f),
						)
					}
				}
			}
			if (onValueChange == null) {
				Row(
					modifier = Modifier
						.padding(end = 12.dp),
					verticalAlignment = Alignment.CenterVertically
				) {
					Checkbox(
						checked = true,
						onCheckedChange = { }
					)
					Text(
						modifier = Modifier,
						text = "${stringResource(R.string.field_task).uppercase()}-$taskId"
					)
				}
			}
			 */
		}
	}
}
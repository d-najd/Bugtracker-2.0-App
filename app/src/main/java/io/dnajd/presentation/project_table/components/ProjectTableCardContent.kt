package io.dnajd.presentation.project_table.components

import android.view.ViewTreeObserver
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import io.dnajd.bugtracker.R
import org.burnoutcrew.reorderable.ReorderableLazyListState
import org.burnoutcrew.reorderable.detectReorderAfterLongPress
import org.burnoutcrew.reorderable.rememberReorderableLazyListState

@Composable
fun ProjectTableCardContent(
    value: String,
    labels: List<String> = emptyList(),
    taskId: Long,
    reorderableState: ReorderableLazyListState = rememberReorderableLazyListState(onMove = {_, _ -> }),
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
    value: String,
    onValueChange: (String) -> Unit,
    onKeyboardStateChange: (Boolean) -> Unit
) {
    ProjectTableCardContentLocal(
        value = value,
        onValueChange = onValueChange,
        onKeyboardStateChange = onKeyboardStateChange,
    )
}

@Composable
private fun ProjectTableCardContentLocal(
    value: String,
    onValueChange: ((String) -> Unit)? = null,
    onKeyboardStateChange: ((Boolean) -> Unit)? = null,
    labels: List<String> = emptyList(),
    taskId: Long? = null,
    reorderableState: ReorderableLazyListState = rememberReorderableLazyListState(onMove = {_, _ -> }),
    isDragging: Boolean = false,
    onTaskClicked: ((Long) -> Unit)? = null,
) {
    val elevation = animateDpAsState(if (isDragging) 4.dp else 0.dp)

    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface,
        ),
        shape = RoundedCornerShape(4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 65.dp)
            .padding(vertical = 2.dp, horizontal = 8.dp),
    ) {
        var cardModifier = Modifier
            .fillMaxWidth()
            .shadow(elevation.value)
            .detectReorderAfterLongPress(reorderableState)
        if(onTaskClicked != null) {
            if(taskId != null) {
                cardModifier = cardModifier.clickable { onTaskClicked(taskId) }
            } else {
                throw IllegalArgumentException("if onTaskClicked is not null taskId must not be null as well")
            }
        }
        Column(
            modifier = cardModifier,
        ) {
            if(onValueChange == null) {
                Text(
                    modifier = Modifier.padding(top = 8.dp, start = 12.dp, end = 12.dp),
                    text = value,
                    maxLines = 2,
                    fontSize = 14.sp,
                )
            } else {
                val focusRequester = remember { FocusRequester() }
                BasicTextField(
                    modifier = Modifier
                        .padding(top = 8.dp, start = 12.dp, end = 12.dp)
                        .focusRequester(focusRequester),
                    value = value,
                    onValueChange = onValueChange,
                    maxLines = 1,
                    textStyle = TextStyle(fontSize = 14.sp),
                )

                if(onKeyboardStateChange != null) {
                    val view = LocalView.current
                    DisposableEffect(view) {
                        val listener = ViewTreeObserver.OnGlobalLayoutListener {
                            val isKeyboardEnabled = ViewCompat.getRootWindowInsets(view)
                                    ?.isVisible(WindowInsetsCompat.Type.ime()) ?: true
                            onKeyboardStateChange(isKeyboardEnabled)
                        }
                        view.viewTreeObserver.addOnGlobalLayoutListener(listener)
                        onDispose {
                            view.viewTreeObserver.removeOnGlobalLayoutListener(listener)
                        }
                    }
                } else {
                    throw IllegalArgumentException()
                }

                LaunchedEffect(Unit) {
                    focusRequester.requestFocus()
                }
            }
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
            if(onValueChange == null) {
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
        }
    }
}
package io.dnajd.presentation.project_table.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.dnajd.bugtracker.R
import org.burnoutcrew.reorderable.ReorderableLazyListState
import org.burnoutcrew.reorderable.detectReorderAfterLongPress
import org.burnoutcrew.reorderable.rememberReorderableLazyListState

@Composable
fun ProjectTableCardContent(
    title: String,
    labels: List<String>,
    id: Long,
    reorderableState: ReorderableLazyListState = rememberReorderableLazyListState(onMove = {_, _ -> }),
    isDragging: Boolean = false,
    onTaskClicked: (Long) -> Unit,
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
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(elevation.value)
                .detectReorderAfterLongPress(reorderableState)
                .clickable { onTaskClicked(id) },
        ) {

            // TODO onclick open dialog that lets you change title
            Text(
                modifier = Modifier.padding(top = 8.dp, start = 12.dp, end = 12.dp),
                text = title,
                maxLines = 2,
                fontSize = 14.sp,
            )
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
                    modifier = Modifier
                        .padding(start = 4.dp),
                    text = "${stringResource(R.string.field_task).uppercase()}-$id"
                )
            }
        }
    }
}
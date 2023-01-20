package io.dnajd.presentation.project_table_task.content.comment

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.dnajd.bugtracker.R
import io.dnajd.bugtracker.ui.project_table_task.TableTaskScreenState
import io.dnajd.presentation.components.BugtrackerIconPairField
import io.dnajd.util.BugtrackerDateFormat
import java.util.*

@Composable
fun TableTaskCommentsContent(state: TableTaskScreenState.Success){
    for(comment in state.task.comments) {
        BugtrackerIconPairField(
            modifier = Modifier.padding(top = 12.dp, bottom = 3.dp),
            iconContent = {
                Icon(
                    modifier = Modifier.size(30.dp),
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = ""
                )
            },
            textContent = {
                Row {
                    val fontSize = (14.5).sp
                    val fontWeight = FontWeight.ExtraLight
                    Text(
                        text = comment.user,
                        fontSize = fontSize,
                        fontWeight = fontWeight,
                    )

                    val dateSeconds: Long = if(comment.editedAt != null) (Date().time - comment.editedAt.time)/1000 else (Date().time - comment.createdAt.time)/1000
                    val dateString = if(dateSeconds > 0) BugtrackerDateFormat.generateStringFromTime(timeSeconds = dateSeconds) else "Invalid Date"
                    Text(
                        modifier = Modifier.padding(start = 6.dp),
                        text = "$dateString ${stringResource(R.string.field_ago).lowercase()}",
                        color = MaterialTheme.colorScheme.onSurface.copy(0.5f),
                        fontSize = fontSize,
                        fontWeight = fontWeight,
                    )

                    if(comment.editedAt != null) {
                        Text(
                            text = " - ${stringResource(R.string.field_edited).lowercase()}",
                            color = MaterialTheme.colorScheme.onSurface.copy(0.65f),
                            fontSize = fontSize,
                            fontWeight = fontWeight,
                        )
                    }
                }
                Text(
                    modifier = Modifier.padding(top = 5.dp),
                    text = comment.message,
                    fontWeight = FontWeight.Light,
                    fontFamily = FontFamily.SansSerif,
                    fontSize = 15.sp
                )
            }
        )
    }
}
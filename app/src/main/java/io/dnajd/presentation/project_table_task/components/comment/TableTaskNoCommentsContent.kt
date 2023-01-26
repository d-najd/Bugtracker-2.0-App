package io.dnajd.presentation.project_table_task.components.comment

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.dnajd.bugtracker.R

@Composable
fun TableTaskNoCommentsContent(){
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            modifier = Modifier
                .padding(top = 36.dp)
                .size(86.dp),
            imageVector = Icons.Default.Forum,
            contentDescription = ""
        )

        Text(
            modifier = Modifier
                .padding(top = 8.dp),
            text = stringResource(R.string.info_comment_first),
            fontSize = 15.sp,
        )
    }
}
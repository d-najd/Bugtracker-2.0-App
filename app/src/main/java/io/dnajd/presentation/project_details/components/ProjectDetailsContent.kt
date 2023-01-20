package io.dnajd.presentation.project_details.components

import androidx.compose.foundation.Image
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.TextButton
import io.dnajd.bugtracker.R
import io.dnajd.bugtracker.ui.project_details.ProjectDetailsScreenState
import io.dnajd.presentation.components.BugtrackerExpandableMenu
import io.dnajd.presentation.components.BugtrackerTextFieldExpandable
import io.dnajd.presentation.components.ProjectIconFactory

@Composable
fun ProjectDetailsContent(
    state: ProjectDetailsScreenState.Success,
    contentPadding: PaddingValues,
) {
   Column(
       modifier = Modifier
           .padding(contentPadding)
   ) {
       Column(
           modifier = Modifier
               .fillMaxWidth()
               .padding(vertical = 32.dp),
           horizontalAlignment = Alignment.CenterHorizontally,
       ) {
           Image(
               modifier = Modifier.size(100.dp),
               painter = painterResource(ProjectIconFactory.getRandom()),
               contentDescription = ""
           )
           Row(
               modifier = Modifier.padding(top = 8.dp),
               verticalAlignment = Alignment.CenterVertically,
               horizontalArrangement = Arrangement.Center,
           ) {
               Icon(
                   modifier = Modifier.size(14.dp),
                   imageVector = Icons.Default.Edit,
                   contentDescription = "",
               )
               Text(
                   modifier = Modifier.padding(start = 2.dp),
                   text = stringResource(R.string.action_change_avatar)
               )
           }
       }

       /* TODO FINISH THIS
       BugtrackerTextFieldExpandable(
           modifierText = Modifier.fillMaxWidth(),
           title = title,
           onTitleChange = { title = it },
           label = stringResource(R.string.field_project),
       ) {
           Row(
               modifier = Modifier.fillMaxWidth(),
               verticalAlignment = Alignment.CenterVertically,
               horizontalArrangement = Arrangement.End,
           ) {
               TextButton(onClick = { /*TODO*/ }) {
                   Text(text = stringResource(id = R.string.action_save))
               }
               TextButton(onClick = { /*TODO*/ }) {
                   Text(text = stringResource(id = R.string.action_cancel))
               }
           }
       }
        */
   }
}
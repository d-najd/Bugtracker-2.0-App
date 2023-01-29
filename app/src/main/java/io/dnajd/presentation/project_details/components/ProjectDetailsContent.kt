package io.dnajd.presentation.project_details.components

import androidx.compose.foundation.Image
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.sp
import io.dnajd.bugtracker.R
import io.dnajd.bugtracker.ui.project_details.ProjectDetailsScreenState
import io.dnajd.presentation.components.BugtrackerExpandableTextField
import io.dnajd.presentation.components.BugtrackerExpandableTextFieldDefaults
import io.dnajd.presentation.components.ProjectIconFactory

@Composable
fun ProjectDetailsContent(
    state: ProjectDetailsScreenState.Success,
    contentPadding: PaddingValues,

    onRenameProjectClicked: (String) -> Unit,
    onDeleteProjectClicked: () -> Unit,
) {
   Column(
       modifier = Modifier
           .padding(contentPadding)
   ) {
       Column(
           modifier = Modifier
               .fillMaxWidth()
               .padding(vertical = 36.dp),
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

       var expanded by remember { mutableStateOf(false) }
       var projectTitle by remember{ mutableStateOf(state.project.title) }
       BugtrackerExpandableTextField(
           modifier = Modifier.padding(horizontal = 16.dp),
           modifierText = Modifier
               .fillMaxWidth()
               .onFocusChanged { expanded = it.isFocused },
           value = projectTitle,
           onValueChange = { projectTitle = it },
           expanded = expanded,
           label = stringResource(R.string.field_project_title),
           keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
       ) {
           BugtrackerExpandableTextFieldDefaults.Content(
               onCancelClicked = { expanded = false },
               onConfirmClicked = { onRenameProjectClicked(projectTitle) },
               confirmEnabled = projectTitle != state.project.title && projectTitle.isNotEmpty()
           )
       }

       TextButton(
           modifier = Modifier.padding(start = 4.dp, top = 20.dp),
           onClick = { onDeleteProjectClicked() },
       ) {
           Text(
               text = stringResource(R.string.action_delete_project),
               fontSize = 16.sp,
               fontWeight = FontWeight.SemiBold,
           )
       }
   }
}
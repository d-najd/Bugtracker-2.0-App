package io.dnajd.presentation.project_details.components

import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.dnajd.bugtracker.R
import io.dnajd.bugtracker.ui.project_details.ProjectDetailsScreenState
import io.dnajd.presentation.components.BugtrackerExpandableTextField
import io.dnajd.presentation.components.BugtrackerExpandableTextFieldDefaults
import io.dnajd.util.MediaType
import io.dnajd.util.uriToFile
import java.io.File

@Composable
fun ProjectDetailsContent(
	state: ProjectDetailsScreenState.Success,
	contentPadding: PaddingValues,

	onRenameProjectClicked: (String) -> Unit,
	onDeleteProjectClicked: () -> Unit,
	onChangeProjectIcon: (File) -> Unit,
) {
	Column(
		modifier = Modifier.padding(contentPadding),
	) {
		Column(
			modifier = Modifier
				.fillMaxWidth()
				.padding(vertical = 36.dp),
			horizontalAlignment = Alignment.CenterHorizontally,
		) {
			val launcher = getLauncherForImage(onImagePicked = onChangeProjectIcon)

			Image(
				modifier = Modifier
					.size(100.dp)
					.clickable {
						launcher.launch(
							PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly),
						)
					},
				bitmap = state.projectIconCollected().bitmap.asImageBitmap(),
				contentDescription = "",
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
					text = stringResource(R.string.action_change_avatar),
				)
			}
		}

		val project = state.projectCollected()

		var expanded by remember { mutableStateOf(false) }
		var projectTitle by remember { mutableStateOf(project.title) }
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
				confirmEnabled = projectTitle != project.title && projectTitle.isNotEmpty(),
			)
		}

		TextButton(
			modifier = Modifier.padding(
				start = 4.dp,
				top = 20.dp,
			),
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

@Composable
fun getLauncherForImage(
	onImagePicked: (File) -> Unit,
): ManagedActivityResultLauncher<PickVisualMediaRequest, Uri?> {
	val context = LocalContext.current

	return rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
		if (uri == null) return@rememberLauncherForActivityResult

		val file = context.uriToFile(uri)

		if (MediaType.resolveFrom(file) != MediaType.Image) {
			throw IllegalStateException("Only picking of images is allowed in this context")
		}
		onImagePicked.invoke(file)
	}
}

package io.dnajd.bugtracker.ui.table_task

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import io.dnajd.presentation.components.LoadingScreen
import io.dnajd.presentation.table_task.TableTaskScreenContent
import io.dnajd.presentation.table_task.sheets.TableTaskAlterDescriptionSheet
import io.dnajd.util.toast
import kotlinx.coroutines.flow.collectLatest

class TableTaskScreen(
	private val taskId: Long
) : Screen {
	@OptIn(ExperimentalMaterialApi::class)
	@Composable
	override fun Content() {
		val navigator = LocalNavigator.currentOrThrow
		val context = LocalContext.current
		val screenModel = rememberScreenModel {
			TableTaskStateScreenModel(
				taskId
			)
		}

		LaunchedEffect(Unit) {
			screenModel.events.collectLatest { event ->
				if (event is TableTaskEvent.LocalizedMessage) {
					context.toast(event.stringRes)
				}
				when (event) {
					is TableTaskEvent.CanNotGetParentTable -> {
						navigator.pop()
					}

					is TableTaskEvent.LocalizedMessage -> {}
				}
			}
		}

		val state by screenModel.state.collectAsState()
		if (state is TableTaskScreenState.Loading) {
			LoadingScreen()
			return
		}
		val successState = (state as TableTaskScreenState.Success)
		val bottomState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)

		// this reeks of spaghetti
		if (successState.sheet !is TableTaskSheet.AlterDescriptionSheet) {
			TableTaskScreenContent(
				state = successState,
				bottomDialogState = bottomState,
				onBackClicked = navigator::pop,
				onChangeTableClicked = screenModel::swapTable,
				onChangeTableSheetClicked = { screenModel.showSheet(TableTaskSheet.BottomSheet()) },
				onAlterDescriptionSheetClicked = {
					screenModel.showSheet(
						TableTaskSheet.AlterDescriptionSheet(
							description = successState.task.description ?: ""
						)
					)
				}
			)
		}

		LaunchedEffect(successState.sheet) {
			when (successState.sheet) {
				is TableTaskSheet.BottomSheet -> {
					bottomState.show()
				}

				else -> {
					if (bottomState.isVisible) {
						bottomState.hide()
					}
				}
			}
		}

		when (successState.sheet) {
			is TableTaskSheet.BottomSheet -> {}
			is TableTaskSheet.AlterDescriptionSheet -> {
				TableTaskAlterDescriptionSheet(
					state = successState,
					description = successState.sheet.description,
					onDescriptionChange = screenModel::updateDescription,
					onBackClicked = screenModel::dismissSheet,
				)
			}

			null -> {}
		}

		if (bottomState.progress.from == ModalBottomSheetValue.Expanded && bottomState.progress.to == ModalBottomSheetValue.Hidden) {
			screenModel.dismissSheet()
		}
	}
}
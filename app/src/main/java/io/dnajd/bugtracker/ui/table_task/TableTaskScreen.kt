package io.dnajd.bugtracker.ui.table_task

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
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
	private val taskId: Long,
) : Screen {
	@OptIn(ExperimentalMaterial3Api::class)
	@Composable
	override fun Content() {
		val navigator = LocalNavigator.currentOrThrow
		val context = LocalContext.current
		val screenModel = rememberScreenModel {
			TableTaskStateScreenModel(
				taskId
			)
		}

		LaunchedEffect(screenModel.events) {
			screenModel.events.collectLatest { event ->
				when (event) {
					is TableTaskEvent.FailedToRetrieveTable,
					is TableTaskEvent.FailedToRetrieveTask,
						-> {
						navigator.pop()
					}

					is TableTaskEvent.LocalizedMessage -> {
						context.toast(event.stringRes)
					}
				}
			}
		}

		val state by screenModel.state.collectAsState()
		if (state is TableTaskScreenState.Loading) {
			LoadingScreen()
			return
		}
		val successState = (state as TableTaskScreenState.Success)
		val bottomState = rememberBottomSheetScaffoldState(
			SheetState(
				false,
				LocalDensity.current,
				SheetValue.Hidden
			)
		)

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
					bottomState.bottomSheetState.show()
					bottomState.bottomSheetState.show()
				}

				else -> {
					if (bottomState.bottomSheetState.isVisible) {
						bottomState.bottomSheetState.hide()
					}
				}
			}
		}

		when (successState.sheet) {
			is TableTaskSheet.AlterDescriptionSheet -> {
				TableTaskAlterDescriptionSheet(
					state = successState,
					description = successState.sheet.description,
					onDescriptionChange = screenModel::updateDescription,
					onBackClicked = screenModel::dismissSheet,
				)
			}

			null -> {}
			is TableTaskSheet.BottomSheet -> TODO()
		}

		if (bottomState.bottomSheetState.currentValue == SheetValue.Expanded && bottomState.bottomSheetState.targetValue == SheetValue.Hidden) {
			screenModel.dismissSheet()
		}
	}
}
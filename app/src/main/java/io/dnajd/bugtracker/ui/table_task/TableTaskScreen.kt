package io.dnajd.bugtracker.ui.table_task

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetValue
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import io.dnajd.bugtracker.ui.util.ScreenFixed
import io.dnajd.presentation.components.LoadingScreen
import io.dnajd.presentation.table_task.TableTaskScreenContent
import io.dnajd.presentation.table_task.sheets.TableTaskAlterDescriptionSheet
import io.dnajd.util.toast
import kotlinx.coroutines.flow.collectLatest

class TableTaskScreen(
	private val taskId: Long,
) : ScreenFixed() {
	@OptIn(ExperimentalMaterial3Api::class)
	@Composable
	override fun Content() {
		val navigator = LocalNavigator.currentOrThrow
		val context = LocalContext.current
		val screenModel = rememberScreenModel { TableTaskStateScreenModel(taskId) }

		LaunchedEffect(screenModel.events) {
			screenModel.events.collectLatest { event ->
				when (event) {
					is TableTaskEvent.FailedToRetrieveTable,
					is TableTaskEvent.FailedToRetrieveTask,
						-> {
						context.toast((event as TableTaskEvent.LocalizedMessage).stringRes)
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
		val bottomState = rememberStandardBottomSheetState(
			initialValue = SheetValue.Hidden,
			skipHiddenState = false,
		)

		LaunchedEffect(Unit) {
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
			null,
			is TableTaskSheet.BottomSheet,
				-> {
				TableTaskScreenContent(
					state = successState,
					sheetState = bottomState,
					onBackClicked = navigator::pop,
					onChangeTableClicked = screenModel::swapTable,
					onChangeTableSheetClicked = { screenModel.showSheet(TableTaskSheet.BottomSheet) },
					onAlterDescriptionSheetClicked = {
						screenModel.showSheet(
							TableTaskSheet.AlterDescriptionSheet(
								description = successState.taskNonComposable().description ?: "",
							),
						)
					},
					onBottomSheetDismissed = {
						screenModel.dismissSheet()
					},
				)
			}

			is TableTaskSheet.AlterDescriptionSheet -> {
				TableTaskAlterDescriptionSheet(
					state = successState,
					description = successState.sheet.description,
					onDescriptionChange = screenModel::updateDescription,
					onBackClicked = screenModel::dismissSheet,
				)
			}
		}
	}
}

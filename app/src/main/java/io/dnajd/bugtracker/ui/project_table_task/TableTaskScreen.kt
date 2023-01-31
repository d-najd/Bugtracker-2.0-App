package io.dnajd.bugtracker.ui.project_table_task

import android.os.Handler
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import cafe.adriel.voyager.core.model.coroutineScope
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.currentOrThrow
import io.dnajd.presentation.components.LoadingScreen
import io.dnajd.presentation.project_table_task.TableTaskScreenContent
import io.dnajd.presentation.util.LocalRouter
import io.dnajd.util.launchUI
import kotlinx.coroutines.runBlocking
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class TableTaskScreen(
    private val taskId: Long
) : Screen {
    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    override fun Content() {
        // val navigator = LocalNavigator.currentOrThrow
        val router = LocalRouter.currentOrThrow
        val context = LocalContext.current
        val screenModel = rememberScreenModel { TableTaskStateScreenModel(context, taskId) }

        val state by screenModel.state.collectAsState()

        if (state is TableTaskScreenState.Loading) {
            LoadingScreen()
            return
        }

        val successState = state as TableTaskScreenState.Success
        val bottomState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)

        TableTaskScreenContent(
            state = successState,
            bottomDialogState = bottomState,
            onBackClicked = router::popCurrentController,
            onChangeTableClicked = { screenModel.showDialog(TableTaskDialog.BottomNavDialog) },
        )

        LaunchedEffect(successState.dialog) {
            when(successState.dialog) {
                is TableTaskDialog.BottomNavDialog -> {
                    bottomState.show()
                }
                else -> {
                    bottomState.hide()
                }
            }
        }

        if(bottomState.progress.from == ModalBottomSheetValue.Expanded && bottomState.progress.to == ModalBottomSheetValue.Hidden) {
            screenModel.dismissDialog()
        }
    }
}
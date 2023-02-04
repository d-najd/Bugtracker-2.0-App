package io.dnajd.bugtracker.ui.table_task

import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.core.os.bundleOf
import cafe.adriel.voyager.navigator.Navigator
import io.dnajd.bugtracker.ui.base.controller.FullComposeController

class TableTaskController: FullComposeController {
    constructor(
        taskId: Long,
    ) : super(
        bundleOf(
            TASK_ID_EXTRA to taskId,
        ),
    )

    // the constructor is necessary, removing it will result in a crash
    @Suppress("unused")
    constructor(bundle: Bundle) : this(
        bundle.getLong(TASK_ID_EXTRA),
    )

    private val taskId: Long
        get() = args.getLong(TASK_ID_EXTRA)

    @Composable
    override fun ComposeContent() {
        Navigator(screen = TableTaskScreen(
            taskId = taskId,
        ))
    }

    companion object{
        const val TASK_ID_EXTRA = "taskId"
    }

}

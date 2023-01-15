package io.dnajd.bugtracker.ui.project_table_task

import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.core.os.bundleOf
import cafe.adriel.voyager.navigator.Navigator
import io.dnajd.bugtracker.ui.base.controller.FullComposeController

class TableTaskController: FullComposeController {

    constructor(
        projectId: Long,
        tableId: Long,
        tableTitle: String,
        taskId: Long,
    ) : super(
        bundleOf(
            PROJECT_ID_EXTRA to projectId,
            TABLE_ID_EXTRA to tableId,
            TABLE_TITLE_EXTRA to tableTitle,
            TASK_ID_EXTRA to taskId,
        ),
    )

    // the constructor is necessary, removing it will result in a crash
    @Suppress("unused")
    constructor(bundle: Bundle) : this(
        bundle.getLong(PROJECT_ID_EXTRA),
        bundle.getLong(TABLE_ID_EXTRA),
        bundle.getString(TABLE_TITLE_EXTRA)!!,
        bundle.getLong(TASK_ID_EXTRA),
    )

    private val projectId: Long
        get() = args.getLong(PROJECT_ID_EXTRA)

    private val tableId: Long
        get() = args.getLong(TABLE_ID_EXTRA)

    private val tableTitle: String
        get() = args.getString(TABLE_TITLE_EXTRA)!!

    private val taskId: Long
        get() = args.getLong(TASK_ID_EXTRA)

    @Composable
    override fun ComposeContent() {
        Navigator(screen = TableTaskScreen(
            projectId = projectId,
            tableId = tableId,
            tableTitle = tableTitle,
            taskId = taskId,
        ))
    }

    companion object{
        const val PROJECT_ID_EXTRA = "projectId"
        const val TABLE_ID_EXTRA = "tableId"
        const val TABLE_TITLE_EXTRA = "tableTitle"
        const val TASK_ID_EXTRA = "taskId"
    }

}

package io.dnajd.bugtracker.ui.project_table

import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.core.os.bundleOf
import cafe.adriel.voyager.navigator.Navigator
import com.google.gson.Gson
import io.dnajd.bugtracker.ui.base.controller.FullComposeController
import io.dnajd.domain.project.model.Project
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.get

class ProjectTableController: FullComposeController {
    private constructor(projectString: String) : super(
        bundleOf(
            PROJECT_ID_EXTRA to projectString
        )
    )

    // TODO this is not a great idea
    constructor(project: Project) : super(
        bundleOf(
            PROJECT_ID_EXTRA to Injekt.get<Gson>().toJson(project),
        ),
    )

    // the constructor is necessary, removing it will result in a crash
    @Suppress("unused")
    constructor(bundle: Bundle) : this(
        bundle.getString(PROJECT_ID_EXTRA)!!,
    )

    private val project: Project
        get() = Injekt.get<Gson>().fromJson(args.getString(PROJECT_ID_EXTRA), Project::class.java)!!

    @Composable
    override fun ComposeContent() {
        Navigator(screen = ProjectTableScreen(project))
    }


    companion object{
        const val PROJECT_ID_EXTRA = "projectId"
    }

}

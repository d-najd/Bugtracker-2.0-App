package io.dnajd.bugtracker

import android.os.Bundle
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.bluelinelabs.conductor.Conductor
import com.bluelinelabs.conductor.Router
import com.google.gson.Gson
import io.dnajd.bugtracker.databinding.MainActivityBinding
import io.dnajd.bugtracker.ui.base.controller.setRoot
import io.dnajd.bugtracker.ui.project.ProjectController
import io.dnajd.bugtracker.ui.project_table.ProjectTableController
import io.dnajd.bugtracker.ui.project_table_task.TableTaskController
import io.dnajd.domain.DomainModule
import io.dnajd.domain.project.model.Project
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.get

class MainActivity : AppCompatActivity() {
    private lateinit var binding: MainActivityBinding
    private lateinit var router: Router

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Injekt.importModule(DomainModule())
        Injekt.importModule(MainActivityModule(this))

        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val container: ViewGroup = binding.controllerContainer
        router = Conductor.attachRouter(this, container, savedInstanceState)
            .setPopRootControllerMode(Router.PopRootControllerMode.NEVER)

        // if there is no controller (in other words starting the app) set a root controller
        // TODO SET ROOT
        if(router.backstack.firstOrNull() == null) {
            // router.setRoot(TableTaskController(1L))
            router.setRoot(ProjectTableController(Project(
                id = 1,
                owner = "user1",
                title = "ProjectTitle",
            )))
            router.setRoot(ProjectController())
        }
    }
}
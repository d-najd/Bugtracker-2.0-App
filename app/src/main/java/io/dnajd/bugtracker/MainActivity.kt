package io.dnajd.bugtracker

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import cafe.adriel.voyager.navigator.Navigator
import io.dnajd.bugtracker.databinding.MainActivityBinding
import io.dnajd.bugtracker.theme.BugtrackerTheme
import io.dnajd.bugtracker.ui.project.ProjectScreen
import io.dnajd.domain.DomainModule
import uy.kohesive.injekt.Injekt

class MainActivity : AppCompatActivity() {
    // private lateinit var binding: MainActivityBinding
    // private lateinit var router: Router

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Injekt.importModule(DomainModule())
        Injekt.importModule(MainActivityModule(this))


        // binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(MainActivityBinding.inflate(layoutInflater).root)

        setContent {
            BugtrackerTheme {
                Navigator(ProjectScreen)
            }
        }

        /*
        super.onCreate(savedInstanceState)

        Injekt.importModule(DomainModule())
        Injekt.importModule(MainActivityModule(this))

        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val container: ViewGroup = binding.controllerContainer
        //router = Conductor.attachRouter(this, container, savedInstanceState)
            // .setPopRootControllerMode(Router.PopRootControllerMode.NEVER)

        if(router.backstack.firstOrNull() == null) {
            val projectFake = Project(
                id = 1,
                owner = "user1",
                title = "Fake Title",
            )

            setContent {
                Navigator(ProjectScreen)
            }
            //Navigator(ProjectScreen)
            // router.setRoot(ProjectController())
            // router.setRoot(ProjectTableController(projectFake))
            // router.setRoot(TableTaskController(1L))
            // router.setRoot(ProjectSettingsController(projectFake))
            // router.setRoot(ProjectDetailsController(projectFake))
            // router.setRoot(ProjectUserManagementController(1L))
        }
         */
    }
}
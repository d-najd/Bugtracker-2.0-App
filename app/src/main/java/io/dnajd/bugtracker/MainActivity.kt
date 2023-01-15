package io.dnajd.bugtracker

import android.os.Bundle
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.bluelinelabs.conductor.Conductor
import com.bluelinelabs.conductor.Router
import io.dnajd.bugtracker.databinding.MainActivityBinding
import io.dnajd.bugtracker.ui.base.controller.setRoot
import io.dnajd.bugtracker.ui.project_table.ProjectTableController
import io.dnajd.bugtracker.ui.project_table_task.TableTaskController
import io.dnajd.domain.DomainModule
import uy.kohesive.injekt.Injekt

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
            router.setRoot(TableTaskController(1L, 1L, 1L))
            // router.setRoot(ProjectTableController(1))
            // router.setRoot(ProjectController())
        }
    }
}

/*
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Bugtracker20Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting("Android")
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    Bugtracker20Theme {
        Greeting("Android")
    }
}
 */
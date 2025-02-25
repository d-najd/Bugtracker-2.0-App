package io.dnajd.bugtracker

import androidx.activity.ComponentActivity
import io.dnajd.bugtracker.util.view.ContextHolder
import uy.kohesive.injekt.api.InjektModule
import uy.kohesive.injekt.api.InjektRegistrar
import uy.kohesive.injekt.api.addSingleton
import uy.kohesive.injekt.api.addSingletonFactory

class MainActivityModule(val app: ComponentActivity) : InjektModule {
	override fun InjektRegistrar.registerInjectables() {
		addSingleton(app)

		addSingletonFactory { ContextHolder(app) }
	}
}

/*
class PreferenceModule(val application: Application) : InjektModule {
    override fun InjektRegistrar.registerInjectables() {
    }
}
 */

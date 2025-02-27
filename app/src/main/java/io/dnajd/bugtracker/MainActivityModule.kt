package io.dnajd.bugtracker

import androidx.activity.ComponentActivity
import uy.kohesive.injekt.api.InjektModule
import uy.kohesive.injekt.api.InjektRegistrar

class MainActivityModule(val app: ComponentActivity) : InjektModule {
	override fun InjektRegistrar.registerInjectables() {
		// addSingleton(app)
	}
}

/*
class PreferenceModule(val application: Application) : InjektModule {
    override fun InjektRegistrar.registerInjectables() {
    }
}
 */

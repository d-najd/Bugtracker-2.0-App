package io.dnajd.bugtracker

import androidx.activity.ComponentActivity
import io.dnajd.data.preference.AndroidPreferenceStore
import io.dnajd.domain.preference.service.PreferenceStore
import uy.kohesive.injekt.api.InjektModule
import uy.kohesive.injekt.api.InjektRegistrar
import uy.kohesive.injekt.api.addSingletonFactory

class MainActivityModule(val app: ComponentActivity) : InjektModule {
	override fun InjektRegistrar.registerInjectables() {
		addSingletonFactory<PreferenceStore> { AndroidPreferenceStore(app) }
	}
}

/*
class PreferenceModule(val application: Application) : InjektModule {
    override fun InjektRegistrar.registerInjectables() {
    }
}
 */

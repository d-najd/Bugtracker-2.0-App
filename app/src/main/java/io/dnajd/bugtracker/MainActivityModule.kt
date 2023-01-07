package io.dnajd.bugtracker

import androidx.appcompat.app.AppCompatActivity
import io.dnajd.bugtracker.util.view.ContextHolder
import uy.kohesive.injekt.api.InjektModule
import uy.kohesive.injekt.api.InjektRegistrar
import uy.kohesive.injekt.api.addSingleton
import uy.kohesive.injekt.api.addSingletonFactory

class MainActivityModule(val app: AppCompatActivity) : InjektModule {
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

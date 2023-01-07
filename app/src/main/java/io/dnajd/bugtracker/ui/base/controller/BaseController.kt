package io.dnajd.bugtracker.ui.base.controller

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.bluelinelabs.conductor.Controller
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import logcat.logcat

abstract class BaseController<VB : ViewBinding>(bundle: Bundle? = null) : Controller(bundle) {

    protected lateinit var binding: VB
        private set

    lateinit var viewScope: CoroutineScope

    init {
        retainViewMode = RetainViewMode.RETAIN_DETACH

        addLifecycleListener(
            object : LifecycleListener() {
                override fun postCreateView(controller: Controller, view: View) {
                    onViewCreated(view)
                }

                override fun preCreateView(controller: Controller) {
                    viewScope = MainScope()
                    logcat { "Create view for ${controller.instance()}" }
                }

                override fun preAttach(controller: Controller, view: View) {
                    logcat { "Attach view for ${controller.instance()}" }
                }

                override fun preDetach(controller: Controller, view: View) {
                    logcat { "Detach view for ${controller.instance()}" }
                }

                override fun preDestroyView(controller: Controller, view: View) {
                    viewScope.cancel()
                    logcat { "Destroy view for ${controller.instance()}" }
                }
            },
        )
    }

    abstract fun createBinding(inflater: LayoutInflater): VB

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup, savedViewState: Bundle?): View {
        binding = createBinding(inflater)
        return binding.root
    }

    open fun onViewCreated(view: View) {}

    private fun Controller.instance(): String {
        return "${javaClass.simpleName}@${Integer.toHexString(hashCode())}"
    }
}

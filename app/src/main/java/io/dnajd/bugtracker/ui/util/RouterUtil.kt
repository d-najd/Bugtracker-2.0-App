package io.dnajd.bugtracker.ui.util

import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.ControllerChangeHandler
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.RouterTransaction
import com.bluelinelabs.conductor.asTransaction

fun Router.setAtBackstack(index: Int, controller: Controller, controllerChangeHandler: ControllerChangeHandler? = null) {
    this.setAtBackstack(index, controller.asTransaction())
}

fun Router.setAtBackstack(index: Int, controller: RouterTransaction, controllerChangeHandler: ControllerChangeHandler? = null): Router {
    val backstack = this.backstack
    backstack[index] = controller
    this.setBackstack(backstack, controllerChangeHandler)
    return this
}

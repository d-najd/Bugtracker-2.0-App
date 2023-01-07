package io.dnajd.bugtracker.ui.base.controller

import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.asTransaction

fun Router.setRoot(controller: Controller) {
    setRoot(controller.asTransaction())
}

fun Router.pushController(controller: Controller) {
    pushController(controller.asTransaction())
}
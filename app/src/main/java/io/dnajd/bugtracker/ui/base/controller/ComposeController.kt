package io.dnajd.bugtracker.ui.base.controller

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.activity.OnBackPressedDispatcherOwner
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import io.dnajd.presentation.util.LocalRouter
import io.dnajd.bugtracker.util.view.setComposeContent
import io.dnajd.bugtracker.databinding.ComposeControllerBinding

abstract class FullComposeController(bundle: Bundle? = null) :
    BaseController<ComposeControllerBinding>(bundle),
    ComposeContentController {

    override fun createBinding(inflater: LayoutInflater) =
        ComposeControllerBinding.inflate(inflater)

    override fun onViewCreated(view: View) {
        super.onViewCreated(view)

        binding.root.apply {
            setComposeContent {
                CompositionLocalProvider(LocalRouter provides router) {
                    ComposeContent()
                }
            }
        }
    }

    // Let Compose view handle this
    override fun handleBack(): Boolean {
        val dispatcher = (activity as? OnBackPressedDispatcherOwner)?.onBackPressedDispatcher ?: return false
        return if (dispatcher.hasEnabledCallbacks()) {
            dispatcher.onBackPressed()
            true
        } else {
            false
        }
    }
}

interface ComposeContentController {
    @Composable fun ComposeContent()
}

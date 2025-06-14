package io.dnajd.presentation.auth

import AuthContentPickUsername
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.dnajd.bugtracker.ui.auth.AuthScreenState
import io.dnajd.presentation.auth.components.AuthContentOAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthScreenContent(
	screenState: AuthScreenState,
) {
	Scaffold { contentPadding ->
		Column(
			modifier = Modifier
				.fillMaxSize()
				.padding(contentPadding),
		) {
			when (screenState) {
				AuthScreenState.SignedOut -> {
					AuthContentOAuth(contentPadding)
				}

				AuthScreenState.PickingUsername -> {
					AuthContentPickUsername()
				}

				AuthScreenState.Loading -> {
					throw IllegalStateException()
				}
			}
		}
	}
}
package io.dnajd.presentation.auth

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.dnajd.bugtracker.ui.auth.AuthScreenState
import io.dnajd.domain.google_auth.model.CreateUser
import io.dnajd.presentation.auth.components.AuthContentOAuth

@Composable
fun AuthScreenContent(
	screenState: AuthScreenState.Success,

	onSignUpClicked: (String, CreateUser) -> Unit,
	onSignInClicked: (String) -> Unit,
) {
	Scaffold { contentPadding ->
		Column(
			modifier = Modifier
				.fillMaxSize()
				.padding(contentPadding),
		) {
			AuthContentOAuth(
				contentPadding,
				onSignUpClicked,
				onSignInClicked
			)
		}
	}
}
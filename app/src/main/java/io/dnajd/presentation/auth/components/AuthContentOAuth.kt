package io.dnajd.presentation.auth.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mmk.kmpauth.google.GoogleAuthCredentials
import com.mmk.kmpauth.google.GoogleAuthProvider
import com.mmk.kmpauth.google.GoogleButtonUiContainer
import com.mmk.kmpauth.uihelper.google.GoogleButtonMode
import com.mmk.kmpauth.uihelper.google.GoogleSignInButton
import io.dnajd.bugtracker.R
import io.dnajd.domain.google_auth.model.CreateUser
import io.dnajd.util.toast

@Composable
fun AuthContentOAuth(
	contentPadding: PaddingValues,

	onSignUpClicked: (String, CreateUser) -> Unit,
	onSignInClicked: (String) -> Unit,
) {
	val context = LocalContext.current

	Column(
		modifier = Modifier
			.padding(contentPadding)
			.padding(top = 58.dp)
			.padding(horizontal = 16.dp)
	) {
		Text(
			text = stringResource(R.string.info_log_in_to_app),
			fontSize = 24.sp,
			fontWeight = FontWeight.ExtraBold
		)

		Text(
			modifier = Modifier.padding(top = 36.dp),
			text = stringResource(R.string.field_username),
			fontWeight = FontWeight.SemiBold
		)

		var username by remember { mutableStateOf("") }
		OutlinedTextField(
			modifier = Modifier
				.padding(top = 6.dp)
				.fillMaxWidth(),
			value = username,
			onValueChange = { username = it },
			placeholder = {
				Text(
					text = stringResource(R.string.info_pick_username),
				)
			},
		)

		var authReady by remember { mutableStateOf(false) }
		LaunchedEffect(Unit) {
			GoogleAuthProvider.create(
				credentials = GoogleAuthCredentials(
					serverId = "523144607813-ccib1llvilpg1e6httmo9a0d839bhh9h.apps.googleusercontent.com"
				)
			)
			authReady = true
		}

		if (authReady) {
			GoogleButtonUiContainer(
				modifier = Modifier
					.align(Alignment.CenterHorizontally)
					.padding(top = 36.dp)
					.height(52.dp)
					.fillMaxWidth(),
				onGoogleSignInResult = { googleUser ->
					if (googleUser == null) {
						return@GoogleButtonUiContainer
					}

					onSignUpClicked(
						googleUser.idToken,
						CreateUser(username)
					)
				}) {
				GoogleSignInButton(
					modifier = Modifier.fillMaxSize(),
					mode = GoogleButtonMode.Dark,
					text = stringResource(R.string.field_sign_up_google),
					onClick = {
						if (!authReady) {
							return@GoogleSignInButton
						}

						if (username.isBlank()) {
							context.toast(R.string.info_pick_username)
							return@GoogleSignInButton
						}

						this.onClick()
					})
			}
		}

		Box(
			modifier = Modifier
				.padding(top = 36.dp)
				.fillMaxWidth(),
			contentAlignment = Alignment.Center
		) {
			HorizontalDivider(
				modifier = Modifier,
			)
			Text(
				modifier = Modifier
					.background(MaterialTheme.colorScheme.background)
					.padding(horizontal = 8.dp),
				text = stringResource(R.string.field_or).uppercase(),
				fontSize = 18.sp,
			)
		}

		if (authReady) {
			GoogleButtonUiContainer(
				modifier = Modifier
					.align(Alignment.CenterHorizontally)
					.padding(top = 36.dp)
					.height(52.dp)
					.fillMaxWidth(),
				onGoogleSignInResult = { googleUser ->
					if (googleUser == null) {
						return@GoogleButtonUiContainer
					}

					onSignInClicked(googleUser.idToken)
				}) {
				GoogleSignInButton(
					modifier = Modifier.fillMaxSize(),
					mode = GoogleButtonMode.Light,
					text = stringResource(R.string.field_sign_up_google),
					onClick = {
						if (!authReady) {
							return@GoogleSignInButton
						}

						this.onClick()
					})
			}
		}
	}
}
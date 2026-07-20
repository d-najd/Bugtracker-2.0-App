package io.dnajd.presentation.auth.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.mmk.kmpauth.google.rememberGoogleSignInState
import io.dnajd.bugtracker.R
import io.dnajd.domain.google_auth.model.CreateUser

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
			.padding(horizontal = 16.dp),
	) {
		Text(
			text = stringResource(R.string.info_log_in_to_app),
			fontSize = 24.sp,
			fontWeight = FontWeight.ExtraBold,
		)

		Text(
			modifier = Modifier.padding(top = 36.dp),
			text = stringResource(R.string.field_username),
			fontWeight = FontWeight.SemiBold,
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

		var authMode by remember { mutableStateOf(AuthMode.SIGN_IN) }

		GoogleAuthProvider.create(
			credentials = GoogleAuthCredentials(
				serverId = "523144607813-ccib1llvilpg1e6httmo9a0d839bhh9h.apps.googleusercontent.com", // TODO move outside!!!!
			),
		)

		val googleSigninState = rememberGoogleSignInState(
			filterByAuthorizedAccounts = false,
			isAutoSelectEnabled = false,
			onResult = { googleUser ->
				if (googleUser == null) {
					return@rememberGoogleSignInState
				}
				if (authMode == AuthMode.SIGN_UP) {
					onSignUpClicked(
						googleUser.idToken,
						CreateUser(username),
					)
				} else {
					onSignInClicked(googleUser.idToken)
				}
			},
		)

//		GoogleSignInButton(
//			modifier = Modifier
//				.align(Alignment.CenterHorizontally)
//				.padding(top = 36.dp)
//				.height(52.dp)
//				.fillMaxWidth(),
//			mode = GoogleButtonMode.Dark,
//			text = stringResource(R.string.field_sign_up_google),
//			onClick = {
//				if (googleSigninState.isInProgress) {
//					return@GoogleSignInButton
//				}
//
//				if (username.isBlank()) {
//					context.toast(R.string.info_pick_username)
//					return@GoogleSignInButton
//				}
//
//				authMode = AuthMode.SIGN_UP
//			},
//		)

		Box(
			modifier = Modifier
				.padding(top = 36.dp)
				.fillMaxWidth(),
			contentAlignment = Alignment.Center,
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

		GoogleSignInButton(
			modifier = Modifier
				.align(Alignment.CenterHorizontally)
				.padding(top = 36.dp)
				.height(52.dp)
				.fillMaxWidth(),

			mode = GoogleButtonMode.Light,
			text = stringResource(R.string.field_sign_in_google),
		) {
			if (googleSigninState.isInProgress) {
				return@GoogleSignInButton
			}


			authMode = AuthMode.SIGN_IN
			googleSigninState.launch()
		}

//		GoogleSignInButton(
//			modifier = Modifier
//				.align(Alignment.CenterHorizontally)
//				.padding(top = 36.dp)
//				.height(52.dp)
//				.fillMaxWidth(),
//			mode = GoogleButtonMode.Light,
//			text = stringResource(R.string.field_sign_in_google),
//			onClick = {
//				if (googleSigninState.isInProgress) {
//					return@GoogleSignInButton
//				}
//
//				authMode = AuthMode.SIGN_IN
//				googleSigninState.launch()
//			},
//		)
	}
}

object AuthMode {
	const val SIGN_IN = "signin"
	const val SIGN_UP = "signup"
}

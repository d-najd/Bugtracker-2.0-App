package io.dnajd.presentation.auth.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.mmk.kmpauth.google.GoogleAuthCredentials
import com.mmk.kmpauth.google.GoogleAuthProvider
import com.mmk.kmpauth.google.GoogleButtonUiContainer
import com.mmk.kmpauth.uihelper.google.GoogleSignInButton

@Composable
fun AuthContentOAuth() {
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
		Box(
			modifier = Modifier.fillMaxSize(),
			contentAlignment = Alignment.Center
		) {
			GoogleButtonUiContainer(
				onGoogleSignInResult = { googleUser ->
					val tokenId = googleUser?.idToken
					println("TOKEN: $tokenId")
				}
			) {
				GoogleSignInButton(
					onClick = { this.onClick() }
				)
			}
		}
	}


	/*
	Button(
		onClick = {},
		modifier = Modifier
			.fillMaxWidth()
			.padding(horizontal = 20.dp)
			.height(45.dp),
		colors = ButtonDefaults.buttonColors(containerColor = Color.White),
		shape = RoundedCornerShape(12.dp),
		elevation = ButtonDefaults.buttonElevation()
	) {
		Row(
			verticalAlignment = Alignment.CenterVertically
		) {
			Icon(
				painter = painterResource(id = R.drawable.ic_google),
				contentDescription = "Google icon",
				tint = Color.Unspecified,
			)
			Text(
				text = "Sign in with Google",
				color = Color.Black,
				fontWeight = FontWeight.W600,
				fontSize = 16.sp,
				modifier = Modifier.padding(start = 16.dp)
			)
		}
	}
	 */
}
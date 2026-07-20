package io.dnajd.presentation.auth.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LogoDev
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

enum class GoogleButtonMode { Light, Dark }

@Composable
fun GoogleSignInButton(
	modifier: Modifier = Modifier,
	mode: GoogleButtonMode = GoogleButtonMode.Light,
	text: String = "Sign in with Google",
	onClick: () -> Unit,
) {
	val containerColor = if (mode == GoogleButtonMode.Light) Color.White else Color.Black
	val contentColor = if (mode == GoogleButtonMode.Light) Color.Black else Color.White

	OutlinedButton(
		onClick = onClick,
		modifier = modifier,
		colors = ButtonDefaults.outlinedButtonColors(
			containerColor = containerColor,
			contentColor = contentColor,
		),
		contentPadding = PaddingValues(horizontal = 12.dp),
	) {
		Row(
			verticalAlignment = Alignment.CenterVertically,
			horizontalArrangement = Arrangement.Center,
		) {
			Icon(
				imageVector = Icons.Default.LogoDev,
				// painter = painterResource(Res.drawable.ic_google), // Uses your local asset safely
				contentDescription = "Google Icon",
				tint = Color.Unspecified,
				modifier = Modifier.size(20.dp),
			)
			Spacer(modifier = Modifier.width(8.dp))
			Text(
				text = text,
				style = MaterialTheme.typography.labelLarge,
			)
		}
	}
}

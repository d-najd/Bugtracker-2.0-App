package io.dnajd.presentation.auth.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.dnajd.bugtracker.R

@Composable
fun AuthContentOAuth() {
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
}
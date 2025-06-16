import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.dnajd.bugtracker.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthContentPickUsername() {
	TextField(
		modifier = Modifier
			.fillMaxWidth()
			.padding(horizontal = 20.dp), value = "", onValueChange = { }, placeholder = {
		Text(text = stringResource(R.string.info_pick_username))
	})
}
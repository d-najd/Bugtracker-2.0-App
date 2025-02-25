package io.dnajd.presentation.components

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

object ProjectIconFactory {
	private const val COUNT = 22
	private const val NAME_PREFIX = "ic_project_"

	// adding hundreds of lines of code will probably be slower in this case and much more verbose
	@SuppressLint("DiscouragedApi")
	@Composable
	fun getRandom(): Int = LocalContext.current.resources.getIdentifier(
		NAME_PREFIX + (1..COUNT).random(),
		"drawable",
		LocalContext.current.packageName
	)

	// adding hundreds of lines of code will probably be slower in this case and much more verbose
	@SuppressLint("DiscouragedApi")
	@Composable
	fun getSpecific(
		index: Int,
	): Int = LocalContext.current.resources.getIdentifier(
		NAME_PREFIX + index,
		"drawable",
		LocalContext.current.packageName
	)
}
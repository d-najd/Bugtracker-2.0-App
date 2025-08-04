package io.dnajd.presentation.util

import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

/**
 * Has transparent container and indicator color
 *
 * This could be converted to a builder and something like: this.colorBuilder(containerColor = Color.Blue)
 * 	.containerColor(all = Color.Yellow)
 * 	.containerColors(focusedContainerColor = Color.Red).build().copy(otherOverride = Color.Red)
 *
 * @see TextFieldDefaults.colors
 */
@Composable
fun TextFieldDefaults.transparentColors(
	focusedContainerColor: Color = Color.Transparent,
	unfocusedContainerColor: Color = Color.Transparent,
	disabledContainerColor: Color = Color.Transparent,
	errorContainerColor: Color = Color.Transparent,
	focusedIndicatorColor: Color = Color.Transparent,
	unfocusedIndicatorColor: Color = Color.Transparent,
	disabledIndicatorColor: Color = Color.Transparent,
	errorIndicatorColor: Color = Color.Transparent,

	focusedTextColor: Color = Color.Unspecified,
	unfocusedTextColor: Color = Color.Unspecified,
	disabledTextColor: Color = Color.Unspecified,
	errorTextColor: Color = Color.Unspecified,
	cursorColor: Color = Color.Unspecified,
	errorCursorColor: Color = Color.Unspecified,
	selectionColors: TextSelectionColors? = null,
	focusedLeadingIconColor: Color = Color.Unspecified,
	unfocusedLeadingIconColor: Color = Color.Unspecified,
	disabledLeadingIconColor: Color = Color.Unspecified,
	errorLeadingIconColor: Color = Color.Unspecified,
	focusedTrailingIconColor: Color = Color.Unspecified,
	unfocusedTrailingIconColor: Color = Color.Unspecified,
	disabledTrailingIconColor: Color = Color.Unspecified,
	errorTrailingIconColor: Color = Color.Unspecified,
	focusedLabelColor: Color = Color.Unspecified,
	unfocusedLabelColor: Color = Color.Unspecified,
	disabledLabelColor: Color = Color.Unspecified,
	errorLabelColor: Color = Color.Unspecified,
	focusedPlaceholderColor: Color = Color.Unspecified,
	unfocusedPlaceholderColor: Color = Color.Unspecified,
	disabledPlaceholderColor: Color = Color.Unspecified,
	errorPlaceholderColor: Color = Color.Unspecified,
	focusedSupportingTextColor: Color = Color.Unspecified,
	unfocusedSupportingTextColor: Color = Color.Unspecified,
	disabledSupportingTextColor: Color = Color.Unspecified,
	errorSupportingTextColor: Color = Color.Unspecified,
	focusedPrefixColor: Color = Color.Unspecified,
	unfocusedPrefixColor: Color = Color.Unspecified,
	disabledPrefixColor: Color = Color.Unspecified,
	errorPrefixColor: Color = Color.Unspecified,
	focusedSuffixColor: Color = Color.Unspecified,
	unfocusedSuffixColor: Color = Color.Unspecified,
	disabledSuffixColor: Color = Color.Unspecified,
	errorSuffixColor: Color = Color.Unspecified,
) = TextFieldDefaults.colors()
	.copy(
		focusedTextColor = focusedTextColor,
		unfocusedTextColor = unfocusedTextColor,
		disabledTextColor = disabledTextColor,
		errorTextColor = errorTextColor,
		focusedContainerColor = focusedContainerColor,
		unfocusedContainerColor = unfocusedContainerColor,
		disabledContainerColor = disabledContainerColor,
		errorContainerColor = errorContainerColor,
		cursorColor = cursorColor,
		errorCursorColor = errorCursorColor,
		textSelectionColors = selectionColors,
		focusedIndicatorColor = focusedIndicatorColor,
		unfocusedIndicatorColor = unfocusedIndicatorColor,
		disabledIndicatorColor = disabledIndicatorColor,
		errorIndicatorColor = errorIndicatorColor,
		focusedLeadingIconColor = focusedLeadingIconColor,
		unfocusedLeadingIconColor = unfocusedLeadingIconColor,
		disabledLeadingIconColor = disabledLeadingIconColor,
		errorLeadingIconColor = errorLeadingIconColor,
		focusedTrailingIconColor = focusedTrailingIconColor,
		unfocusedTrailingIconColor = unfocusedTrailingIconColor,
		disabledTrailingIconColor = disabledTrailingIconColor,
		errorTrailingIconColor = errorTrailingIconColor,
		focusedLabelColor = focusedLabelColor,
		unfocusedLabelColor = unfocusedLabelColor,
		disabledLabelColor = disabledLabelColor,
		errorLabelColor = errorLabelColor,
		focusedPlaceholderColor = focusedPlaceholderColor,
		unfocusedPlaceholderColor = unfocusedPlaceholderColor,
		disabledPlaceholderColor = disabledPlaceholderColor,
		errorPlaceholderColor = errorPlaceholderColor,
		focusedSupportingTextColor = focusedSupportingTextColor,
		unfocusedSupportingTextColor = unfocusedSupportingTextColor,
		disabledSupportingTextColor = disabledSupportingTextColor,
		errorSupportingTextColor = errorSupportingTextColor,
		focusedPrefixColor = focusedPrefixColor,
		unfocusedPrefixColor = unfocusedPrefixColor,
		disabledPrefixColor = disabledPrefixColor,
		errorPrefixColor = errorPrefixColor,
		focusedSuffixColor = focusedSuffixColor,
		unfocusedSuffixColor = unfocusedSuffixColor,
		disabledSuffixColor = disabledSuffixColor,
		errorSuffixColor = errorSuffixColor,
	)

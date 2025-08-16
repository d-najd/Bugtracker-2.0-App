package io.dnajd.util

import android.content.ContentResolver
import android.net.Uri

fun ContentResolver.getFileExtension(uri: Uri): String? {
	return getType(uri)?.split("/")
		?.last()
}

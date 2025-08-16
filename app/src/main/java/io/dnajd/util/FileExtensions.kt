package io.dnajd.util

import android.webkit.MimeTypeMap
import java.io.File

fun File.getExtension(): String? {
	return MimeTypeMap.getFileExtensionFromUrl(
		this.toURI()
			.toString(),
	)
}

fun File.getMimeType(): String? {
	return MimeTypeMap.getSingleton()
		.getMimeTypeFromExtension(this.getExtension())
}

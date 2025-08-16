package io.dnajd.util

import java.io.File

enum class MediaType(val serializedName: String) {
	Unknown("unknown"),
	Image("image"),
	Video("video");

	companion object {
		fun resolveFrom(source: File): MediaType {
			val mimeType = source.getMimeType()
			if (mimeType?.startsWith("image") == true)
				return Image
			if (mimeType?.startsWith("video") == true)
				return Video
			return Unknown
		}
	}
}


package io.dnajd.domain.project_icon.model

import android.graphics.Bitmap
import io.dnajd.domain.base.BaseApiEntity
import java.io.Serializable

data class ProjectIcon(
	val projectId: Long,
	val bitmap: Bitmap,
) : Serializable, BaseApiEntity<Long> {
	override fun getId(): Long {
		return projectId
	}
}

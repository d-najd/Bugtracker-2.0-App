package io.dnajd.domain.project_icon.service

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import okhttp3.MultipartBody
import okhttp3.ResponseBody

interface ProjectIconApiService {

	suspend fun getByProjectId(projectId: Long): Result<ResponseBody>

	suspend fun getByProjectIdAsBitmap(projectId: Long): Result<Bitmap> {
		val result = getByProjectId(projectId)
			.onFailure {
				return Result.failure<Bitmap>(it)
			}
			.getOrThrow()

		try {
			val bitmap = BitmapFactory.decodeStream(result.byteStream())
			return Result.success(bitmap)
		} catch (e: Exception) {
			return Result.failure<Bitmap>(e)
		}
	}

	suspend fun updateByProjectId(projectId: Long, file: MultipartBody.Part): Result<Unit>

}

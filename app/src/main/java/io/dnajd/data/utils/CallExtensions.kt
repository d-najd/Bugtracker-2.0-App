package io.dnajd.data.utils

import retrofit2.Call
import retrofit2.HttpException

fun <T> Call<T>.toResult(): Result<T> {
	try {
		val response = this.execute()
		if (!response.isSuccessful) {
			return Result.failure(HttpException(response))
		}
		if (response.body() != null) {
			return Result.success(response.body()!!)
		}

		return Result.failure(HttpException(response))
	} catch (e: Exception) {
		return Result.failure(e)
	}
}
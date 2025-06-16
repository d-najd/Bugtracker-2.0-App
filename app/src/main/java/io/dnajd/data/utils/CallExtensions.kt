package io.dnajd.data.utils

import retrofit2.Call
import retrofit2.HttpException
import java.io.IOException

class RetrofitFailedException(e: Exception) : IOException(e)

fun <T> handleRetrofitRequest(request: () -> (Call<T>)): Result<T> {
	return try {
		request().toResult()
	} catch (e: Exception) {
		Result.failure(RetrofitFailedException(e))
	}
}

fun <T> Call<T>.toResult(): Result<T> {
	try {
		val response = this.execute()
		if (!response.isSuccessful) {
			return Result.failure(HttpException(response))
		}
		if (response.body() != null) {
			return Result.success(response.body()!!)
		} else {
			@Suppress("UNCHECKED_CAST") return Result.success(Unit as T)
		}
	} catch (e: Exception) {
		return Result.failure(e)
	}
}
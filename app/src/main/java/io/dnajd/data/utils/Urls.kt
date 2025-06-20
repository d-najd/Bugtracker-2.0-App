package io.dnajd.data.utils

object Urls {
	const val SERVER_URL: String = "http://192.168.0.150:8080/"
	const val BASE: String = "${SERVER_URL}api/"
	const val PROJECT: String = BASE + "project/"
	const val PROJECT_TABLE: String = BASE + "project-table/"
	const val PROJECT_TABLE_ISSUE: String = BASE + "project-table-issue/"
	const val GOOGLE_AUTH: String = BASE + "google_auth/"
	const val JWT_REFRESH_AUTH: String = BASE + "jwt_auth/"
}

/*
/**
 * enum is not used because it is 'not compile time compliant' so I had to resort to using this, it is recommended to get the
 * fields using the interface [UrlsGetFields] where possible since it offers more flexibility
 */
@Suppress("ClassName")
sealed class Urls : UrlsGetFields {
	object API : Urls() {
		const val baseUrlLocal = "http://192.168.0.150:8080"
		const val appendedUrlLocal = "http://192.168.0.150:8080"
		override fun getBaseUrl(): String = baseUrlLocal
		override fun getAppendedUrl(): String = appendedUrlLocal
	}

	@Suppress("MemberVisibilityCanBePrivate")
	object PROJECT : Urls() {
		const val baseUrlLocal = "project-service/api"
		const val appendedUrlLocal = "${API.baseUrlLocal}/$baseUrlLocal"
		override fun getBaseUrl(): String = baseUrlLocal
		override fun getAppendedUrl(): String = appendedUrlLocal
	}

	@Suppress("MemberVisibilityCanBePrivate")
	object PROJECT_TABLE : Urls() {
		const val baseUrlLocal = "project-table-service/api"
		const val appendedUrlLocal = "${API.baseUrlLocal}/$baseUrlLocal"
		override fun getBaseUrl(): String = baseUrlLocal
		override fun getAppendedUrl(): String = appendedUrlLocal
	}

	@Suppress("MemberVisibilityCanBePrivate")
	object TABLE_TASK : Urls() {
		const val baseUrlLocal = "project-table-issue-service/api"
		const val appendedUrlLocal = "${API.baseUrlLocal}/$baseUrlLocal"
		override fun getBaseUrl(): String = baseUrlLocal
		override fun getAppendedUrl(): String = appendedUrlLocal
	}

	@Suppress("MemberVisibilityCanBePrivate")
	object USER_AUTHORITY : Urls() {
		const val baseUrlLocal = "user-authority-service/api"
		const val appendedUrlLocal = "${API.baseUrlLocal}/$baseUrlLocal"
		override fun getBaseUrl(): String = baseUrlLocal
		override fun getAppendedUrl(): String = appendedUrlLocal
	}
}

interface UrlsGetFields {

	fun getBaseUrl(): String

	fun getAppendedUrl(): String = "${Urls.API.baseUrlLocal}/${getBaseUrl()}"

}
 */
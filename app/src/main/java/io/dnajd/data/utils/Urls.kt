package io.dnajd.data.utils

object Urls {

	//const val SERVER_URL: String = "http://192.168.0.150:8080/"

	const val SERVER_URL: String = "https://green-ape-13.telebit.io/"

	const val BASE: String = "${SERVER_URL}api/"
	const val PROJECT: String = BASE + "project/"
	const val PROJECT_TABLE: String = BASE + "project-table/"
	const val PROJECT_TABLE_ISSUE: String = BASE + "project-table-issue/"
	const val GOOGLE_AUTH: String = BASE + "google_auth/"
	const val JWT_REFRESH_AUTH: String = BASE + "jwt_refresh_auth/"
	const val PROJECT_AUTHORITY: String = BASE + "project-authority/"
	const val TASK_COMMENT: String = BASE + "issue_comment/"
	const val PROJECT_ICON: String = BASE + "project_icon/"
}

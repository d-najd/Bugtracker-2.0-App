package io.dnajd.data.utils


/**
 * enum is not used because it is 'not compile time constant' so I had to resort to using this, it is recommended to get the
 * fields using the interface [UrlsGetFields] where possible since it offers more flexibility
 */
@Suppress("ClassName")
sealed class Urls: UrlsGetFields {
    object API: Urls() {
        const val baseUrlLocal = "http://192.168.1.150:8080"
        const val appendedUrlLocal = "http://192.168.1.150:8080"
        override fun getBaseUrl(): String = baseUrlLocal
        override fun getAppendedUrl(): String = appendedUrlLocal
    }
    @Suppress("MemberVisibilityCanBePrivate")
    object PROJECT: Urls() {
        const val baseUrlLocal = "project-service/api"
        const val appendedUrlLocal = "${API.baseUrlLocal}/$baseUrlLocal"
        override fun getBaseUrl(): String = baseUrlLocal
        override fun getAppendedUrl(): String = appendedUrlLocal
    }
    @Suppress("MemberVisibilityCanBePrivate")
    object PROJECT_TABLE: Urls() {
        const val baseUrlLocal = "project-table-service/api"
        const val appendedUrlLocal = "${API.baseUrlLocal}/$baseUrlLocal"
        override fun getBaseUrl(): String = baseUrlLocal
        override fun getAppendedUrl(): String = appendedUrlLocal
    }
    @Suppress("MemberVisibilityCanBePrivate")
    object TABLE_TASK: Urls() {
        const val baseUrlLocal = "project-table-issue-service/api"
        const val appendedUrlLocal = "${API.baseUrlLocal}/$baseUrlLocal"
        override fun getBaseUrl(): String = baseUrlLocal
        override fun getAppendedUrl(): String = appendedUrlLocal
    }
    @Suppress("MemberVisibilityCanBePrivate")
    object USER_AUTHORITY: Urls() {
        const val baseUrlLocal = "user-authority-service/api"
        const val appendedUrlLocal = "${API.baseUrlLocal}/$baseUrlLocal"
        override fun getBaseUrl(): String = baseUrlLocal
        override fun getAppendedUrl(): String = appendedUrlLocal
    }
}

interface UrlsGetFields {

    fun getBaseUrl(): String

    fun getAppendedUrl(): String

}
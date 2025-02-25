package io.dnajd.domain.table_task.model


import com.google.gson.annotations.SerializedName

/**
 * TODO there is some refactoring to be done here and in couple other places with composition
 */
data class TableChildTask(
	@SerializedName("id") val id: Long = -1,
	@SerializedName("title") val title: String,
	@SerializedName("tableId") val tableId: Long,
)

fun TableChildTask.toBasic(): ProjectTableChildTaskBasic = ProjectTableChildTaskBasic(
	id = id,
)

/**
 * contains only the most basic of fields, this is used for items in the table which does not require
 * that much data
 */
data class ProjectTableChildTaskBasic(
	@SerializedName("id") val id: Long = -1,
)
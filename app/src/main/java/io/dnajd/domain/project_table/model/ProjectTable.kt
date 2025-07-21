package io.dnajd.domain.project_table.model


import com.google.gson.annotations.SerializedName
import io.dnajd.data.project_table.repository.ProjectTableRepository
import io.dnajd.data.table_task.repository.TableTaskRepository
import io.dnajd.domain.project.model.Project
import io.dnajd.domain.table_task.model.TableTask
import java.util.Objects

data class ProjectTableListResponse(
	@SerializedName("data") val `data`: List<ProjectTable> = emptyList(),
)

/**
 * Used primarily for networking
 */
data class ProjectTable(
	@SerializedName("id") val id: Long = -1L,
	@SerializedName("projectId") val projectId: Long = -1L,
	@SerializedName("title") val title: String = "",
	@SerializedName("position") val position: Int = -1,
	/**
	 * this is nullable for places where tasks accessing is illegal, for example
	 * [ProjectTableRepository]'s data shouldn't have tasks and [TableTaskRepository] should be used
	 * instead.
	 */
	@SerializedName("issues") val tasks: List<TableTask> = mutableListOf(),
) : java.io.Serializable {
	constructor(basicTable: ProjectTable) : this(
		id = basicTable.id,
		projectId = basicTable.projectId,
		title = basicTable.title,
		position = basicTable.position
	)
}

/**
 * Used primarily for managing state with repositories
 */
data class ProjectTableBasic(
	@SerializedName("id") val id: Long = -1L,
	@SerializedName("projectId") val projectId: Long = -1L,
	@SerializedName("title") val title: String = "",
	@SerializedName("position") val position: Int = -1,
) {
	constructor(table: ProjectTable) : this(
		id = table.id,
		projectId = table.projectId,
		title = table.title,
		position = table.position
	)

	override fun hashCode(): Int {
		return Objects.hash(id)
	}

	override fun equals(other: Any?): Boolean {
		if (other !is Project) return false

		if (id != other.id) return false

		return true
	}
}
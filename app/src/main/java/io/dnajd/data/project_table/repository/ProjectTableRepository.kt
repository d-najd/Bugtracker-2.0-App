package io.dnajd.data.project_table.repository

import io.dnajd.data.table_task.repository.TableTaskRepository
import io.dnajd.data.utils.RepositoryBase
import io.dnajd.domain.project_table.model.ProjectTable
import io.dnajd.domain.project_table.service.ProjectTableApiService
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.get

object ProjectTableRepository :
	RepositoryBase<Set<ProjectTable>, RepositoryBase.State<Set<ProjectTable>>>(State(emptySet())) {

	private val api: ProjectTableApiService = Injekt.get()

	/**
	 * @param fetchTasks if true the tasks will be fetched and the repository for tasks updated
	 */
	suspend fun fetchAllIfUninitialized(
		projectId: Long,
		forceFetch: Boolean = false,
		fetchTasks: Boolean = false,
	): Result<Unit> {
		if (!forceFetch && state.value.fetchedData) {
			return Result.success(Unit)
		}
		return api
			.getAllByProjectId(
				projectId,
				fetchTasks
			)
			.onSuccess {
				update(it.data.toSet())
				if (fetchTasks) {
					val tasks = it.data
						.flatMap { table -> table.tasks }
						.toSet()
					TableTaskRepository.update(tasks)
				}
			}
			.map { }
	}

	override fun update(
		data: Set<ProjectTable>,
		setDataFetched: Boolean,
	) {
		val dataWithoutTasks = data
			.map { it.copy(tasks = emptyList()) }
			.toSet()

		super.update(
			dataWithoutTasks,
			setDataFetched
		)
	}
}
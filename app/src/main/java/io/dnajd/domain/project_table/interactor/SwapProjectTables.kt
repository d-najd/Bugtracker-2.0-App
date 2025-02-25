package io.dnajd.domain.project_table.interactor

import io.dnajd.domain.project_table.service.ProjectTableRepository

class SwapProjectTables(
	private val repository: ProjectTableRepository,
) {
	suspend fun await(fId: Long, sId: Long): Boolean = repository.swapPositionWith(fId, sId)
}
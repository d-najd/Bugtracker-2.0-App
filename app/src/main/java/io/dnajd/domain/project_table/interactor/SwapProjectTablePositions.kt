package io.dnajd.domain.project_table.interactor

import io.dnajd.domain.project_table.service.ProjectTableRepository

class SwapProjectTablePositions(
    private val repository: ProjectTableRepository,
){
    suspend fun await(fId: Long, sId: Long): Boolean {
        return repository.swapTablePositions(fId, sId)
    }
}
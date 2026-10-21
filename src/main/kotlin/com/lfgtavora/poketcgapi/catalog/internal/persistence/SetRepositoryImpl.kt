package com.lfgtavora.poketcgapi.catalog.internal.persistence

import com.lfgtavora.poketcgapi.catalog.PageResult
import com.lfgtavora.poketcgapi.catalog.PaginationParams
import com.lfgtavora.poketcgapi.catalog.model.Set
import com.lfgtavora.poketcgapi.catalog.exception.SetNotFoundException
import com.lfgtavora.poketcgapi.catalog.internal.persistence.data.SetEntity
import com.lfgtavora.poketcgapi.catalog.internal.persistence.data.toDomain
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Repository

@Repository
class SetRepositoryImpl(
    private val setJpaRepository: SetJpaRepository
) : SetRepository {

    override fun findById(id: String): Set =
        setJpaRepository.findById(id)
            .orElseThrow { SetNotFoundException(id) }
            .toDomain()

    override fun findAll(query: String?, paginationParams: PaginationParams): PageResult<Set> =
        setJpaRepository.findAll(
            pageRequest(page = paginationParams.page, paginationParams.pageSize),
        ).toPageResult()


}

private fun pageRequest(page: Int, pageSize: Int) =
    PageRequest.of((page - 1).coerceAtLeast(0), pageSize)

private fun Page<SetEntity>.toPageResult() =
    PageResult(
        content = content.map(SetEntity::toDomain),
        page = number + 1,
        size = size,
        totalPages = totalPages,
        totalElements = totalElements,
    )
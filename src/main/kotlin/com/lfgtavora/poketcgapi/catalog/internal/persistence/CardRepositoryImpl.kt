package com.lfgtavora.poketcgapi.catalog.internal.persistence

import com.lfgtavora.poketcgapi.catalog.model.Card
import com.lfgtavora.poketcgapi.catalog.exception.CardNotFoundException
import com.lfgtavora.poketcgapi.catalog.PageResult
import com.lfgtavora.poketcgapi.catalog.internal.persistence.data.CardEntity
import com.lfgtavora.poketcgapi.catalog.internal.persistence.data.toDomain
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Repository

@Repository
class CardRepositoryImpl(
    private val database: CardJpaRepository,
) : CardRepository {

    override fun findById(id: String): Card =
        database.findById(id)
            .orElseThrow { CardNotFoundException(id) }
            .toDomain()

    override fun findAll(
        q: String?, //TODO impl query constraints
        page: Int,
        pageSize: Int,
        orderBy: String?,
        select: String?,
    ): PageResult<Card> =
        database.findAll(pageRequest(page, pageSize)).toPageResult()

    override fun findByNameContainingIgnoreCase(
        name: String,
        page: Int,
        pageSize: Int,
        orderBy: String?,
        select: String?,
    ): PageResult<Card> =
        database.findByNameContainingIgnoreCase(name, pageRequest(page, pageSize)).toPageResult()

    private fun pageRequest(page: Int, pageSize: Int) =
        PageRequest.of((page - 1).coerceAtLeast(0), pageSize)

    private fun Page<CardEntity>.toPageResult() =
        PageResult(
            content = content.map(CardEntity::toDomain),
            page = number + 1,
            size = size,
            totalPages = totalPages,
            totalElements = totalElements,
        )
}

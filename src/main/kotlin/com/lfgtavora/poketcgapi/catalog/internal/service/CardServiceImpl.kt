package com.lfgtavora.poketcgapi.catalog.internal.service

import com.lfgtavora.poketcgapi.catalog.model.Card
import com.lfgtavora.poketcgapi.catalog.CardService
import com.lfgtavora.poketcgapi.catalog.PageResult
import com.lfgtavora.poketcgapi.catalog.PaginationParams
import com.lfgtavora.poketcgapi.catalog.internal.persistence.CardRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class CardServiceImpl(
    private val cardRepository: CardRepository,
) : CardService {

    override fun findById(id: String) =
        cardRepository.findById(id)

    override fun findAll(
        query: String?,
        paginationParams: PaginationParams
    ): PageResult<Card> = cardRepository.findAll(
        query,
        paginationParams.page,
        paginationParams.pageSize,
        paginationParams.orderBy,
        paginationParams.select
    )

    override fun findByNameContainingIgnoreCase(
        name: String,
        paginationParams: PaginationParams
    ): PageResult<Card> =
        cardRepository.findByNameContainingIgnoreCase(
            name,
            paginationParams.page,
            paginationParams.pageSize,
            paginationParams.orderBy,
            paginationParams.select
        )

}
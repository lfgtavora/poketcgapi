package com.lfgtavora.poketcgapi.catalog

import com.lfgtavora.poketcgapi.catalog.model.Card

interface CardService {
    fun findById(id: String): Card
    fun findAll(query: String?, paginationParams: PaginationParams): PageResult<Card>
    fun findByNameContainingIgnoreCase(
        name: String,
        paginationParams: PaginationParams
    ): PageResult<Card>
}

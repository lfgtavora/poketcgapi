package com.lfgtavora.poketcgapi.catalog

import com.lfgtavora.poketcgapi.catalog.model.Set

interface SetService {
    fun findAll(query: String?, paginationParams: PaginationParams): PageResult<Set>
    fun findById(id: String): Set
}
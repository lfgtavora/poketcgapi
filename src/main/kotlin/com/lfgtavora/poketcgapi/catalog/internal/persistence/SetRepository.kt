package com.lfgtavora.poketcgapi.catalog.internal.persistence

import com.lfgtavora.poketcgapi.catalog.PageResult
import com.lfgtavora.poketcgapi.catalog.PaginationParams
import com.lfgtavora.poketcgapi.catalog.model.Set

interface SetRepository {
    fun findAll(query: String?, paginationParams: PaginationParams): PageResult<Set>
    fun findById(id: String): Set
}
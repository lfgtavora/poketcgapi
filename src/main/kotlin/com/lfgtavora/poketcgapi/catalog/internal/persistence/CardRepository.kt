package com.lfgtavora.poketcgapi.catalog.internal.persistence

import com.lfgtavora.poketcgapi.catalog.model.Card
import com.lfgtavora.poketcgapi.catalog.PageResult

interface CardRepository {
    fun findById(id: String): Card
    fun findAll(q: String?, page: Int, pageSize: Int, orderBy: String?, select: String?): PageResult<Card>
    fun findByNameContainingIgnoreCase(
        name: String,
        page: Int,
        pageSize: Int,
        orderBy: String?,
        select: String?
    ): PageResult<Card>

}
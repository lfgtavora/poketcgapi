package com.lfgtavora.poketcgapi.catalog.internal.service

import com.lfgtavora.poketcgapi.catalog.PageResult
import com.lfgtavora.poketcgapi.catalog.PaginationParams
import com.lfgtavora.poketcgapi.catalog.model.Set
import com.lfgtavora.poketcgapi.catalog.SetService
import com.lfgtavora.poketcgapi.catalog.internal.persistence.SetRepository
import org.springframework.stereotype.Service

@Service
class SetServiceImpl(
    private val setRepository: SetRepository
): SetService {

    override fun findAll(
        query: String?,
        paginationParams: PaginationParams
    ): PageResult<Set> =
        setRepository.findAll(query, paginationParams)

    override fun findById(id: String): Set =
        setRepository.findById(id)

}
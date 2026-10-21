package com.lfgtavora.poketcgapi.catalog.internal.persistence

import com.lfgtavora.poketcgapi.catalog.internal.persistence.data.CardEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface CardJpaRepository : JpaRepository<CardEntity, String> {
    fun findByNameContainingIgnoreCase(name: String, pageable: Pageable): Page<CardEntity>
    fun findBySetId(setId: String): List<CardEntity>
}

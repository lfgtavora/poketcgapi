package com.lfgtavora.poketcgapi.catalog.internal.persistence

import com.lfgtavora.poketcgapi.catalog.internal.persistence.data.SetEntity
import org.springframework.data.jpa.repository.JpaRepository

interface SetJpaRepository : JpaRepository<SetEntity, String>

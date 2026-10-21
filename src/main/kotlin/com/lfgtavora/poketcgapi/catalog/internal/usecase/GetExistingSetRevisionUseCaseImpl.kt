package com.lfgtavora.poketcgapi.catalog.internal.usecase

import com.lfgtavora.poketcgapi.catalog.GetExistingSetRevisionUseCase
import com.lfgtavora.poketcgapi.catalog.internal.persistence.SetJpaRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class GetExistingSetRevisionUseCaseImpl(
    private val setRepository: SetJpaRepository
) : GetExistingSetRevisionUseCase {

    @Transactional(readOnly = true)
    override fun invoke(): Map<String, String> =
        setRepository.findAll().associate { it.id to it.updatedAt }
}

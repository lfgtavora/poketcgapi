package com.lfgtavora.poketcgapi.catalog.internal.usecase

import com.lfgtavora.poketcgapi.catalog.model.Card
import com.lfgtavora.poketcgapi.catalog.model.Set
import com.lfgtavora.poketcgapi.catalog.ReplaceSetUseCase
import com.lfgtavora.poketcgapi.catalog.internal.persistence.CardJpaRepository
import com.lfgtavora.poketcgapi.catalog.internal.persistence.SetJpaRepository
import com.lfgtavora.poketcgapi.catalog.internal.persistence.data.copyFrom
import com.lfgtavora.poketcgapi.catalog.internal.persistence.data.toEntity
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ReplaceSetUseCaseImpl(
    private val cardRepository: CardJpaRepository,
    private val setRepository: SetJpaRepository
): ReplaceSetUseCase {

    @Transactional
    override fun invoke(set: Set, cards: List<Card>) {
        val setEntity = setRepository.findById(set.id)
            .map { it.apply { copyFrom(set) } }
            .orElseGet { set.toEntity() }
            .let { setRepository.saveAndFlush(it) }

        val existingCards = cardRepository.findBySetId(set.id)
        if (existingCards.isNotEmpty()) {
            cardRepository.deleteAll(existingCards)
            cardRepository.flush()
        }

        if (cards.isNotEmpty()) {
            cardRepository.saveAll(cards.map { it.toEntity(setEntity) })
        }
    }
}
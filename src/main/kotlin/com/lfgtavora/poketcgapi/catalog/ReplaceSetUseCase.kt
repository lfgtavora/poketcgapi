package com.lfgtavora.poketcgapi.catalog

import com.lfgtavora.poketcgapi.catalog.model.Card
import com.lfgtavora.poketcgapi.catalog.model.Set

interface ReplaceSetUseCase {
    operator fun invoke(set: Set, cards: List<Card>)
}
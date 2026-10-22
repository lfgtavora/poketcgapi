package com.lfgtavora.poketcgapi.catalog

import com.lfgtavora.poketcgapi.catalog.exception.CardNotFoundException
import com.lfgtavora.poketcgapi.catalog.exception.SetNotFoundException
import com.lfgtavora.poketcgapi.catalog.model.Card
import com.lfgtavora.poketcgapi.catalog.model.CardImages
import com.lfgtavora.poketcgapi.catalog.model.Legalities
import com.lfgtavora.poketcgapi.catalog.model.Set
import com.lfgtavora.poketcgapi.catalog.model.SetImages

class FakeCardService(cards: List<Card> = emptyList()) : CardService {
    private val cards = cards.associateBy { it.id }

    var lastQuery: String? = null
    var lastPagination: PaginationParams? = null

    override fun findById(id: String): Card = cards[id] ?: throw CardNotFoundException(id)

    override fun findAll(query: String?, paginationParams: PaginationParams): PageResult<Card> {
        lastQuery = query
        lastPagination = paginationParams
        return cards.values.toList().paged(paginationParams)
    }

    override fun findByNameContainingIgnoreCase(
        name: String,
        paginationParams: PaginationParams,
    ): PageResult<Card> {
        lastPagination = paginationParams
        return cards.values.filter { it.name.contains(name, ignoreCase = true) }.paged(paginationParams)
    }
}

class FakeSetService(sets: List<Set> = emptyList()) : SetService {
    private val sets = sets.associateBy { it.id }

    var lastQuery: String? = null
    var lastPagination: PaginationParams? = null

    override fun findAll(query: String?, paginationParams: PaginationParams): PageResult<Set> {
        lastQuery = query
        lastPagination = paginationParams
        return sets.values.toList().paged(paginationParams)
    }

    override fun findById(id: String): Set = sets[id] ?: throw SetNotFoundException(id)
}

/** In-memory stand-in for the catalog side of the sync flow. */
class FakeSetStore : ReplaceSetUseCase, GetExistingSetRevisionUseCase {
    val sets = linkedMapOf<String, Set>()
    val cardsBySetId = linkedMapOf<String, List<Card>>()
    val replacedSetIds = mutableListOf<String>()
    val failOnSetIds = mutableSetOf<String>()

    override fun invoke(set: Set, cards: List<Card>) {
        check(set.id !in failOnSetIds) { "Simulated persistence failure for ${set.id}" }
        sets[set.id] = set
        cardsBySetId[set.id] = cards
        replacedSetIds += set.id
    }

    override fun invoke(): Map<String, String> = sets.mapValues { it.value.updatedAt }
}

private fun <T> List<T>.paged(pagination: PaginationParams): PageResult<T> {
    val from = ((pagination.page - 1) * pagination.pageSize).coerceAtMost(size)
    val to = (from + pagination.pageSize).coerceAtMost(size)
    return PageResult(
        content = subList(from, to),
        page = pagination.page,
        size = pagination.pageSize,
        totalElements = size.toLong(),
        totalPages = (size + pagination.pageSize - 1) / pagination.pageSize,
    )
}

fun sampleSet(id: String = "base1", updatedAt: String = "2022/10/10 15:12:00") = Set(
    id = id,
    name = "Set $id",
    series = "Base",
    printedTotal = 102,
    total = 102,
    legalities = Legalities(unlimited = "Legal"),
    releaseDate = "1999/01/09",
    updatedAt = updatedAt,
    images = SetImages(symbol = "symbol", logo = "logo"),
)

fun sampleCard(id: String = "base1-4", name: String = "Charizard", set: Set = sampleSet()) = Card(
    id = id,
    name = name,
    supertype = "Pokémon",
    set = set,
    number = id.substringAfter('-'),
    legalities = Legalities(unlimited = "Legal"),
    images = CardImages(small = "small", large = "large"),
)

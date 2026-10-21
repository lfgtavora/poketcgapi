package com.lfgtavora.poketcgapi.sync.internal

import com.lfgtavora.poketcgapi.catalog.model.Card
import com.lfgtavora.poketcgapi.catalog.model.Set
import com.lfgtavora.poketcgapi.catalog.GetExistingSetRevisionUseCase
import com.lfgtavora.poketcgapi.catalog.ReplaceSetUseCase
import com.lfgtavora.poketcgapi.sync.internal.config.SyncProperties
import com.lfgtavora.poketcgapi.sync.internal.mapper.toDomain
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class TcgDataSyncService(
    private val client: TcgDataClient,
    private val getExistingSetRevisionUseCase: GetExistingSetRevisionUseCase,
    private val replaceSetUseCase: ReplaceSetUseCase,
    private val properties: SyncProperties,
) {
    private val log = LoggerFactory.getLogger(javaClass)

    fun sync() {
        val revisions = getExistingSetRevisionUseCase()
        if (revisions.isEmpty()) {
            coldStart()
        } else {
            incremental(revisions)
        }
    }

    private fun coldStart() {
        log.info("H2 catalog empty — cold-starting from zip archive")
        val archive = client.downloadArchive()
        log.info("Archive contains {} sets", archive.sets.size)
        archive.sets.forEach { setJson ->
            val set = setJson.toDomain()
            val cards = archive.cardsBySetId[set.id].orEmpty().map { it.toDomain(set) }
            if (cards.isEmpty()) {
                log.warn("No cards in archive for set {}", set.id)
            }
            replaceSafely(set, cards)
        }
    }

    internal fun incremental(revisions: Map<String, String>) {
        val remoteSets = client.fetchSets()
        val stale = remoteSets.filter { revisions[it.id] != it.updatedAt }
        log.info(
            "Incremental sync: {} remote sets, {} stale/new",
            remoteSets.size,
            stale.size,
        )
        stale.forEach { setJson ->
            val set = setJson.toDomain()
            val cards = client.fetchCards(set.id).map { it.toDomain(set) }
            replaceSafely(set, cards)
        }
    }

    private fun replaceSafely(set: Set, cards: List<Card>) {
        try {
            replaceSetUseCase(set, cards)
            log.info("Synced set {} ({} cards)", set.id, cards.size)
        } catch (e: Exception) {
            if (properties.failOnError) throw e
            log.error("Failed to persist set {}", set.id, e)
        }
    }
}

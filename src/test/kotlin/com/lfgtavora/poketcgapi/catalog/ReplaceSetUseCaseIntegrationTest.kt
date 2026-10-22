package com.lfgtavora.poketcgapi.catalog

import com.lfgtavora.poketcgapi.TestcontainersConfiguration
import com.lfgtavora.poketcgapi.catalog.exception.CardNotFoundException
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertFailsWith

@SpringBootTest
@Import(TestcontainersConfiguration::class)
class ReplaceSetUseCaseIntegrationTest(
    @Autowired private val replaceSetUseCase: ReplaceSetUseCase,
    @Autowired private val getRevisions: GetExistingSetRevisionUseCase,
    @Autowired private val cardService: CardService
) {

    @Test
    fun `inserts a new set with its cards`() {
        val set = sampleSet("it-insert", updatedAt = "2024/01/01 10:00:00")

        replaceSetUseCase(
            set,
            listOf(
                sampleCard("it-insert-1", "Charizard", set),
                sampleCard("it-insert-2", "Pikachu", set),
            ),
        )

        assertEquals("2024/01/01 10:00:00", getRevisions()["it-insert"])
        assertEquals("Charizard", cardService.findById("it-insert-1").name)
        assertEquals("it-insert", cardService.findById("it-insert-2").set.id)
    }

    @Test
    fun `replaces the cards of an existing set`() {
        val old = sampleSet("it-replace", updatedAt = "2024/01/01 10:00:00")
        replaceSetUseCase(old, listOf(sampleCard("it-replace-1", set = old)))

        val updated = sampleSet("it-replace", updatedAt = "2024/02/01 10:00:00")
        replaceSetUseCase(updated, listOf(sampleCard("it-replace-2", set = updated)))

        assertEquals("2024/02/01 10:00:00", getRevisions()["it-replace"])
        assertFailsWith<CardNotFoundException> { cardService.findById("it-replace-1") }
        assertEquals("it-replace-2", cardService.findById("it-replace-2").id)
    }

    @Test
    fun `rolls back the whole replacement when a card is invalid`() {
        val old = sampleSet("it-rollback", updatedAt = "2024/01/01 10:00:00")
        replaceSetUseCase(old, listOf(sampleCard("it-rollback-1", set = old)))

        val updated = sampleSet("it-rollback", updatedAt = "2024/02/01 10:00:00")
        val tooLongName = sampleCard("it-rollback-2", name = "x".repeat(300), set = updated)
        assertFails { replaceSetUseCase(updated, listOf(tooLongName)) }

        assertEquals("2024/01/01 10:00:00", getRevisions()["it-rollback"])
        assertEquals("it-rollback-1", cardService.findById("it-rollback-1").id)
    }
}

package com.lfgtavora.poketcgapi.sync.internal

import com.lfgtavora.poketcgapi.catalog.GetExistingSetRevisionUseCase
import com.lfgtavora.poketcgapi.catalog.ReplaceSetUseCase
import com.lfgtavora.poketcgapi.sync.internal.config.SyncProperties
import com.lfgtavora.poketcgapi.sync.internal.mapper.toDomain
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito.never
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class TcgDataSyncServiceTest {

    @Mock
    private lateinit var client: TcgDataClient

    @Mock
    private lateinit var getExistingSetRevisionUseCase: GetExistingSetRevisionUseCase

    @Mock
    private lateinit var replaceSetUseCase: ReplaceSetUseCase

    private val properties = SyncProperties(failOnError = true)

    private val service by lazy {
        TcgDataSyncService(
            client,
            getExistingSetRevisionUseCase,
            replaceSetUseCase,
            properties
        )
    }

    @Test
    fun `skips sets whose updatedAt matches`() {
        val remote = sampleGithubSet(updatedAt = "2022/10/10 15:12:00")
        `when`(getExistingSetRevisionUseCase()).thenReturn(mapOf("base1" to "2022/10/10 15:12:00"))
        `when`(client.fetchSets()).thenReturn(listOf(remote))

        service.sync()

        verify(client, never()).fetchCards(anyNonNull())
        verify(replaceSetUseCase, never()).invoke(anyNonNull(), anyNonNull())
    }

    @Test
    fun `replaces sets when updatedAt differs`() {
        val remote = sampleGithubSet(updatedAt = "2026/01/01 00:00:00")
        val card = sampleGithubCard()
        `when`(getExistingSetRevisionUseCase()).thenReturn(mapOf("base1" to "2022/10/10 15:12:00"))
        `when`(client.fetchSets()).thenReturn(listOf(remote))
        `when`(client.fetchCards("base1")).thenReturn(listOf(card))

        service.sync()

        val set = remote.toDomain()
        verify(client).fetchCards("base1")
        verify(replaceSetUseCase).invoke(eqNonNull(set), eqNonNull(listOf(card.toDomain(set))))
    }

    @Test
    fun `cold start uses zip archive`() {
        val remote = sampleGithubSet()
        val card = sampleGithubCard()
        `when`(getExistingSetRevisionUseCase()).thenReturn(emptyMap())
        `when`(client.downloadArchive()).thenReturn(
            TcgDataArchive(
                sets = listOf(remote),
                cardsBySetId = mapOf("base1" to listOf(card)),
            ),
        )

        service.sync()

        val set = remote.toDomain()
        verify(client).downloadArchive()
        verify(client, never()).fetchSets()
        verify(replaceSetUseCase)(eqNonNull(set), eqNonNull(listOf(card.toDomain(set))))
    }
}

@Suppress("UNCHECKED_CAST")
private fun <T> anyNonNull(): T = ArgumentMatchers.any() as T

private fun <T> eqNonNull(value: T): T = ArgumentMatchers.eq(value) ?: value

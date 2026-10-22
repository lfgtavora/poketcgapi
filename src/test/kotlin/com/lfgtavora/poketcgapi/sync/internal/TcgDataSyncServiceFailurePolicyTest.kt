package com.lfgtavora.poketcgapi.sync.internal

import com.lfgtavora.poketcgapi.catalog.FakeSetStore
import com.lfgtavora.poketcgapi.catalog.sampleSet
import com.lfgtavora.poketcgapi.sync.internal.config.SyncProperties
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class TcgDataSyncServiceFailurePolicyTest {

    private val client = mock(TcgDataClient::class.java)
    private val store = FakeSetStore()

    private fun serviceWith(failOnError: Boolean) =
        TcgDataSyncService(client, store, store, SyncProperties(failOnError = failOnError))

    private fun archiveOf(vararg setIds: String) = TcgDataArchive(
        sets = setIds.map { sampleGithubSet(id = it) },
        cardsBySetId = setIds.associateWith { listOf(sampleGithubCard()) },
    )

    @Test
    fun `cold start keeps syncing the other sets when one fails and failOnError is false`() {
        `when`(client.downloadArchive()).thenReturn(archiveOf("base1", "base2"))
        store.failOnSetIds += "base1"

        serviceWith(failOnError = false).sync()

        assertEquals(listOf("base2"), store.replacedSetIds)
    }

    @Test
    fun `cold start propagates the failure when failOnError is true`() {
        `when`(client.downloadArchive()).thenReturn(archiveOf("base1", "base2"))
        store.failOnSetIds += "base1"

        assertFailsWith<IllegalStateException> { serviceWith(failOnError = true).sync() }

        assertEquals(emptyList(), store.replacedSetIds)
    }

    @Test
    fun `incremental sync only replaces new or stale sets`() {
        store.invoke(sampleSet("base1", updatedAt = "2022/10/10 15:12:00"), emptyList())
        store.replacedSetIds.clear()
        `when`(client.fetchSets()).thenReturn(
            listOf(
                sampleGithubSet(id = "base1", updatedAt = "2022/10/10 15:12:00"),
                sampleGithubSet(id = "base2", updatedAt = "2024/01/01 00:00:00"),
            ),
        )
        `when`(client.fetchCards("base2")).thenReturn(listOf(sampleGithubCard()))

        serviceWith(failOnError = true).sync()

        assertEquals(listOf("base2"), store.replacedSetIds)
        assertEquals("2024/01/01 00:00:00", store.sets["base2"]?.updatedAt)
    }

    @Test
    fun `incremental sync replaces a set whose updatedAt changed`() {
        store.invoke(sampleSet("base1", updatedAt = "2022/10/10 15:12:00"), emptyList())
        store.replacedSetIds.clear()
        `when`(client.fetchSets()).thenReturn(listOf(sampleGithubSet(id = "base1", updatedAt = "2026/01/01 00:00:00")))
        `when`(client.fetchCards("base1")).thenReturn(listOf(sampleGithubCard()))

        serviceWith(failOnError = true).sync()

        assertEquals(listOf("base1"), store.replacedSetIds)
        assertEquals("2026/01/01 00:00:00", store.sets["base1"]?.updatedAt)
    }
}

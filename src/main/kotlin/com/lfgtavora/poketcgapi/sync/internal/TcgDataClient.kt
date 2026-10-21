package com.lfgtavora.poketcgapi.sync.internal

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.lfgtavora.poketcgapi.sync.internal.config.SyncProperties
import com.lfgtavora.poketcgapi.sync.internal.json.GithubCardJson
import com.lfgtavora.poketcgapi.sync.internal.json.GithubSetJson
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient
import java.io.ByteArrayInputStream
import java.net.URI
import java.util.zip.ZipInputStream

data class TcgDataArchive(
    val sets: List<GithubSetJson>,
    val cardsBySetId: Map<String, List<GithubCardJson>>,
)

@Component
class TcgDataClient(
    private val properties: SyncProperties,
) {
    private val log = LoggerFactory.getLogger(javaClass)
    private val objectMapper = jacksonObjectMapper()
    private val restClient = RestClient.builder()
        .defaultHeader(HttpHeaders.USER_AGENT, "poketcgapi")
        .build()

    fun fetchSets(): List<GithubSetJson> {
        val json = getString("${properties.baseUrl.trimEnd('/')}/sets/en.json")
        return objectMapper.readValue(json, SETS_TYPE)
    }

    fun fetchCards(setId: String): List<GithubCardJson> {
        val json = getString("${properties.baseUrl.trimEnd('/')}/cards/en/$setId.json")
        return objectMapper.readValue(json, CARDS_TYPE)
    }

    fun downloadArchive(): TcgDataArchive {
        log.info("Downloading TCG data archive from {}", properties.zipUrl)
        val bytes = downloadBytes(properties.zipUrl)
        if (bytes.isEmpty()) error("Empty archive payload from ${properties.zipUrl}")
        return parseArchive(bytes)
    }

    private fun downloadBytes(url: String, redirectsLeft: Int = 5): ByteArray {
        val response = restClient.get()
            .uri(url)
            .retrieve()
            .toEntity(ByteArray::class.java)

        val location = response.headers.location
        if (response.statusCode.is3xxRedirection && location != null && redirectsLeft > 0) {
            val next = if (location.isAbsolute) location.toString() else URI.create(url).resolve(location).toString()
            log.info("Following archive redirect to {}", next)
            return downloadBytes(next, redirectsLeft - 1)
        }

        return response.body
            ?: error("Empty archive payload from $url (HTTP ${response.statusCode.value()})")
    }

    internal fun parseArchive(bytes: ByteArray): TcgDataArchive {
        val sets = mutableListOf<GithubSetJson>()
        val cardsBySetId = mutableMapOf<String, List<GithubCardJson>>()
        val cardFile = Regex("""cards/en/([^/]+)\.json$""")

        ZipInputStream(ByteArrayInputStream(bytes)).use { zip ->
            generateSequence { zip.nextEntry }.forEach { entry ->
                if (entry.isDirectory) return@forEach
                val name = entry.name
                when {
                    name.endsWith("sets/en.json") ->
                        sets += objectMapper.readValue(zip.readBytes(), SETS_TYPE)
                    else -> {
                        val match = cardFile.find(name) ?: return@forEach
                        cardsBySetId[match.groupValues[1]] =
                            objectMapper.readValue(zip.readBytes(), CARDS_TYPE)
                    }
                }
            }
        }

        return TcgDataArchive(sets = sets, cardsBySetId = cardsBySetId)
    }

    private fun getString(url: String): String =
        restClient.get()
            .uri(url)
            .retrieve()
            .body(String::class.java)
            ?: error("Empty payload from $url")

    private companion object {
        val SETS_TYPE = object : TypeReference<List<GithubSetJson>>() {}
        val CARDS_TYPE = object : TypeReference<List<GithubCardJson>>() {}
    }
}

package com.lfgtavora.poketcgapi.sync.internal

import com.lfgtavora.poketcgapi.sync.internal.config.SyncProperties
import com.sun.net.httpserver.HttpServer
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.io.ByteArrayOutputStream
import java.net.InetSocketAddress
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

class TcgDataClientTest {

    private val sampleZip = zipOf(
        "pokemon-tcg-data-master/sets/en.json" to """
            [{"id":"base1","name":"Base","series":"Base","printedTotal":102,"total":102,
              "legalities":{"unlimited":"Legal"},"ptcgoCode":"BS","releaseDate":"1999/01/09",
              "updatedAt":"2022/10/10 15:12:00",
              "images":{"symbol":"https://s","logo":"https://l"}}]
        """.trimIndent(),
        "pokemon-tcg-data-master/cards/en/base1.json" to """
            [{"id":"base1-1","name":"Alakazam","supertype":"Pokémon","number":"1",
              "images":{"small":"https://s","large":"https://l"}}]
        """.trimIndent(),
    )

    @Test
    fun `parses sets and cards from github-style zip layout`() {
        val archive = TcgDataClient(SyncProperties()).parseArchive(sampleZip)

        assertThat(archive.sets).hasSize(1)
        assertThat(archive.sets[0].id).isEqualTo("base1")
        assertThat(archive.cardsBySetId["base1"]).hasSize(1)
        assertThat(archive.cardsBySetId["base1"]!![0].name).isEqualTo("Alakazam")
    }

    @Test
    fun `follows empty 302 to the zip like github archive urls`() {
        val server = HttpServer.create(InetSocketAddress(0), 0)
        val zip = sampleZip
        server.createContext("/archive.zip") { exchange ->
            exchange.responseHeaders.add(
                "Location",
                "http://localhost:${server.address.port}/file.zip",
            )
            exchange.sendResponseHeaders(302, -1)
            exchange.close()
        }
        server.createContext("/file.zip") { exchange ->
            exchange.responseHeaders.add("Content-Type", "application/zip")
            exchange.sendResponseHeaders(200, zip.size.toLong())
            exchange.responseBody.write(zip)
            exchange.close()
        }
        server.start()
        try {
            val client = TcgDataClient(
                SyncProperties(zipUrl = "http://localhost:${server.address.port}/archive.zip"),
            )
            val archive = client.downloadArchive()
            assertThat(archive.sets[0].id).isEqualTo("base1")
            assertThat(archive.cardsBySetId["base1"]!![0].name).isEqualTo("Alakazam")
        } finally {
            server.stop(0)
        }
    }

    private fun zipOf(vararg files: Pair<String, String>): ByteArray {
        val out = ByteArrayOutputStream()
        ZipOutputStream(out).use { zip ->
            files.forEach { (name, content) ->
                zip.putNextEntry(ZipEntry(name))
                zip.write(content.toByteArray())
                zip.closeEntry()
            }
        }
        return out.toByteArray()
    }
}

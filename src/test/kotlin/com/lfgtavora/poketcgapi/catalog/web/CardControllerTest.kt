package com.lfgtavora.poketcgapi.catalog.web

import com.lfgtavora.poketcgapi.GlobalExceptionHandler
import com.lfgtavora.poketcgapi.catalog.FakeCardService
import com.lfgtavora.poketcgapi.catalog.sampleCard
import org.junit.jupiter.api.Test
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import kotlin.test.assertEquals

class CardControllerTest {

    private val cards = FakeCardService(
        listOf(
            sampleCard("base1-4", "Charizard"),
            sampleCard("base1-5", "Charmander"),
            sampleCard("base1-6", "Pikachu"),
        ),
    )

    private val mvc = MockMvcBuilders
        .standaloneSetup(CardController(cards))
        .setControllerAdvice(GlobalExceptionHandler())
        .build()

    @Test
    fun `returns a card by id`() {
        mvc.get("/api/cards/base1-4").andExpect {
            status { isOk() }
            jsonPath("$.id") { value("base1-4") }
            jsonPath("$.name") { value("Charizard") }
        }
    }

    @Test
    fun `returns 404 with an error body for an unknown card`() {
        mvc.get("/api/cards/xyz").andExpect {
            status { isNotFound() }
            jsonPath("$.error") { value("Card with id `xyz` not found.") }
        }
    }

    @Test
    fun `lists cards with default pagination`() {
        mvc.get("/api/cards").andExpect {
            status { isOk() }
            jsonPath("$.content.length()") { value(3) }
            jsonPath("$.page") { value(1) }
            jsonPath("$.size") { value(250) }
            jsonPath("$.totalElements") { value(3) }
            jsonPath("$.totalPages") { value(1) }
        }
    }

    @Test
    fun `passes query and pagination parameters to the service`() {
        mvc.get("/api/cards?q=name:char&page=2&pageSize=1&orderBy=name&select=id")

        assertEquals("name:char", cards.lastQuery)
        val pagination = cards.lastPagination!!
        assertEquals(2, pagination.page)
        assertEquals(1, pagination.pageSize)
        assertEquals("name", pagination.orderBy)
        assertEquals("id", pagination.select)
    }

    @Test
    fun `rejects a page size above the maximum`() {
        mvc.get("/api/cards?pageSize=251").andExpect {
            status { isBadRequest() }
            jsonPath("$.error") { value("Validation failed") }
            jsonPath("$.details.pageSize") { exists() }
        }
    }

    @Test
    fun `rejects a page below 1`() {
        mvc.get("/api/cards?page=0").andExpect {
            status { isBadRequest() }
            jsonPath("$.details.page") { exists() }
        }
    }

    @Test
    fun `searches cards by name ignoring case`() {
        mvc.get("/api/cards/search?name=CHAR&pageSize=10").andExpect {
            status { isOk() }
            jsonPath("$.content.length()") { value(2) }
            jsonPath("$.totalElements") { value(2) }
        }
    }

    @Test
    fun `requires the name parameter on search`() {
        mvc.get("/api/cards/search").andExpect {
            status { isBadRequest() }
        }
    }
}

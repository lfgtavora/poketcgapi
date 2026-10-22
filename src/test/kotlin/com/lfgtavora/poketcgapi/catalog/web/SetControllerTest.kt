package com.lfgtavora.poketcgapi.catalog.web

import com.lfgtavora.poketcgapi.GlobalExceptionHandler
import com.lfgtavora.poketcgapi.catalog.FakeSetService
import com.lfgtavora.poketcgapi.catalog.sampleSet
import org.junit.jupiter.api.Test
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.setup.MockMvcBuilders

class SetControllerTest {

    private val sets = FakeSetService(listOf(sampleSet("base1"), sampleSet("base2"), sampleSet("jungle")))

    private val mvc = MockMvcBuilders
        .standaloneSetup(SetController(sets))
        .setControllerAdvice(GlobalExceptionHandler())
        .build()

    @Test
    fun `returns a set by id`() {
        mvc.get("/api/sets/base1").andExpect {
            status { isOk() }
            jsonPath("$.id") { value("base1") }
            jsonPath("$.series") { value("Base") }
        }
    }

    @Test
    fun `returns 404 with an error body for an unknown set`() {
        mvc.get("/api/sets/nope").andExpect {
            status { isNotFound() }
            jsonPath("$.error") { value("Set with id `nope` not found.") }
        }
    }

    @Test
    fun `lists sets page by page`() {
        mvc.get("/api/sets?page=2&pageSize=2").andExpect {
            status { isOk() }
            jsonPath("$.content.length()") { value(1) }
            jsonPath("$.content[0].id") { value("jungle") }
            jsonPath("$.page") { value(2) }
            jsonPath("$.totalElements") { value(3) }
            jsonPath("$.totalPages") { value(2) }
        }
    }

    @Test
    fun `rejects a page size of zero`() {
        mvc.get("/api/sets?pageSize=0").andExpect {
            status { isBadRequest() }
            jsonPath("$.details.pageSize") { exists() }
        }
    }
}

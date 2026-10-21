package com.lfgtavora.poketcgapi

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.modulith.core.ApplicationModules
import org.springframework.modulith.docs.Documenter

class ModularityTests {

    private val modules = ApplicationModules.of(PoketcgapiApplication::class.java)

    @Test
    fun `detects catalog module and verifies structure`() {
        assertThat(modules.stream().map { it.identifier.toString() })
            .containsExactlyInAnyOrder("catalog", "sync")

        modules.verify()
    }

    @Test
    fun `writes module documentation`() {
        Documenter(modules)
            .writeModulesAsPlantUml()
            .writeIndividualModulesAsPlantUml()
    }
}

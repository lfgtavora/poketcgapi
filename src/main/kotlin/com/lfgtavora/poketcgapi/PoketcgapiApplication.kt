package com.lfgtavora.poketcgapi

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Info
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
@OpenAPIDefinition(
    info = Info(
        title = "Poke TCG API",
        version = "0.0.1",
        description = "Pokémon Trading Card Game catalog",
    ),
)
class PoketcgapiApplication

fun main(args: Array<String>) {
    runApplication<PoketcgapiApplication>(*args)
}

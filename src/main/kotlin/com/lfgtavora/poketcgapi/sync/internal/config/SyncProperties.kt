package com.lfgtavora.poketcgapi.sync.internal.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "poketcg.sync")
data class SyncProperties(
    val enabled: Boolean = true,
    val baseUrl: String = "https://cdn.jsdelivr.net/gh/PokemonTCG/pokemon-tcg-data@master",
    val zipUrl: String = "https://codeload.github.com/PokemonTCG/pokemon-tcg-data/zip/refs/heads/master",
    val failOnError: Boolean = false,
)
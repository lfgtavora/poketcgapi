package com.lfgtavora.poketcgapi.sync.internal.json

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class GithubSetJson(
    val id: String,
    val name: String,
    val series: String,
    val printedTotal: Int,
    val total: Int,
    val legalities: GithubLegalitiesJson = GithubLegalitiesJson(),
    val ptcgoCode: String? = null,
    val releaseDate: String,
    val updatedAt: String,
    val images: GithubSetImagesJson,
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class GithubSetImagesJson(
    val symbol: String,
    val logo: String,
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class GithubLegalitiesJson(
    val standard: String? = null,
    val expanded: String? = null,
    val unlimited: String? = null,
)

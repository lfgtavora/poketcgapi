package com.lfgtavora.poketcgapi.catalog.model

data class Set(
    val id: String,
    val name: String,
    val series: String,
    val printedTotal: Int,
    val total: Int,
    val legalities: Legalities,
    val ptcgoCode: String? = null,
    val releaseDate: String,
    val updatedAt: String,
    val images: SetImages,
)

data class SetImages(
    val symbol: String,
    val logo: String,
)

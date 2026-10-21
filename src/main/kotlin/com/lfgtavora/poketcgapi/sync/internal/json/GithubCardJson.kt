package com.lfgtavora.poketcgapi.sync.internal.json

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class GithubCardJson(
    val id: String,
    val name: String,
    val supertype: String,
    val subtypes: List<String> = emptyList(),
    val level: String? = null,
    val hp: String? = null,
    val types: List<String> = emptyList(),
    val evolvesFrom: String? = null,
    val evolvesTo: List<String> = emptyList(),
    val rules: List<String> = emptyList(),
    val ancientTrait: GithubAncientTraitJson? = null,
    val abilities: List<GithubAbilityJson> = emptyList(),
    val attacks: List<GithubAttackJson> = emptyList(),
    val weaknesses: List<GithubTypeValueJson> = emptyList(),
    val resistances: List<GithubTypeValueJson> = emptyList(),
    val retreatCost: List<String> = emptyList(),
    val convertedRetreatCost: Int? = null,
    val number: String,
    val artist: String? = null,
    val rarity: String? = null,
    val flavorText: String? = null,
    val nationalPokedexNumbers: List<Int> = emptyList(),
    val legalities: GithubLegalitiesJson = GithubLegalitiesJson(),
    val regulationMark: String? = null,
    val images: GithubCardImagesJson,
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class GithubCardImagesJson(
    val small: String,
    val large: String,
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class GithubAncientTraitJson(
    val name: String,
    val text: String,
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class GithubAbilityJson(
    val name: String,
    val text: String? = null,
    val type: String,
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class GithubAttackJson(
    val cost: List<String> = emptyList(),
    val name: String,
    val text: String? = null,
    val damage: String? = null,
    val convertedEnergyCost: Int = 0,
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class GithubTypeValueJson(
    val type: String,
    val value: String,
)

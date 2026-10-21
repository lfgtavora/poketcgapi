package com.lfgtavora.poketcgapi.sync.internal.mapper

import com.lfgtavora.poketcgapi.catalog.model.Ability
import com.lfgtavora.poketcgapi.catalog.model.AncientTrait
import com.lfgtavora.poketcgapi.catalog.model.Attack
import com.lfgtavora.poketcgapi.catalog.model.Card
import com.lfgtavora.poketcgapi.catalog.model.CardImages
import com.lfgtavora.poketcgapi.catalog.model.Set
import com.lfgtavora.poketcgapi.catalog.model.Legalities
import com.lfgtavora.poketcgapi.catalog.model.SetImages
import com.lfgtavora.poketcgapi.catalog.model.TypeValue
import com.lfgtavora.poketcgapi.sync.internal.json.GithubAbilityJson
import com.lfgtavora.poketcgapi.sync.internal.json.GithubAncientTraitJson
import com.lfgtavora.poketcgapi.sync.internal.json.GithubAttackJson
import com.lfgtavora.poketcgapi.sync.internal.json.GithubCardImagesJson
import com.lfgtavora.poketcgapi.sync.internal.json.GithubCardJson
import com.lfgtavora.poketcgapi.sync.internal.json.GithubLegalitiesJson
import com.lfgtavora.poketcgapi.sync.internal.json.GithubSetImagesJson
import com.lfgtavora.poketcgapi.sync.internal.json.GithubSetJson
import com.lfgtavora.poketcgapi.sync.internal.json.GithubTypeValueJson

fun GithubSetJson.toDomain() =
    Set(
        id = id,
        name = name,
        series = series,
        printedTotal = printedTotal,
        total = total,
        legalities = legalities.toDomain(),
        ptcgoCode = ptcgoCode,
        releaseDate = releaseDate,
        updatedAt = updatedAt,
        images = images.toDomain(),
    )

fun GithubCardJson.toDomain(set: Set) =
    Card(
        id = id,
        name = name,
        supertype = supertype,
        subtypes = subtypes,
        level = level,
        hp = hp,
        types = types,
        evolvesFrom = evolvesFrom,
        evolvesTo = evolvesTo,
        rules = rules,
        ancientTrait = ancientTrait?.toDomain(),
        abilities = abilities.map { it.toDomain() },
        attacks = attacks.map { it.toDomain() },
        weaknesses = weaknesses.map { it.toDomain() },
        resistances = resistances.map { it.toDomain() },
        retreatCost = retreatCost,
        convertedRetreatCost = convertedRetreatCost,
        set = set,
        number = number,
        artist = artist,
        rarity = rarity,
        flavorText = flavorText,
        nationalPokedexNumbers = nationalPokedexNumbers,
        legalities = legalities.toDomain(),
        regulationMark = regulationMark,
        images = images.toDomain(),
        tcgplayer = null,
        cardmarket = null,
    )

private fun GithubSetImagesJson.toDomain() =
    SetImages(symbol = symbol, logo = logo)

private fun GithubLegalitiesJson.toDomain() =
    Legalities(
        standard = standard,
        expanded = expanded,
        unlimited = unlimited,
    )

private fun GithubCardImagesJson.toDomain() =
    CardImages(small = small, large = large)

private fun GithubAncientTraitJson.toDomain() =
    AncientTrait(name = name, text = text)

private fun GithubAbilityJson.toDomain() =
    Ability(name = name, text = text, type = type)

private fun GithubAttackJson.toDomain() =
    Attack(
        cost = cost,
        name = name,
        text = text,
        damage = damage,
        convertedEnergyCost = convertedEnergyCost,
    )

private fun GithubTypeValueJson.toDomain() =
    TypeValue(type = type, value = value)

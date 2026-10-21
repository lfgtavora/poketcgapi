package com.lfgtavora.poketcgapi.sync.internal

import com.lfgtavora.poketcgapi.sync.internal.json.GithubAbilityJson
import com.lfgtavora.poketcgapi.sync.internal.json.GithubAttackJson
import com.lfgtavora.poketcgapi.sync.internal.json.GithubCardImagesJson
import com.lfgtavora.poketcgapi.sync.internal.json.GithubCardJson
import com.lfgtavora.poketcgapi.sync.internal.json.GithubLegalitiesJson
import com.lfgtavora.poketcgapi.sync.internal.json.GithubSetImagesJson
import com.lfgtavora.poketcgapi.sync.internal.json.GithubSetJson
import com.lfgtavora.poketcgapi.sync.internal.json.GithubTypeValueJson
import com.lfgtavora.poketcgapi.sync.internal.mapper.toDomain
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class GithubCardMapperTest {

    @Test
    fun `maps set json to CardSet`() {
        val set = sampleGithubSet().toDomain()

        assertThat(set.id).isEqualTo("base1")
        assertThat(set.name).isEqualTo("Base")
        assertThat(set.series).isEqualTo("Base")
        assertThat(set.printedTotal).isEqualTo(102)
        assertThat(set.ptcgoCode).isEqualTo("BS")
        assertThat(set.updatedAt).isEqualTo("2022/10/10 15:12:00")
        assertThat(set.legalities.unlimited).isEqualTo("Legal")
        assertThat(set.images.symbol).isEqualTo("https://images.pokemontcg.io/base1/symbol.png")
    }

    @Test
    fun `maps card json without set using injected CardSet`() {
        val set = sampleGithubSet().toDomain()
        val card = sampleGithubCard().toDomain(set)

        assertThat(card.id).isEqualTo("base1-1")
        assertThat(card.name).isEqualTo("Alakazam")
        assertThat(card.set).isEqualTo(set)
        assertThat(card.subtypes).containsExactly("Stage 2")
        assertThat(card.types).containsExactly("Psychic")
        assertThat(card.attacks).hasSize(1)
        assertThat(card.attacks[0].name).isEqualTo("Confuse Ray")
        assertThat(card.attacks[0].convertedEnergyCost).isEqualTo(3)
        assertThat(card.tcgplayer).isNull()
        assertThat(card.cardmarket).isNull()
    }
}

internal fun sampleGithubSet(
    id: String = "base1",
    updatedAt: String = "2022/10/10 15:12:00",
) = GithubSetJson(
    id = id,
    name = "Base",
    series = "Base",
    printedTotal = 102,
    total = 102,
    legalities = GithubLegalitiesJson(unlimited = "Legal"),
    ptcgoCode = "BS",
    releaseDate = "1999/01/09",
    updatedAt = updatedAt,
    images = GithubSetImagesJson(
        symbol = "https://images.pokemontcg.io/base1/symbol.png",
        logo = "https://images.pokemontcg.io/base1/logo.png",
    ),
)

internal fun sampleGithubCard() = GithubCardJson(
    id = "base1-1",
    name = "Alakazam",
    supertype = "Pokémon",
    subtypes = listOf("Stage 2"),
    level = "42",
    hp = "80",
    types = listOf("Psychic"),
    evolvesFrom = "Kadabra",
    abilities = listOf(
        GithubAbilityJson(
            name = "Damage Swap",
            text = "Move 1 damage counter.",
            type = "Pokémon Power",
        ),
    ),
    attacks = listOf(
        GithubAttackJson(
            cost = listOf("Psychic", "Psychic", "Psychic"),
            name = "Confuse Ray",
            text = "Flip a coin.",
            damage = "30",
            convertedEnergyCost = 3,
        ),
    ),
    weaknesses = listOf(GithubTypeValueJson(type = "Psychic", value = "×2")),
    retreatCost = listOf("Colorless", "Colorless", "Colorless"),
    convertedRetreatCost = 3,
    number = "1",
    artist = "Ken Sugimori",
    rarity = "Rare Holo",
    flavorText = "Its brain can outperform a supercomputer.",
    nationalPokedexNumbers = listOf(65),
    legalities = GithubLegalitiesJson(unlimited = "Legal"),
    images = GithubCardImagesJson(
        small = "https://images.pokemontcg.io/base1/1.png",
        large = "https://images.pokemontcg.io/base1/1_hires.png",
    ),
)

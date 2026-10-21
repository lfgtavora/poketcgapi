package com.lfgtavora.poketcgapi.catalog.model

import com.fasterxml.jackson.annotation.JsonProperty
import java.math.BigDecimal

data class Card(
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
    val ancientTrait: AncientTrait? = null,
    val abilities: List<Ability> = emptyList(),
    val attacks: List<Attack> = emptyList(),
    val weaknesses: List<TypeValue> = emptyList(),
    val resistances: List<TypeValue> = emptyList(),
    val retreatCost: List<String> = emptyList(),
    val convertedRetreatCost: Int? = null,
    val set: Set,
    val number: String,
    val artist: String? = null,
    val rarity: String? = null,
    val flavorText: String? = null,
    val nationalPokedexNumbers: List<Int> = emptyList(),
    val legalities: Legalities,
    val regulationMark: String? = null,
    val images: CardImages,
    val tcgplayer: TcgPlayer? = null,
    val cardmarket: Cardmarket? = null,
)

data class AncientTrait(
    val name: String,
    val text: String,
)

data class Ability(
    val name: String,
    val text: String?,
    val type: String,
)

data class Attack(
    val cost: List<String> = emptyList(),
    val name: String,
    val text: String? = null,
    val damage: String? = null,
    val convertedEnergyCost: Int,
)

data class TypeValue(
    val type: String,
    val value: String,
)

data class Legalities(
    val standard: String? = null,
    val expanded: String? = null,
    val unlimited: String? = null,
)

data class CardImages(
    val small: String,
    val large: String,
)


data class TcgPlayer(
    val url: String,
    val updatedAt: String,
    val prices: TcgPlayerPrices,
)

data class TcgPlayerPrices(
    val normal: TcgPlayerPrice? = null,
    val holofoil: TcgPlayerPrice? = null,
    val reverseHolofoil: TcgPlayerPrice? = null,
    @get:JsonProperty("1stEditionHolofoil")
    val firstEditionHolofoil: TcgPlayerPrice? = null,
    @get:JsonProperty("1stEditionNormal")
    val firstEditionNormal: TcgPlayerPrice? = null,
)

data class TcgPlayerPrice(
    val low: BigDecimal? = null,
    val mid: BigDecimal? = null,
    val high: BigDecimal? = null,
    val market: BigDecimal? = null,
    val directLow: BigDecimal? = null,
)

data class Cardmarket(
    val url: String,
    val updatedAt: String,
    val prices: CardmarketPrices,
)

data class CardmarketPrices(
    val averageSellPrice: BigDecimal? = null,
    val lowPrice: BigDecimal? = null,
    val trendPrice: BigDecimal? = null,
    val germanProLow: BigDecimal? = null,
    val suggestedPrice: BigDecimal? = null,
    val reverseHoloSell: BigDecimal? = null,
    val reverseHoloLow: BigDecimal? = null,
    val reverseHoloTrend: BigDecimal? = null,
    val lowPriceExPlus: BigDecimal? = null,
    val avg1: BigDecimal? = null,
    val avg7: BigDecimal? = null,
    val avg30: BigDecimal? = null,
    val reverseHoloAvg1: BigDecimal? = null,
    val reverseHoloAvg7: BigDecimal? = null,
    val reverseHoloAvg30: BigDecimal? = null,
)

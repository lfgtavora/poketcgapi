package com.lfgtavora.poketcgapi.catalog.internal.persistence.data

import com.fasterxml.jackson.annotation.JsonProperty
import com.lfgtavora.poketcgapi.catalog.model.Ability
import com.lfgtavora.poketcgapi.catalog.model.AncientTrait
import com.lfgtavora.poketcgapi.catalog.model.Attack
import com.lfgtavora.poketcgapi.catalog.model.CardImages
import com.lfgtavora.poketcgapi.catalog.model.Cardmarket
import com.lfgtavora.poketcgapi.catalog.model.CardmarketPrices
import com.lfgtavora.poketcgapi.catalog.model.Legalities
import com.lfgtavora.poketcgapi.catalog.model.TcgPlayer
import com.lfgtavora.poketcgapi.catalog.model.TcgPlayerPrice
import com.lfgtavora.poketcgapi.catalog.model.TcgPlayerPrices
import com.lfgtavora.poketcgapi.catalog.model.TypeValue
import java.math.BigDecimal

data class AncientTraitJson(
    val name: String,
    val text: String,
)

data class AbilityJson(
    val name: String,
    val text: String? = null,
    val type: String,
)

data class TypeValueJson(
    val type: String,
    val value: String,
)

data class CardImagesJson(
    val small: String,
    val large: String,
)

data class LegalitiesJson(
    val standard: String? = null,
    val expanded: String? = null,
    val unlimited: String? = null,
)

data class AttackJson(
    val cost: List<String> = emptyList(),
    val name: String,
    val text: String? = null,
    val damage: String? = null,
    val convertedEnergyCost: Int,
)

data class TcgPlayerJson(
    val url: String,
    val updatedAt: String,
    val prices: TcgPlayerPricesJson,
)

data class TcgPlayerPricesJson(
    val normal: TcgPlayerPriceJson? = null,
    val holofoil: TcgPlayerPriceJson? = null,
    val reverseHolofoil: TcgPlayerPriceJson? = null,
    @get:JsonProperty("1stEditionHolofoil")
    val firstEditionHolofoil: TcgPlayerPriceJson? = null,
    @get:JsonProperty("1stEditionNormal")
    val firstEditionNormal: TcgPlayerPriceJson? = null,
)

data class TcgPlayerPriceJson(
    val low: BigDecimal? = null,
    val mid: BigDecimal? = null,
    val high: BigDecimal? = null,
    val market: BigDecimal? = null,
    val directLow: BigDecimal? = null,
)

data class CardmarketJson(
    val url: String,
    val updatedAt: String,
    val prices: CardmarketPricesJson,
)

data class CardmarketPricesJson(
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

fun AncientTraitJson.toDomain() =
    AncientTrait(name = name, text = text)

fun AbilityJson.toDomain() =
    Ability(name = name, text = text, type = type)

fun TypeValueJson.toDomain() =
    TypeValue(type = type, value = value)

fun CardImagesJson.toDomain() =
    CardImages(small = small, large = large)

fun LegalitiesJson.toDomain() =
    Legalities(
        standard = standard,
        expanded = expanded,
        unlimited = unlimited,
    )

fun AttackJson.toDomain() =
    Attack(
        cost = cost,
        name = name,
        text = text,
        damage = damage,
        convertedEnergyCost = convertedEnergyCost,
    )

fun TcgPlayerJson.toDomain() =
    TcgPlayer(
        url = url,
        updatedAt = updatedAt,
        prices = prices.toDomain(),
    )

fun TcgPlayerPricesJson.toDomain() =
    TcgPlayerPrices(
        normal = normal?.toDomain(),
        holofoil = holofoil?.toDomain(),
        reverseHolofoil = reverseHolofoil?.toDomain(),
        firstEditionHolofoil = firstEditionHolofoil?.toDomain(),
        firstEditionNormal = firstEditionNormal?.toDomain(),
    )

fun TcgPlayerPriceJson.toDomain() =
    TcgPlayerPrice(
        low = low,
        mid = mid,
        high = high,
        market = market,
        directLow = directLow,
    )

fun CardmarketJson.toDomain() =
    Cardmarket(
        url = url,
        updatedAt = updatedAt,
        prices = prices.toDomain(),
    )

fun CardmarketPricesJson.toDomain() =
    CardmarketPrices(
        averageSellPrice = averageSellPrice,
        lowPrice = lowPrice,
        trendPrice = trendPrice,
        germanProLow = germanProLow,
        suggestedPrice = suggestedPrice,
        reverseHoloSell = reverseHoloSell,
        reverseHoloLow = reverseHoloLow,
        reverseHoloTrend = reverseHoloTrend,
        lowPriceExPlus = lowPriceExPlus,
        avg1 = avg1,
        avg7 = avg7,
        avg30 = avg30,
        reverseHoloAvg1 = reverseHoloAvg1,
        reverseHoloAvg7 = reverseHoloAvg7,
        reverseHoloAvg30 = reverseHoloAvg30,
    )

fun AncientTrait.toJson() =
    AncientTraitJson(name = name, text = text)

fun Ability.toJson() =
    AbilityJson(name = name, text = text, type = type)

fun TypeValue.toJson() =
    TypeValueJson(type = type, value = value)

fun CardImages.toJson() =
    CardImagesJson(small = small, large = large)

fun Legalities.toJson() =
    LegalitiesJson(
        standard = standard,
        expanded = expanded,
        unlimited = unlimited,
    )

fun Attack.toJson() =
    AttackJson(
        cost = cost,
        name = name,
        text = text,
        damage = damage,
        convertedEnergyCost = convertedEnergyCost,
    )

fun TcgPlayer.toJson() =
    TcgPlayerJson(
        url = url,
        updatedAt = updatedAt,
        prices = prices.toJson(),
    )

fun TcgPlayerPrices.toJson() =
    TcgPlayerPricesJson(
        normal = normal?.toJson(),
        holofoil = holofoil?.toJson(),
        reverseHolofoil = reverseHolofoil?.toJson(),
        firstEditionHolofoil = firstEditionHolofoil?.toJson(),
        firstEditionNormal = firstEditionNormal?.toJson(),
    )

fun TcgPlayerPrice.toJson() =
    TcgPlayerPriceJson(
        low = low,
        mid = mid,
        high = high,
        market = market,
        directLow = directLow,
    )

fun Cardmarket.toJson() =
    CardmarketJson(
        url = url,
        updatedAt = updatedAt,
        prices = prices.toJson(),
    )

fun CardmarketPrices.toJson() =
    CardmarketPricesJson(
        averageSellPrice = averageSellPrice,
        lowPrice = lowPrice,
        trendPrice = trendPrice,
        germanProLow = germanProLow,
        suggestedPrice = suggestedPrice,
        reverseHoloSell = reverseHoloSell,
        reverseHoloLow = reverseHoloLow,
        reverseHoloTrend = reverseHoloTrend,
        lowPriceExPlus = lowPriceExPlus,
        avg1 = avg1,
        avg7 = avg7,
        avg30 = avg30,
        reverseHoloAvg1 = reverseHoloAvg1,
        reverseHoloAvg7 = reverseHoloAvg7,
        reverseHoloAvg30 = reverseHoloAvg30,
    )

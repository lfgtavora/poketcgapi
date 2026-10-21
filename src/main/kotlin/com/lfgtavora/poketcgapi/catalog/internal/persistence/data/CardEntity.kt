package com.lfgtavora.poketcgapi.catalog.internal.persistence.data

import com.lfgtavora.poketcgapi.catalog.model.Card
import jakarta.persistence.CollectionTable
import jakarta.persistence.Column
import jakarta.persistence.ElementCollection
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.Index
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OrderColumn
import jakarta.persistence.Table
import org.hibernate.annotations.Fetch
import org.hibernate.annotations.FetchMode
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.type.SqlTypes

@Entity
@Table(
    name = "cards",
    indexes = [Index(name = "idx_cards_name", columnList = "name")],
)
class CardEntity(
    @Id
    @Column(nullable = false)
    var id: String,

    @Column(nullable = false)
    var name: String,

    @Column(nullable = false)
    var supertype: String,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "set_id", nullable = false)
    var set: SetEntity,

    @Column(nullable = false)
    var number: String,

    var level: String? = null,

    var hp: String? = null,

    var evolvesFrom: String? = null,

    var convertedRetreatCost: Int? = null,

    var artist: String? = null,

    var rarity: String? = null,

    @Column(columnDefinition = "TEXT")
    var flavorText: String? = null,

    var regulationMark: String? = null,

    @JdbcTypeCode(SqlTypes.JSON)
    var images: CardImagesJson,

    @ElementCollection
    @CollectionTable(
        name = "card_subtypes",
        joinColumns = [JoinColumn(name = "card_id")],
        indexes = [Index(name = "idx_card_subtypes_subtype", columnList = "subtype")],
    )
    @Column(name = "subtype", nullable = false)
    @OrderColumn(name = "position")
    @Fetch(FetchMode.SUBSELECT)
    var subtypes: MutableList<String> = mutableListOf(),

    @ElementCollection
    @CollectionTable(
        name = "card_types",
        joinColumns = [JoinColumn(name = "card_id")],
        indexes = [Index(name = "idx_card_types_type", columnList = "type")],
    )
    @Column(name = "type", nullable = false)
    @OrderColumn(name = "position")
    @Fetch(FetchMode.SUBSELECT)
    var types: MutableList<String> = mutableListOf(),

    @JdbcTypeCode(SqlTypes.JSON)
    var evolvesTo: List<String> = emptyList(),

    @JdbcTypeCode(SqlTypes.JSON)
    var rules: List<String> = emptyList(),

    @JdbcTypeCode(SqlTypes.JSON)
    var ancientTrait: AncientTraitJson? = null,

    @JdbcTypeCode(SqlTypes.JSON)
    var abilities: List<AbilityJson> = emptyList(),

    @JdbcTypeCode(SqlTypes.JSON)
    var attacks: List<AttackJson> = emptyList(),

    @JdbcTypeCode(SqlTypes.JSON)
    var weaknesses: List<TypeValueJson> = emptyList(),

    @JdbcTypeCode(SqlTypes.JSON)
    var resistances: List<TypeValueJson> = emptyList(),

    @JdbcTypeCode(SqlTypes.JSON)
    var retreatCost: List<String> = emptyList(),

    @JdbcTypeCode(SqlTypes.JSON)
    var nationalPokedexNumbers: List<Int> = emptyList(),

    @JdbcTypeCode(SqlTypes.JSON)
    var legalities: LegalitiesJson = LegalitiesJson(),

    @JdbcTypeCode(SqlTypes.JSON)
    var tcgplayer: TcgPlayerJson? = null,

    @JdbcTypeCode(SqlTypes.JSON)
    var cardmarket: CardmarketJson? = null,
)

fun CardEntity.toDomain() =
    Card(
        id = id,
        name = name,
        supertype = supertype,
        subtypes = subtypes.toList(),
        level = level,
        hp = hp,
        types = types.toList(),
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
        set = set.toDomain(),
        number = number,
        artist = artist,
        rarity = rarity,
        flavorText = flavorText,
        nationalPokedexNumbers = nationalPokedexNumbers,
        legalities = legalities.toDomain(),
        regulationMark = regulationMark,
        images = images.toDomain(),
        tcgplayer = tcgplayer?.toDomain(),
        cardmarket = cardmarket?.toDomain(),
    )

fun Card.toEntity(set: SetEntity) =
    CardEntity(
        id = id,
        name = name,
        supertype = supertype,
        set = set,
        number = number,
        level = level,
        hp = hp,
        evolvesFrom = evolvesFrom,
        convertedRetreatCost = convertedRetreatCost,
        artist = artist,
        rarity = rarity,
        flavorText = flavorText,
        regulationMark = regulationMark,
        images = images.toJson(),
        subtypes = subtypes.toMutableList(),
        types = types.toMutableList(),
        evolvesTo = evolvesTo,
        rules = rules,
        ancientTrait = ancientTrait?.toJson(),
        abilities = abilities.map { it.toJson() },
        attacks = attacks.map { it.toJson() },
        weaknesses = weaknesses.map { it.toJson() },
        resistances = resistances.map { it.toJson() },
        retreatCost = retreatCost,
        nationalPokedexNumbers = nationalPokedexNumbers,
        legalities = legalities.toJson(),
        tcgplayer = tcgplayer?.toJson(),
        cardmarket = cardmarket?.toJson(),
    )


package com.lfgtavora.poketcgapi.catalog.internal.persistence.data

import com.lfgtavora.poketcgapi.catalog.model.Set
import com.lfgtavora.poketcgapi.catalog.model.SetImages
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Index
import jakarta.persistence.Table
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.type.SqlTypes

@Entity
@Table(
    name = "sets",
    indexes = [
        Index(name = "idx_set_release_date", columnList = "releaseDate")
    ]
)
class SetEntity(
    @Id
    @Column(nullable = false)
    var id: String,

    @Column(nullable = false)
    var name: String,

    @Column(nullable = false)
    var series: String,

    @Column(nullable = false)
    var printedTotal: Int,

    @Column(nullable = false)
    var total: Int,

    @Column(nullable = false)
    var releaseDate: String,

    @Column(nullable = false)
    var updatedAt: String,

    @JdbcTypeCode(SqlTypes.JSON)
    var images: SetImagesJson,

    @JdbcTypeCode(SqlTypes.JSON)
    var legalities: LegalitiesJson = LegalitiesJson(),

    var ptcgoCode: String? = null,
)

data class SetImagesJson(
    val symbol: String,
    val logo: String,
)

fun SetEntity.toDomain() =
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

fun SetImagesJson.toDomain() =
    SetImages(symbol = symbol, logo = logo)

fun Set.toEntity() =
    SetEntity(
        id = id,
        name = name,
        series = series,
        printedTotal = printedTotal,
        total = total,
        releaseDate = releaseDate,
        updatedAt = updatedAt,
        images = images.toJson(),
        legalities = legalities.toJson(),
        ptcgoCode = ptcgoCode,
    )

fun SetEntity.copyFrom(set: Set) {
    name = set.name
    series = set.series
    printedTotal = set.printedTotal
    total = set.total
    releaseDate = set.releaseDate
    updatedAt = set.updatedAt
    images = set.images.toJson()
    legalities = set.legalities.toJson()
    ptcgoCode = set.ptcgoCode
}

fun SetImages.toJson() =
    SetImagesJson(symbol = symbol, logo = logo)

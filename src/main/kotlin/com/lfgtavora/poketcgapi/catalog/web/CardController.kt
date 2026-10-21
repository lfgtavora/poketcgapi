package com.lfgtavora.poketcgapi.catalog.web

import com.lfgtavora.poketcgapi.catalog.model.Card
import com.lfgtavora.poketcgapi.catalog.CardService
import com.lfgtavora.poketcgapi.catalog.PageResult
import com.lfgtavora.poketcgapi.catalog.PaginationParams
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springdoc.core.annotations.ParameterObject
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/cards")
@Tag(name = "Cards", description = "Pokémon TCG card catalog")
class CardController(
    private val cardService: CardService,
) {

    @GetMapping("/{id}")
    @Operation(operationId = "findCardById", summary = "Get a card by id")
    @ApiResponses(
        ApiResponse(
            responseCode = "200",
            description = "Card found",
            content = [Content(schema = Schema(implementation = Card::class))],
        ),
        ApiResponse(responseCode = "404", description = "Card not found", content = [Content()]),
    )
    fun findById(
        @Parameter(description = "Card id", example = "base1-4", required = true)
        @PathVariable id: String,
    ): Card = cardService.findById(id)

    @GetMapping
    @Operation(operationId = "listCards", summary = "List cards")
    @ApiResponse(responseCode = "200", description = "Page of cards")
    fun findAll(
        @Parameter(description = "Search query")
        @RequestParam(name = "q", required = false) q: String?,
        @ParameterObject @Valid pagination: PaginationParams,
    ): PageResult<Card> =
        cardService.findAll(
            query= q,
            paginationParams = PaginationParams(
                page = pagination.page,
                pageSize = pagination.pageSize,
                orderBy = pagination.orderBy,
                select = pagination.select,
            )
        )

    @GetMapping("/search")
    @Operation(operationId = "searchCardsByName", summary = "Search cards by name")
    @ApiResponse(responseCode = "200", description = "Page of matching cards")
    fun findByNameContainingIgnoreCase(
        @Parameter(description = "Case-insensitive name fragment", required = true, example = "Charizard")
        @RequestParam(name = "name") name: String,
        @ParameterObject @Valid pagination: PaginationParams,
    ): PageResult<Card> =
        cardService.findByNameContainingIgnoreCase(
            name = name,
            paginationParams = PaginationParams(
                page = pagination.page,
                pageSize = pagination.pageSize,
                orderBy = pagination.orderBy,
                select = pagination.select,
            )
        )
}
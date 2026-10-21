package com.lfgtavora.poketcgapi.catalog.web

import com.lfgtavora.poketcgapi.catalog.PageResult
import com.lfgtavora.poketcgapi.catalog.PaginationParams
import com.lfgtavora.poketcgapi.catalog.model.Set
import com.lfgtavora.poketcgapi.catalog.SetService
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
@RequestMapping("/api/sets")
@Tag(name = "Sets", description = "Pokémon TCG sets catalog")
class SetController(
    private val setService: SetService,
) {

    @GetMapping("/{id}")
    @Operation(operationId = "findSetById", summary = "Get a set by id")
    @ApiResponses(
        ApiResponse(
            responseCode = "200",
            description = "Set found",
            content = [Content(schema = Schema(implementation = Set::class))],
        ),
        ApiResponse(responseCode = "404", description = "Set not found", content = [Content()]),
    )
    fun findById(
        @Parameter(description = "Set id", example = "base1", required = true)
        @PathVariable id: String,
    ): Set = setService.findById(id)

    @GetMapping
    @Operation(operationId = "listSets", summary = "List sets")
    @ApiResponse(responseCode = "200", description = "Page of sets")
    fun findAll(
        @Parameter(description = "Search query")
        @RequestParam(name = "q", required = false) q: String?,
        @ParameterObject @Valid pagination: PaginationParams,
    ): PageResult<Set> =
        setService.findAll(
            query= q,
            paginationParams = PaginationParams(
                page = pagination.page,
                pageSize = pagination.pageSize,
                orderBy = pagination.orderBy,
                select = pagination.select,
            )
        )
}
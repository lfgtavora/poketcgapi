package com.lfgtavora.poketcgapi.catalog

import io.swagger.v3.oas.annotations.Parameter
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min

data class PaginationParams(
    @field:Min(1)
    @Parameter(description = "1-based page number")
    val page: Int = 1,
    @field:Min(1)
    @field:Max(250)
    @Parameter(description = "Results per page")
    val pageSize: Int = 250,
    @Parameter(description = "Field to order by")
    val orderBy: String? = null,
    @Parameter(description = "Comma-separated fields to return")
    val select: String? = null,
)

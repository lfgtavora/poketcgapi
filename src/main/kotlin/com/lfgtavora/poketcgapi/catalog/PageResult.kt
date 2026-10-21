package com.lfgtavora.poketcgapi.catalog

class PageResult<T>(
    val content: List<T>,
    val page: Int,
    val size: Int,
    val totalElements: Long,
    val totalPages: Int,
)
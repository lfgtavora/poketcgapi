package com.lfgtavora.poketcgapi.catalog.exception

abstract class ResourceNotFoundException(
    resource: String,
    val id: String,
) : RuntimeException("$resource with id `$id` not found.")
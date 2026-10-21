package com.lfgtavora.poketcgapi.catalog

interface GetExistingSetRevisionUseCase {
    operator fun invoke(): Map<String, String>
}
package com.lfgtavora.poketcgapi.catalog.exception

class SetNotFoundException(id: String) : ResourceNotFoundException("Set",id)
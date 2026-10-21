package com.lfgtavora.poketcgapi.catalog.exception

class CardNotFoundException(id: String) :  ResourceNotFoundException("Card",id)
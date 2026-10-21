package com.lfgtavora.poketcgapi

import com.lfgtavora.poketcgapi.catalog.exception.ResourceNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun handleNotFound(ex: ResourceNotFoundException): Map<String, String> =
        mapOf("error" to (ex.message ?: "Not found"))

    @ExceptionHandler(MethodArgumentNotValidException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleValidation(ex: MethodArgumentNotValidException): Map<String, Any> =
        mapOf(
            "error" to "Validation failed",
            "details" to ex.bindingResult.fieldErrors.associate { it.field to (it.defaultMessage ?: "invalid") },
        )
}
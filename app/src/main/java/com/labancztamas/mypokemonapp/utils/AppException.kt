package com.labancztamas.mypokemonapp.utils

sealed class AppException : Exception() {
    class NotFoundException : AppException()
    class InvalidResponseException : AppException()
    class GeneralException : AppException()
}

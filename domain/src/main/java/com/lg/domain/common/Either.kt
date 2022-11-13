package com.lg.domain.common

sealed class Either<out T, out E> {
    data class Success<out T>(val data: T) : Either<T, Nothing>()
    data class Failure<out E>(val error: E) : Either<Nothing, E>()
}

fun <T> eitherSuccess(data: T) = Either.Success(data)

fun<T> eitherFailure(error: T) = Either.Failure(error)
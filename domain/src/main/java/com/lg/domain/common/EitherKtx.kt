package com.lg.domain.common

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

@OptIn(ExperimentalContracts::class)
inline fun <T1, E1, T2, E2> Either<T1, E1>.map(
    onSuccess: (T1) -> T2,
    onFailure: (E1) -> E2
): Either<T2, E2> {
    contract {
        callsInPlace(onSuccess, InvocationKind.AT_MOST_ONCE)
        callsInPlace(onFailure, InvocationKind.AT_MOST_ONCE)
    }

    return when (this) {
        is Either.Success -> Either.Success(onSuccess(data))
        is Either.Failure -> Either.Failure(onFailure(error))
    }
}
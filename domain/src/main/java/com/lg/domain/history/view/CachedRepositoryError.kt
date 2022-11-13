package com.lg.domain.history.view

sealed class CachedRepositoryError {
    object GenericError: CachedRepositoryError()
}
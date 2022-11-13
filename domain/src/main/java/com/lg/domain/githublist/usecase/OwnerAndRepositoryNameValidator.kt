package com.lg.domain.githublist.usecase

class OwnerAndRepositoryNameValidator {

    private val regex = Regex("\\S+/\\S+")

    fun isValid(ownerAndRepositoryInput: String): Boolean = ownerAndRepositoryInput.matches(regex)

}
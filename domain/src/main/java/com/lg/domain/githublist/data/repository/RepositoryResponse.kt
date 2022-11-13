package com.lg.domain.githublist.data.repository

data class RepositoryResponse(
    val id: Int
){
    var ownerAndRepository: String? = null
}
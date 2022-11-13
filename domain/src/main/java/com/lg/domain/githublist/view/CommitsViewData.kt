package com.lg.domain.githublist.view

data class CommitsViewData(
    val message: String,
    val sha: String,
    val authorName: String
){
    var isSelected: Boolean = false
}
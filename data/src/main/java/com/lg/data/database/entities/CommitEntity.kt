package com.lg.data.database.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index(value = ["ownerAndRepository"])])
data class CommitEntity(
    @PrimaryKey(autoGenerate = false)
    var sha: String = "",
    var ownerAndRepository: String = "",
    var message: String = "",
    var authorName: String = "",
    var page: Int = 0
)
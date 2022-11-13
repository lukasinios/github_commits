package com.lg.data.database.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index(value = ["ownerAndRepository"])])
data class RepositoryEntity(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    var repositoryId: Int = 0,
    var ownerAndRepository: String = "",
)
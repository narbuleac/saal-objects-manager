package com.example.saalobjectsmanager.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "saal_objects")
data class SaalObject(
    @PrimaryKey val id: String,
    val type: String,
    val name: String,
    val description: String
)


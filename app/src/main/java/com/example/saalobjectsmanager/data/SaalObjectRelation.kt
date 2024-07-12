package com.example.saalobjectsmanager.data

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Junction
import androidx.room.Relation

@Entity(
    tableName = "relations",
    primaryKeys = ["saalObjectId", "objectRelationId"]
)
data class SaalObjectRelation(
    val saalObjectId: String,
    val objectRelationId: String
)

data class SaalObjectWithRelations(
    @Embedded val saalObject: SaalObject,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            SaalObjectRelation::class,
            parentColumn = "saalObjectId",
            entityColumn = "objectRelationId"
        )
    )
    val relations: List<SaalObject>
)
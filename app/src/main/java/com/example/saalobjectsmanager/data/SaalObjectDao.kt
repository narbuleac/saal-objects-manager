package com.example.saalobjectsmanager.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface SaalObjectDao {
    @Query("SELECT * FROM saal_objects")
    fun getAllSaalObjects(): Flow<List<SaalObject>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSaalObject(saalObject: SaalObject)

    @Query("DELETE FROM saal_objects WHERE id = :objectId")
    suspend fun deleteSaalObject(objectId: String)

    @Query("SELECT * FROM saal_objects WHERE id = :objectId")
    fun getSaalObjectForId(objectId: String): Flow<SaalObject>

    @Transaction
    @Query("SELECT * FROM saal_objects WHERE id = :objectId")
    fun getRelationsForObjectId(objectId: String): Flow<SaalObjectWithRelations>

    @Insert
    suspend fun insertRelation(relation: SaalObjectRelation)

    @Delete
    suspend fun deleteRelation(relation: SaalObjectRelation)
}
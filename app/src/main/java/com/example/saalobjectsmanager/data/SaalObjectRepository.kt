package com.example.saalobjectsmanager.data

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.collections.List as List

@Singleton
class SaalObjectRepository @Inject constructor(private val saalObjectDao: SaalObjectDao) {

    fun getSaalObjects(): Flow<List<SaalObject>> {
        return saalObjectDao.getAllSaalObjects()
    }

    suspend fun addSaalObject(saalObject: SaalObject) = saalObjectDao.insertSaalObject(saalObject)

    suspend fun deleteSaalObject(objectId: String) = saalObjectDao.deleteSaalObject(objectId)

    fun getSaalObjectForId(objectId: String): Flow<SaalObject> {
        return saalObjectDao.getSaalObjectForId(objectId)
    }

    suspend fun addObjectRelation(saalRelation: SaalObjectRelation) =
        saalObjectDao.insertRelation(saalRelation)

    suspend fun deleteObjectRelation(saalRelation: SaalObjectRelation) =
        saalObjectDao.deleteRelation(saalRelation)

    fun getObjectRelations(objectId: String): Flow<SaalObjectWithRelations> {
        return saalObjectDao.getRelationsForObjectId(objectId)
    }
}
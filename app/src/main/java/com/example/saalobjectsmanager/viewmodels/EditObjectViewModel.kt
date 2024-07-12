package com.example.saalobjectsmanager.viewmodels

import android.app.ActivityManager.TaskDescription
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.saalobjectsmanager.data.SaalObject
import com.example.saalobjectsmanager.data.SaalObjectRelation
import com.example.saalobjectsmanager.data.SaalObjectRepository
import com.example.saalobjectsmanager.data.SaalObjectWithRelations
import com.example.saalobjectsmanager.ui.ObjectManagerDestinations
import com.example.saalobjectsmanager.ui.ObjectManagerDestinationsArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class EditObjectViewModel @Inject constructor(
    private val repository: SaalObjectRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val objectId: String = savedStateHandle[ObjectManagerDestinationsArgs.OBJECT_ID_ARG]!!

    private val _type = MutableStateFlow("")
    val type = _type.asStateFlow()
    private val _name = MutableStateFlow("")
    val name = _name.asStateFlow()
    private val _description = MutableStateFlow("")
    val description = _description.asStateFlow()
    private val _isObjectSaved = MutableStateFlow(false)
    val isObjectSaved = _isObjectSaved.asStateFlow()

    private val _saalObject = MutableStateFlow<SaalObject?>(null)
    val saalObject: Flow<SaalObject> get() = _saalObject.filterNotNull()

    private val _saalObjectList = MutableStateFlow<List<SaalObject>?>(null)
    val saalObjectList: Flow<List<SaalObject>> get() = _saalObjectList.filterNotNull()

    private val _objectRelations = MutableStateFlow<SaalObjectWithRelations?>(null)
    val objectRelations: Flow<SaalObjectWithRelations> get() = _objectRelations.filterNotNull()

    fun updateType(input: String) {
        _type.value = input
    }

    fun updateName(input: String) {
        _name.value = input
    }

    fun updateDescription(input: String) {
        _description.value = input
    }

    fun addRelation(saalObject: SaalObject) {
        val relation = SaalObjectRelation(saalObjectId = objectId, objectRelationId = saalObject.id)
        viewModelScope.launch {
            try {
                repository.addObjectRelation(relation)
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                refreshData()
            }
        }
    }

    fun deleteRelation(saalObject: SaalObject) {
        val relation = SaalObjectRelation(saalObjectId = objectId, objectRelationId = saalObject.id)
        viewModelScope.launch {
            try {
                repository.deleteObjectRelation(relation)
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                refreshData()
            }
        }
    }

    fun saveObjectChanges() = viewModelScope.launch {
        try {
            val objectId: String
            if (_saalObject.value != null) {
                objectId = _saalObject.value!!.id
            } else {
                objectId = withContext(Dispatchers.Default) {
                    UUID.randomUUID().toString()
                }
            }

            val saalObject =
                SaalObject(
                    id = objectId,
                    type = type.value,
                    name = name.value,
                    description = description.value
                )
            repository.addSaalObject(saalObject)
        } catch (e: Exception) {
            throw Error("Error editing object")
        } finally {
            _isObjectSaved.value = true
        }
    }

    init {
        refreshData()
    }

    fun refreshData() {

        viewModelScope.launch {
            try {
                coroutineScope {
                    launch {
                        repository.getSaalObjectForId(objectId).collect {
                            _saalObject.value = it
                            _type.value = it.type
                            _name.value = it.name
                            _description.value = it.description
                        }
                    }

                    launch {
                        _saalObjectList.value = repository.getSaalObjects().first()
                    }

                    launch {
                        _objectRelations.value = repository.getObjectRelations(objectId).first()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
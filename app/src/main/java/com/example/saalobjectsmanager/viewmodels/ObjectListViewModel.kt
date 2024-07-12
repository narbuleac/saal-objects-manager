package com.example.saalobjectsmanager.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.saalobjectsmanager.data.SaalObject
import com.example.saalobjectsmanager.data.SaalObjectRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ObjectListViewModel @Inject constructor(private val repository: SaalObjectRepository) :
    ViewModel() {

    private val _saalObjects = MutableStateFlow<List<SaalObject>?>(null)
    val saalObjects: Flow<List<SaalObject>> get() = _saalObjects.filterNotNull()

    private val _filteredObjects = MutableStateFlow<List<SaalObject>?>(null)
    val filteredObjects: Flow<List<SaalObject>> get() = _filteredObjects.filterNotNull()

    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    init {
        refreshData()
    }

    fun refreshData() {
        viewModelScope.launch {
            try {
                _saalObjects.value = repository.getSaalObjects().first()
                _filteredObjects.value = _saalObjects.value
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun deleteObject(objectId: String) = viewModelScope.launch {
        try {
            repository.deleteSaalObject(objectId)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            refreshData()
        }
    }

    fun search(searchText: String) {
        _searchText.value = searchText
        viewModelScope.launch {
            delay(1000)
            _saalObjects.map { saalObjects -> saalObjects?.filter { it.name.contains(searchText) } }
                .collect { filtered ->
                    _filteredObjects.value = filtered
                }
        }
    }
}
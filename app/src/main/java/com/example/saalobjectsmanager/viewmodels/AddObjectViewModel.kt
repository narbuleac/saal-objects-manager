package com.example.saalobjectsmanager.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.saalobjectsmanager.data.SaalObject
import com.example.saalobjectsmanager.data.SaalObjectRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class AddObjectViewModel @Inject constructor(private val repository: SaalObjectRepository) :
    ViewModel() {

    private val _type = MutableStateFlow("")
    val type = _type.asStateFlow()
    private val _name = MutableStateFlow("")
    val name = _name.asStateFlow()
    private val _description = MutableStateFlow("")
    val description = _description.asStateFlow()
    private val _isObjectAdded = MutableStateFlow(false)
    val isObjectAdded = _isObjectAdded.asStateFlow()

    fun updateType(input:String) {
        _type.value = input
    }

    fun updateName(input:String) {
        _name.value = input
    }

    fun updateDescription(input:String) {
        _description.value = input
    }

    fun insertObject() = viewModelScope.launch {
        try {
            val objectId = withContext(Dispatchers.Default) {
                UUID.randomUUID().toString()
            }
            val saalObject =
                SaalObject(id = objectId, type = type.value, name = name.value, description = description.value)
            repository.addSaalObject(saalObject)
        } catch (error: Error) {
            throw Error("Error inserting object")
        } finally {
            _isObjectAdded.value = true
        }
    }

}
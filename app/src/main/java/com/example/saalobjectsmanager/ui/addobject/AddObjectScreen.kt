package com.example.saalobjectsmanager.ui.addobject

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.saalobjectsmanager.R
import com.example.saalobjectsmanager.ui.utlities.SaalTextField
import com.example.saalobjectsmanager.viewmodels.AddObjectViewModel
import com.example.saalobjectsmanager.viewmodels.ObjectListViewModel

@Composable
fun AddObjectButton(onAddButtonClick: () -> Unit) {
    ExtendedFloatingActionButton(
        onClick = { onAddButtonClick() },
        modifier = Modifier.padding(8.dp)
    ) {
        Row {
            Icon(Icons.Filled.Add, contentDescription = null)
            Text(text = "Add object")
        }
    }
}

@Composable
fun AddObjectScreen(
    modifier: Modifier = Modifier,
    viewModel: AddObjectViewModel = hiltViewModel(),
    onObjectAdded: (Boolean) -> Unit
) {

    val isObjectAdded by viewModel.isObjectAdded.collectAsState()

    LaunchedEffect(isObjectAdded) {
        if (isObjectAdded) {
            onObjectAdded(isObjectAdded)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column {
            SaalTextField(
                value = viewModel.type.collectAsState().value,
                onValueChanged = { viewModel.updateType(it) },
                placeholder = R.string.placeholder_object_type,
                modifier = Modifier
            )
            SaalTextField(
                value = viewModel.name.collectAsState().value,
                onValueChanged = { viewModel.updateName(it) },
                placeholder = R.string.placeholder_object_name,
                modifier = Modifier
            )
            SaalTextField(
                value = viewModel.description.collectAsState().value,
                onValueChanged = { viewModel.updateDescription(it) },
                placeholder = R.string.placeholder_object_description,
                modifier = Modifier
            )
        }
        Spacer(modifier = modifier.weight(1f, true))
        AddObjectButton(onAddButtonClick = {
            viewModel.insertObject()
        })
    }
}
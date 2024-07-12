package com.example.saalobjectsmanager.ui.editobject

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.saalobjectsmanager.R
import com.example.saalobjectsmanager.data.SaalObject
import com.example.saalobjectsmanager.ui.utlities.SaalTextField
import com.example.saalobjectsmanager.viewmodels.EditObjectViewModel

@Composable
fun SaveChangesButton(onSaveClick: () -> Unit) {
    ExtendedFloatingActionButton(
        onClick = { onSaveClick() },
        modifier = Modifier.padding(8.dp)
    ) {
        Text(text = "Save")
    }
}

@Composable
fun EditRelationButton(onEditRelationClick: () -> Unit) {
    ExtendedFloatingActionButton(
        onClick = { onEditRelationClick() },
        modifier = Modifier.padding(8.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Filled.Edit, contentDescription = null)
            Text(text = "Edit relations")
        }
    }
}

@Composable
fun AddRelationListItem(
    saalObject: SaalObject,
    onAddClick: (SaalObject) -> Unit,
    onDeleteClick: (SaalObject) -> Unit,
) {

    Card(
        modifier = Modifier
            .fillMaxSize(),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface)
    ) {
        Row {
            Column {
                Row {
                    Text(
                        text = saalObject.type + ":",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = " " + saalObject.name,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
                Text(text = saalObject.description)
                Row(modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End) {
                    Button(onClick = { onDeleteClick(saalObject) }, modifier = Modifier.padding(8.dp)) {
                        Text(text = "Delete")
                    }
                    Button(onClick = { onAddClick(saalObject) }, modifier = Modifier.padding(8.dp)) {
                        Text(text = "Add")
                    }
                }
            }
        }
        HorizontalDivider()
    }
}

@Composable
fun RelationListItem(
    saalObject: SaalObject,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface)
    ) {
        Row {
            Column {
                Row {
                    Text(
                        text = saalObject.type + ":",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = " " + saalObject.name,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
                Text(text = saalObject.description)
            }
        }
    }
    HorizontalDivider()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditObjectScreen(
    modifier: Modifier = Modifier,
    viewModel: EditObjectViewModel = hiltViewModel(),
    onObjectSaved: (Boolean) -> Unit
) {

    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()
    val isObjectAdded by viewModel.isObjectSaved.collectAsState()
    val relationsList =
        viewModel.objectRelations.collectAsState(initial = null).value
    val saalObjectList =
        viewModel.saalObjectList.collectAsState(initial = emptyList()).value

    LaunchedEffect(isObjectAdded) {
        if (isObjectAdded) {
            onObjectSaved(isObjectAdded)
        }
    }

    LaunchedEffect(relationsList) {
        viewModel.refreshData()
    }

    Column(
        modifier = modifier
            .fillMaxSize(),
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
        LazyColumn(modifier = Modifier.padding(8.dp)) {
            if (!relationsList?.relations.isNullOrEmpty()) {
                item {
                    Text(
                        style = MaterialTheme.typography.titleLarge,
                        text = "Relations"
                    )
                }
                item { HorizontalDivider() }
                items(relationsList!!.relations) { item ->
                    RelationListItem(
                        saalObject = item
                    )
                }
            }
        }
        Spacer(modifier = Modifier.weight(1.0F))
        Row {
            SaveChangesButton(onSaveClick = {
                viewModel.saveObjectChanges()
            })
            EditRelationButton(onEditRelationClick = {
                showBottomSheet = !showBottomSheet
            })
        }
    }
    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                showBottomSheet = false
            },
            sheetState = sheetState
        ) {
            LazyColumn(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(saalObjectList) { item ->
                    AddRelationListItem(
                        saalObject = item,
                        onAddClick = {
                            viewModel.addRelation(it)
                        },
                        onDeleteClick = {
                            viewModel.deleteRelation(it)
                        },
                    )
                }
            }
        }
    }
}

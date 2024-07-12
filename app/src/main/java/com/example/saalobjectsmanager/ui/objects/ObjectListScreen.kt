package com.example.saalobjectsmanager.ui.objects

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.saalobjectsmanager.R
import com.example.saalobjectsmanager.data.SaalObject
import com.example.saalobjectsmanager.ui.ObjectManagerScreen
import com.example.saalobjectsmanager.ui.theme.SaalObjectsManagerTheme
import com.example.saalobjectsmanager.viewmodels.ObjectListViewModel

@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    searchText: String,
    onValueChanged: (String) -> Unit
) {

    TextField(
        value = searchText,
        onValueChange = {
            onValueChanged(it)
        },
        leadingIcon = {
            Icon(imageVector = Icons.Default.Search, contentDescription = null)
        },
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            focusedContainerColor = MaterialTheme.colorScheme.surface
        ),
        placeholder = {
            Text(stringResource(R.string.placeholder_search))
        },
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 56.dp)
    )
}

@Composable
fun ObjectActionMenu(
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    var expanded by remember {
        mutableStateOf(false)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize(Alignment.TopEnd)
    ) {
        IconButton(onClick = { expanded = !expanded }) {
            Icon(imageVector = Icons.Default.MoreVert, contentDescription = null)
        }

        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            DropdownMenuItem(
                text = { Text(stringResource(id = R.string.edit)) },
                onClick = {
                    expanded = !expanded
                    onEditClick()
                })
            DropdownMenuItem(
                text = { Text(stringResource(id = R.string.delete)) },
                onClick = {
                    expanded = !expanded
                    onDeleteClick()
                })
        }
    }
}

@Composable
fun ObjectListItem(
    saalObject: SaalObject,
    modifier: Modifier = Modifier,
    onEditClick: (String) -> Unit,
    viewModel: ObjectListViewModel = hiltViewModel()
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
            ObjectActionMenu(onEditClick = {
                onEditClick(saalObject.id)
            }, onDeleteClick = {
                viewModel.deleteObject(saalObject.id)
            })
        }
    }
    HorizontalDivider()
}

@Composable
fun AddButton(
    modifier: Modifier = Modifier,
    onAddButtonClick: () -> Unit
) {
    ExtendedFloatingActionButton(
        onClick = { onAddButtonClick() },
        modifier = modifier.padding(8.dp)
    ) {
        Row {
            Icon(Icons.Filled.Add, contentDescription = null)
            Text(text = "Add new object")
        }
    }
}

@Composable
fun ObjectList(
    objectsList: List<SaalObject>,
    onEditClick: (String) -> Unit,
) {
    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        items(objectsList) { item ->
            ObjectListItem(
                saalObject = item,
                onEditClick = onEditClick
            )
        }
    }
}

@Composable
fun ObjectListScreen(
    modifier: Modifier = Modifier,
    viewModel: ObjectListViewModel = hiltViewModel(),
    onEditClick: (String) -> Unit,
    onAddButtonClick: () -> Unit
) {
    val saalObjectList = viewModel.saalObjects.collectAsState(initial = emptyList()).value
    val filteredObjectList = viewModel.filteredObjects.collectAsState(initial = emptyList()).value

    LaunchedEffect(saalObjectList) {
        viewModel.refreshData()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (saalObjectList.isEmpty()) {
            Text(
                text = "You have no objects yet",
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
                modifier = modifier.fillMaxWidth()
            )
        } else {
            SearchBar(
                searchText = viewModel.searchText.collectAsState().value,
                onValueChanged = { viewModel.search(it) }
            )
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
                    .padding(top = 16.dp)
            ) {
                ObjectList(
                    objectsList = filteredObjectList,
                    onEditClick = onEditClick,
                )
            }
        }
        AddButton(onAddButtonClick = {
            onAddButtonClick()
        })
    }
}

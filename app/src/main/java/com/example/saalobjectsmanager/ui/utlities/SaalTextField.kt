package com.example.saalobjectsmanager.ui.utlities

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

@Composable
fun SaalTextField(
    value: String,
    onValueChanged: (String) -> Unit,
    placeholder: Int,
    modifier: Modifier
) {
    TextField(
        value = value,
        onValueChange = {
            onValueChanged(it)
        },
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            focusedContainerColor = MaterialTheme.colorScheme.surface
        ),
        label = {
            Text(stringResource(placeholder))
        },
        modifier = modifier
            .padding(8.dp)
            .fillMaxWidth()
            .heightIn(min = 56.dp)
    )
}
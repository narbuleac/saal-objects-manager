package com.example.saalobjectsmanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHost
import androidx.navigation.compose.NavHost
import com.example.saalobjectsmanager.ui.ObjectsManagerApp
import com.example.saalobjectsmanager.ui.objects.AddButton
import com.example.saalobjectsmanager.ui.objects.ObjectList
import com.example.saalobjectsmanager.ui.objects.ObjectListScreen
import com.example.saalobjectsmanager.ui.theme.SaalObjectsManagerTheme
import com.example.saalobjectsmanager.viewmodels.ObjectListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SaalObjectsManagerTheme {
                ObjectsManagerApp()
            }
        }
    }
}
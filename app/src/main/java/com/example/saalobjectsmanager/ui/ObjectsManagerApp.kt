package com.example.saalobjectsmanager.ui

import android.app.Activity
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.saalobjectsmanager.R
import com.example.saalobjectsmanager.ui.ObjectManagerDestinations.OBJECT_LIST_ROUTE
import com.example.saalobjectsmanager.ui.ObjectManagerDestinationsArgs.OBJECT_ID_ARG
import com.example.saalobjectsmanager.ui.ObjectManagerDestinationsArgs.TITLE_ARG
import com.example.saalobjectsmanager.ui.ObjectManagerScreens.ADD_OBJECT_SCREEN
import com.example.saalobjectsmanager.ui.ObjectManagerScreens.EDIT_OBJECT_SCREEN
import com.example.saalobjectsmanager.ui.ObjectManagerScreens.OBJECT_LIST_SCREEN
import com.example.saalobjectsmanager.ui.addobject.AddObjectScreen
import com.example.saalobjectsmanager.ui.editobject.EditObjectScreen
import com.example.saalobjectsmanager.ui.objects.ObjectListScreen

private object ObjectManagerScreens {
    const val OBJECT_LIST_SCREEN = "objectList"
    const val ADD_OBJECT_SCREEN = "addObject"
    const val EDIT_OBJECT_SCREEN = "editObject"
}

object ObjectManagerDestinationsArgs {
    const val OBJECT_ID_ARG = "objectId"
    const val TITLE_ARG = "title"
}

object ObjectManagerDestinations {
    const val OBJECT_LIST_ROUTE = "$OBJECT_LIST_SCREEN"
    const val ADD_OBJECT_ROUTE = "$ADD_OBJECT_SCREEN/{$TITLE_ARG}"
    const val EDIT_OBJECT_ROUTE = "$EDIT_OBJECT_SCREEN/{$TITLE_ARG}/{$OBJECT_ID_ARG}"
}

enum class ObjectManagerScreenTitles(@StringRes val title: Int) {
    ObjectList(title = R.string.object_list),
    AddObject(title = R.string.add_object),
    EditObject(title = R.string.edit_object)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ObjectManagerAppBar(
    @StringRes title: Int,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    CenterAlignedTopAppBar(
        title = { Text(stringResource(title)) },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = modifier,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = null
                    )
                }
            }
        }
    )
}

@Composable
fun ObjectsManagerApp() {
    val navController = rememberNavController()
    ObjectsManagerNavHost(navController = navController)
}

@Composable
fun ObjectsManagerNavHost(
    navController: NavHostController,
) {

    val startDestination = OBJECT_LIST_ROUTE
    val currentNavBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentNavBackStackEntry?.destination?.route ?: startDestination

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            ObjectManagerAppBar(
                title = currentNavBackStackEntry?.arguments?.getInt(TITLE_ARG)
                    ?: R.string.object_list,
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = { navController.navigateUp() }
            )
        }) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = startDestination
        ) {
            composable(
                route = ObjectManagerDestinations.OBJECT_LIST_ROUTE
            ) {

                ObjectListScreen(
                    modifier = Modifier.padding(innerPadding),
                    onEditClick = {
                        navController.navigate("$EDIT_OBJECT_SCREEN/${R.string.edit_object}/$it")
                    },
                    onAddButtonClick = {
                        navController.navigate("$ADD_OBJECT_SCREEN/${R.string.add_object}")
                    }
                )
            }
            composable(
                route = ObjectManagerDestinations.ADD_OBJECT_ROUTE,
                arguments = listOf(navArgument(TITLE_ARG) { type = NavType.IntType })
            ) {
                AddObjectScreen(
                    modifier = Modifier.padding(innerPadding),
                    onObjectAdded = { navController.navigateUp() }
                )
            }
            composable(
                route = ObjectManagerDestinations.EDIT_OBJECT_ROUTE,
                arguments = listOf(
                    navArgument(TITLE_ARG) { type = NavType.IntType },
                    navArgument(OBJECT_ID_ARG) { type = NavType.StringType })
            ) {
                EditObjectScreen(
                    modifier = Modifier.padding(innerPadding),
                    onObjectSaved = { navController.navigateUp() }
                )
            }
        }
    }
}


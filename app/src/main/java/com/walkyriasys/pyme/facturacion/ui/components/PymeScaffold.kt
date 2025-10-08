package com.walkyriasys.pyme.facturacion.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.walkyriasys.pyme.facturacion.ui.Screens

sealed class BottomNavItem(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Products : BottomNavItem(Screens.Products.route, "Productos", Icons.Default.Inventory)
    object Orders : BottomNavItem(Screens.Orders.route, "Órdenes", Icons.Default.Receipt)
    object Reports : BottomNavItem(Screens.Reports.route, "Reportes", Icons.Default.Assessment)
    object Settings : BottomNavItem(Screens.Settings.route, "Configuración", Icons.Default.Settings)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PymeScaffold(
    title: String,
    navController: NavController,
    modifier: Modifier = Modifier,
    showBottomBar: Boolean = true,
    actions: @Composable RowScope.() -> Unit = {},
    navigationIcon: @Composable () -> Unit = {},
    floatingActionButton: @Composable () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val bottomNavItems = listOf(
        BottomNavItem.Products,
        BottomNavItem.Orders,
        BottomNavItem.Reports,
        BottomNavItem.Settings
    )

    Scaffold(
        modifier = modifier.fillMaxSize(),
        floatingActionButton = floatingActionButton,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Medium
                    )
                },
                navigationIcon = navigationIcon,
                actions = actions,
            )
        },
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    bottomNavItems.forEach { item ->
                        NavigationBarItem(
                            icon = {
                                Icon(
                                    imageVector = item.icon,
                                    contentDescription = item.title
                                )
                            },
                            label = {
                                Text(
                                    text = item.title,
                                    style = MaterialTheme.typography.labelSmall
                                )
                            },
                            selected = currentRoute == item.route,
                            onClick = {
                                if (currentRoute != item.route) {
                                    navController.navigate(item.route) {
                                        // Pop up to the start destination to avoid building up a large stack
                                        popUpTo(navController.graph.startDestinationId) {
                                            saveState = true
                                        }
                                        // Avoid multiple copies of the same destination
                                        launchSingleTop = true
                                        // Restore state when reselecting a previously selected item
                                        restoreState = true
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        content(paddingValues)
    }
}

// Overloaded version for simpler usage without action buttons
@Composable
fun PymeScaffold(
    title: String,
    navController: NavController,
    modifier: Modifier = Modifier,
    showBottomBar: Boolean = true,
    content: @Composable (PaddingValues) -> Unit
) {
    PymeScaffold(
        title = title,
        navController = navController,
        modifier = modifier,
        showBottomBar = showBottomBar,
        content = content
    )
}

@Preview(showBackground = true)
@Composable
fun PymeScaffoldPreview() {
    val navController = rememberNavController()
    PymeScaffold(
        title = "Productos",
        navController = navController,
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Text(
                text = "Screen Content Here",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}
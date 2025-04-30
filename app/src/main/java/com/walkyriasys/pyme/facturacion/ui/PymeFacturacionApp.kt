package com.walkyriasys.pyme.facturacion.ui

import PymeFacturacionNavHost
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.walkyriasys.pyme.facturacion.navigation.NavControllerNavigator
import com.walkyriasys.pyme.facturacion.navigation.Navigator
import com.walkyriasys.pyme.facturacion.ui.theme.PymefacturacionTheme

val LocalNavigator = compositionLocalOf<Navigator> { error("Navigator not provided") }

@Composable
fun PymeFacturacionApp(snackbarHostState: SnackbarHostState) {
    PymefacturacionTheme {
        val navController = rememberNavController()
        val navigator = NavControllerNavigator(navController)

        CompositionLocalProvider(LocalNavigator provides navigator) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                PymeFacturacionNavHost(
                    navController = navController,
                )

                SnackbarHost(
                    hostState = snackbarHostState,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 5.dp)
                )
            }
        }
    }
}
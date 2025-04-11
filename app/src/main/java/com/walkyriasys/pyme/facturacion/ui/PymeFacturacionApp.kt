package com.walkyriasys.pyme.facturacion.ui

import PymeFacturacionNavHost
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.walkyriasys.pyme.facturacion.ui.theme.PymefacturacionTheme

@Composable
fun PymeFacturacionApp() {
    PymefacturacionTheme {
        val navController = rememberNavController()
        PymeFacturacionNavHost(navController = navController)
    }
}
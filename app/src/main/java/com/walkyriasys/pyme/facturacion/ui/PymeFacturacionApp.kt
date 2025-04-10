package com.walkyriasys.pyme.facturacion.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.walkyriasys.pyme.facturacion.ui.theme.PymefacturacionTheme

@Composable
fun PymeFacturacionApp() {
    PymefacturacionTheme {
        Text(
            text = "Hello Pyme Facturacion!",
            modifier = Modifier
        )
    }
}
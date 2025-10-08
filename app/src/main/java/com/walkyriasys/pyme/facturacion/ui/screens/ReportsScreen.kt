package com.walkyriasys.pyme.facturacion.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.walkyriasys.pyme.facturacion.ui.components.PymeScaffold
import com.walkyriasys.pyme.facturacion.ui.theme.PymefacturacionTheme

data class ReportItem(
    val title: String,
    val description: String,
    val period: String,
    val amount: String
)

@Composable
fun ReportsScreen(navController: NavController) {
    // Sample data - in a real app this would come from a ViewModel
    val reports = listOf(
        ReportItem(
            title = "Ventas del Mes",
            description = "Total de ventas realizadas este mes",
            period = "Enero 2024",
            amount = "$125,430.50"
        ),
        ReportItem(
            title = "Productos Más Vendidos",
            description = "Top 10 productos con mayor cantidad de ventas",
            period = "Últimos 30 días",
            amount = "45 productos"
        ),
        ReportItem(
            title = "Ingresos por Categoría",
            description = "Distribución de ingresos por categoría de producto",
            period = "Trimestre actual",
            amount = "$89,230.75"
        ),
        ReportItem(
            title = "Clientes Frecuentes",
            description = "Lista de clientes con mayor número de compras",
            period = "Año 2024",
            amount = "127 clientes"
        )
    )

    PymeScaffold(
        title = "Reportes",
        navController = navController,
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(reports) { report ->
                ReportCard(report = report)
            }
        }
    }
}

@Composable
private fun ReportCard(report: ReportItem) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = report.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = report.description,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 4.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Column {
                    Text(
                        text = "Período:",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = report.period,
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Medium
                    )
                }

                Text(
                    text = report.amount,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ReportsScreenPreview() {
    PymefacturacionTheme {
        ReportsScreen(rememberNavController())
    }
}

@Preview(showBackground = true)
@Composable
fun ReportCardPreview() {
    PymefacturacionTheme {
        ReportCard(
            report = ReportItem(
                title = "Ventas del Mes",
                description = "Total de ventas realizadas este mes",
                period = "Enero 2024",
                amount = "$125,430.50"
            )
        )
    }
}
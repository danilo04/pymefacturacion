import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.walkyriasys.pyme.facturacion.ui.Screens
import com.walkyriasys.pyme.facturacion.ui.screens.AddOrderScreen
import com.walkyriasys.pyme.facturacion.ui.screens.AddProductScreen
import com.walkyriasys.pyme.facturacion.ui.screens.BluetoothDeviceSelectionScreen
import com.walkyriasys.pyme.facturacion.ui.screens.HomeScreen
import com.walkyriasys.pyme.facturacion.ui.screens.OrderDetailsScreen
import com.walkyriasys.pyme.facturacion.ui.screens.OrdersScreen
import com.walkyriasys.pyme.facturacion.ui.screens.PrintTestScreen
import com.walkyriasys.pyme.facturacion.ui.screens.ProductsScreen
import com.walkyriasys.pyme.facturacion.ui.screens.ReportsScreen
import com.walkyriasys.pyme.facturacion.ui.screens.SettingsScreen

@Composable
fun PymeFacturacionNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = Screens.Home.route,
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screens.Home.route) {
            HomeScreen(navController)
        }
        composable(Screens.Products.route) { 
            ProductsScreen(navController)
        }
        composable(Screens.AddProduct.route) {
            AddProductScreen(navController)
        }
        composable(Screens.Orders.route) {
            OrdersScreen(navController)
        }
        composable(Screens.AddOrder.route) {
            AddOrderScreen(navController)
        }
        composable(
            route = "${Screens.OrderDetail.route}/{orderId}",
            arguments = listOf(navArgument("orderId") { type = NavType.LongType })
        ) { backStackEntry ->
            val orderId = backStackEntry.arguments?.getLong("orderId") ?: 0L
            OrderDetailsScreen(navController, orderId)
        }
        composable(Screens.Reports.route) {
            ReportsScreen(navController)
        }
        composable(Screens.Settings.route) {
            SettingsScreen(
                navController = navController,
                onPrinterSelected = { /* Handle printer selection if needed */ },
                onBarcodeGenerationChanged = { /* Handle barcode generation setting */ }
            )
        }
        composable(Screens.PrinterSelection.route) {
            BluetoothDeviceSelectionScreen(navController)
        }
        composable(Screens.PrintTest.route) {
            PrintTestScreen(navController)
        }
    }
}
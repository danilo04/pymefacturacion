package com.walkyriasys.pyme.facturacion.navigation

import androidx.navigation.NavController

class NavControllerNavigator(private val navController: NavController) : Navigator {
    override fun navigateTo(route: String) {
        navController.navigate(route)
    }

    override fun goBack() {
        navController.popBackStack()
    }
}
package com.walkyriasys.pyme.facturacion.ui.icons

import androidx.compose.ui.graphics.vector.ImageVector
import com.walkyriasys.pyme.facturacion.ui.icons.dashboard.IcOrders
import com.walkyriasys.pyme.facturacion.ui.icons.dashboard.IcProducts
import com.walkyriasys.pyme.facturacion.ui.icons.dashboard.IcReports
import com.walkyriasys.pyme.facturacion.ui.icons.dashboard.IcSettings
import kotlin.collections.List as ____KtList

public object Icons

private var __AllIcons: ____KtList<ImageVector>? = null

public val Icons.AllIcons: ____KtList<ImageVector>
  get() {
    if (__AllIcons != null) {
      return __AllIcons!!
    }
    __AllIcons= listOf(IcOrders, IcProducts, IcReports, IcSettings)
    return __AllIcons!!
  }

package com.walkyriasys.pyme.facturacion.ui.icons.dashboard

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType.Companion.NonZero
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap.Companion.Butt
import androidx.compose.ui.graphics.StrokeJoin.Companion.Miter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.ImageVector.Builder
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.walkyriasys.pyme.facturacion.ui.icons.Icons
import kotlin.Unit

public val Icons.IcOrders: ImageVector
    get() {
        if (_icOrders != null) {
            return _icOrders!!
        }
        _icOrders = Builder(name = "Ic-orders", defaultWidth = 800.0.dp, defaultHeight =
                800.0.dp, viewportWidth = 48.0f, viewportHeight = 48.0f).apply {
            path(fill = SolidColor(Color(0xFF8BC34A)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(43.0f, 36.0f)
                horizontalLineTo(29.0f)
                verticalLineTo(14.0f)
                horizontalLineToRelative(10.6f)
                curveToRelative(0.9f, 0.0f, 1.6f, 0.6f, 1.9f, 1.4f)
                lineTo(45.0f, 26.0f)
                verticalLineToRelative(8.0f)
                curveTo(45.0f, 35.1f, 44.1f, 36.0f, 43.0f, 36.0f)
                close()
            }
            path(fill = SolidColor(Color(0xFF388E3C)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(29.0f, 36.0f)
                horizontalLineTo(5.0f)
                curveToRelative(-1.1f, 0.0f, -2.0f, -0.9f, -2.0f, -2.0f)
                verticalLineTo(9.0f)
                curveToRelative(0.0f, -1.1f, 0.9f, -2.0f, 2.0f, -2.0f)
                horizontalLineToRelative(22.0f)
                curveToRelative(1.1f, 0.0f, 2.0f, 0.9f, 2.0f, 2.0f)
                verticalLineTo(36.0f)
                close()
            }
            path(fill = SolidColor(Color(0xFF37474F)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(37.0f, 36.0f)
                moveToRelative(-5.0f, 0.0f)
                arcToRelative(5.0f, 5.0f, 0.0f, true, true, 10.0f, 0.0f)
                arcToRelative(5.0f, 5.0f, 0.0f, true, true, -10.0f, 0.0f)
            }
            path(fill = SolidColor(Color(0xFF37474F)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(13.0f, 36.0f)
                moveToRelative(-5.0f, 0.0f)
                arcToRelative(5.0f, 5.0f, 0.0f, true, true, 10.0f, 0.0f)
                arcToRelative(5.0f, 5.0f, 0.0f, true, true, -10.0f, 0.0f)
            }
            path(fill = SolidColor(Color(0xFF78909C)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(37.0f, 36.0f)
                moveToRelative(-2.0f, 0.0f)
                arcToRelative(2.0f, 2.0f, 0.0f, true, true, 4.0f, 0.0f)
                arcToRelative(2.0f, 2.0f, 0.0f, true, true, -4.0f, 0.0f)
            }
            path(fill = SolidColor(Color(0xFF78909C)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(13.0f, 36.0f)
                moveToRelative(-2.0f, 0.0f)
                arcToRelative(2.0f, 2.0f, 0.0f, true, true, 4.0f, 0.0f)
                arcToRelative(2.0f, 2.0f, 0.0f, true, true, -4.0f, 0.0f)
            }
            path(fill = SolidColor(Color(0xFF37474F)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(41.0f, 25.0f)
                horizontalLineToRelative(-7.0f)
                curveToRelative(-0.6f, 0.0f, -1.0f, -0.4f, -1.0f, -1.0f)
                verticalLineToRelative(-7.0f)
                curveToRelative(0.0f, -0.6f, 0.4f, -1.0f, 1.0f, -1.0f)
                horizontalLineToRelative(5.3f)
                curveToRelative(0.4f, 0.0f, 0.8f, 0.3f, 0.9f, 0.7f)
                lineToRelative(1.7f, 5.2f)
                curveToRelative(0.0f, 0.1f, 0.1f, 0.2f, 0.1f, 0.3f)
                verticalLineTo(24.0f)
                curveTo(42.0f, 24.6f, 41.6f, 25.0f, 41.0f, 25.0f)
                close()
            }
            path(fill = SolidColor(Color(0xFFDCEDC8)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(21.8f, 13.8f)
                lineToRelative(-7.9f, 7.9f)
                lineToRelative(-3.7f, -3.8f)
                lineToRelative(-2.2f, 2.2f)
                lineToRelative(5.9f, 5.9f)
                lineToRelative(10.1f, -10.1f)
                close()
            }
        }
        .build()
        return _icOrders!!
    }

private var _icOrders: ImageVector? = null

@Preview
@Composable
private fun Preview(): Unit {
    Box(modifier = Modifier.padding(12.dp)) {
        Image(imageVector = Icons.IcOrders, contentDescription = "")
    }
}

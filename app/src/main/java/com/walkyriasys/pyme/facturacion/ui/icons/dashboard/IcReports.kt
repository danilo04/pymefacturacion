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

public val Icons.IcReports: ImageVector
    get() {
        if (_icReports != null) {
            return _icReports!!
        }
        _icReports = Builder(name = "Ic-reports", defaultWidth = 800.0.dp, defaultHeight =
                800.0.dp, viewportWidth = 48.0f, viewportHeight = 48.0f).apply {
            path(fill = SolidColor(Color(0xFF4CAF50)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(40.0f, 21.0f)
                horizontalLineToRelative(4.0f)
                verticalLineToRelative(23.0f)
                horizontalLineToRelative(-4.0f)
                close()
            }
            path(fill = SolidColor(Color(0xFF4CAF50)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(34.0f, 28.0f)
                horizontalLineToRelative(4.0f)
                verticalLineToRelative(16.0f)
                horizontalLineToRelative(-4.0f)
                close()
            }
            path(fill = SolidColor(Color(0xFF4CAF50)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(28.0f, 23.0f)
                horizontalLineToRelative(4.0f)
                verticalLineToRelative(21.0f)
                horizontalLineToRelative(-4.0f)
                close()
            }
            path(fill = SolidColor(Color(0xFF4CAF50)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(22.0f, 29.0f)
                horizontalLineToRelative(4.0f)
                verticalLineToRelative(15.0f)
                horizontalLineToRelative(-4.0f)
                close()
            }
            path(fill = SolidColor(Color(0xFF4CAF50)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(16.0f, 32.0f)
                horizontalLineToRelative(4.0f)
                verticalLineToRelative(12.0f)
                horizontalLineToRelative(-4.0f)
                close()
            }
            path(fill = SolidColor(Color(0xFF4CAF50)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(10.0f, 30.0f)
                horizontalLineToRelative(4.0f)
                verticalLineToRelative(14.0f)
                horizontalLineToRelative(-4.0f)
                close()
            }
            path(fill = SolidColor(Color(0xFF4CAF50)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(4.0f, 34.0f)
                horizontalLineToRelative(4.0f)
                verticalLineToRelative(10.0f)
                horizontalLineToRelative(-4.0f)
                close()
            }
            path(fill = SolidColor(Color(0xFF388E3C)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(40.1f, 9.1f)
                lineToRelative(-6.1f, 6.1f)
                lineToRelative(-4.0f, -4.0f)
                lineToRelative(-10.0f, 10.0f)
                lineToRelative(-5.0f, -5.0f)
                lineToRelative(-10.4f, 10.4f)
                lineToRelative(2.8f, 2.8f)
                lineToRelative(7.6f, -7.6f)
                lineToRelative(5.0f, 5.0f)
                lineToRelative(10.0f, -10.0f)
                lineToRelative(4.0f, 4.0f)
                lineToRelative(8.9f, -8.9f)
                close()
            }
            path(fill = SolidColor(Color(0xFF388E3C)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(44.0f, 8.0f)
                lineToRelative(-9.0f, 0.0f)
                lineToRelative(9.0f, 9.0f)
                close()
            }
        }
        .build()
        return _icReports!!
    }

private var _icReports: ImageVector? = null

@Preview
@Composable
private fun Preview(): Unit {
    Box(modifier = Modifier.padding(12.dp)) {
        Image(imageVector = Icons.IcReports, contentDescription = "")
    }
}

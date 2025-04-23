package com.walkyriasys.pyme.facturacion.ui.icons.dashboard

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType.Companion.EvenOdd
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

public val Icons.IcSettings: ImageVector
    get() {
        if (_icSettings != null) {
            return _icSettings!!
        }
        _icSettings = Builder(name = "Ic-settings", defaultWidth = 800.0.dp, defaultHeight =
                800.0.dp, viewportWidth = 24.0f, viewportHeight = 24.0f).apply {
            path(fill = SolidColor(Color(0xFF000000)), stroke = null, fillAlpha = 0.2f, strokeAlpha
                    = 0.2f, strokeLineWidth = 0.0f, strokeLineCap = Butt, strokeLineJoin = Miter,
                    strokeLineMiter = 4.0f, pathFillType = EvenOdd) {
                moveTo(11.0f, 3.0f)
                curveTo(10.448f, 3.0f, 10.0f, 3.448f, 10.0f, 4.0f)
                verticalLineTo(4.569f)
                curveTo(10.0f, 4.997f, 9.713f, 5.368f, 9.318f, 5.532f)
                curveTo(8.922f, 5.696f, 8.462f, 5.634f, 8.16f, 5.331f)
                lineTo(7.757f, 4.929f)
                curveTo(7.367f, 4.538f, 6.734f, 4.538f, 6.343f, 4.929f)
                lineTo(4.929f, 6.343f)
                curveTo(4.538f, 6.734f, 4.538f, 7.367f, 4.929f, 7.757f)
                lineTo(5.331f, 8.16f)
                curveTo(5.634f, 8.462f, 5.696f, 8.922f, 5.532f, 9.318f)
                curveTo(5.368f, 9.713f, 4.997f, 10.0f, 4.569f, 10.0f)
                lineTo(4.0f, 10.0f)
                curveTo(3.448f, 10.0f, 3.0f, 10.448f, 3.0f, 11.0f)
                verticalLineTo(13.0f)
                curveTo(3.0f, 13.552f, 3.448f, 14.0f, 4.0f, 14.0f)
                horizontalLineTo(4.569f)
                curveTo(4.997f, 14.0f, 5.368f, 14.287f, 5.532f, 14.682f)
                curveTo(5.696f, 15.078f, 5.634f, 15.538f, 5.331f, 15.84f)
                lineTo(4.929f, 16.243f)
                curveTo(4.538f, 16.633f, 4.538f, 17.266f, 4.929f, 17.657f)
                lineTo(6.343f, 19.071f)
                curveTo(6.734f, 19.462f, 7.367f, 19.462f, 7.757f, 19.071f)
                lineTo(8.16f, 18.669f)
                curveTo(8.462f, 18.366f, 8.922f, 18.304f, 9.318f, 18.468f)
                curveTo(9.713f, 18.632f, 10.0f, 19.003f, 10.0f, 19.431f)
                verticalLineTo(20.0f)
                curveTo(10.0f, 20.552f, 10.448f, 21.0f, 11.0f, 21.0f)
                horizontalLineTo(13.0f)
                curveTo(13.552f, 21.0f, 14.0f, 20.552f, 14.0f, 20.0f)
                verticalLineTo(19.431f)
                curveTo(14.0f, 19.003f, 14.287f, 18.632f, 14.682f, 18.468f)
                curveTo(15.078f, 18.304f, 15.538f, 18.366f, 15.84f, 18.669f)
                lineTo(16.243f, 19.071f)
                curveTo(16.633f, 19.462f, 17.266f, 19.462f, 17.657f, 19.071f)
                lineTo(19.071f, 17.657f)
                curveTo(19.462f, 17.266f, 19.462f, 16.633f, 19.071f, 16.243f)
                lineTo(18.669f, 15.84f)
                curveTo(18.366f, 15.538f, 18.304f, 15.078f, 18.468f, 14.682f)
                curveTo(18.632f, 14.287f, 19.003f, 14.0f, 19.431f, 14.0f)
                horizontalLineTo(20.0f)
                curveTo(20.552f, 14.0f, 21.0f, 13.552f, 21.0f, 13.0f)
                verticalLineTo(11.0f)
                curveTo(21.0f, 10.448f, 20.552f, 10.0f, 20.0f, 10.0f)
                lineTo(19.431f, 10.0f)
                curveTo(19.003f, 10.0f, 18.632f, 9.713f, 18.468f, 9.318f)
                curveTo(18.304f, 8.922f, 18.366f, 8.462f, 18.669f, 8.16f)
                lineTo(19.071f, 7.757f)
                curveTo(19.462f, 7.367f, 19.462f, 6.734f, 19.071f, 6.343f)
                lineTo(17.657f, 4.929f)
                curveTo(17.266f, 4.538f, 16.633f, 4.538f, 16.243f, 4.929f)
                lineTo(15.84f, 5.331f)
                curveTo(15.538f, 5.634f, 15.078f, 5.696f, 14.682f, 5.532f)
                curveTo(14.287f, 5.368f, 14.0f, 4.997f, 14.0f, 4.569f)
                verticalLineTo(4.0f)
                curveTo(14.0f, 3.448f, 13.552f, 3.0f, 13.0f, 3.0f)
                horizontalLineTo(11.0f)
                close()
                moveTo(12.0f, 14.0f)
                curveTo(13.105f, 14.0f, 14.0f, 13.105f, 14.0f, 12.0f)
                curveTo(14.0f, 10.895f, 13.105f, 10.0f, 12.0f, 10.0f)
                curveTo(10.895f, 10.0f, 10.0f, 10.895f, 10.0f, 12.0f)
                curveTo(10.0f, 13.105f, 10.895f, 14.0f, 12.0f, 14.0f)
                close()
            }
            path(fill = SolidColor(Color(0x00000000)), stroke = SolidColor(Color(0xFF000000)),
                    strokeLineWidth = 1.5f, strokeLineCap = Butt, strokeLineJoin = Miter,
                    strokeLineMiter = 4.0f, pathFillType = NonZero) {
                moveTo(11.0f, 3.0f)
                horizontalLineTo(13.0f)
                curveTo(13.552f, 3.0f, 14.0f, 3.448f, 14.0f, 4.0f)
                verticalLineTo(4.569f)
                curveTo(14.0f, 4.997f, 14.287f, 5.368f, 14.682f, 5.532f)
                curveTo(15.078f, 5.696f, 15.538f, 5.634f, 15.84f, 5.331f)
                lineTo(16.243f, 4.929f)
                curveTo(16.633f, 4.538f, 17.266f, 4.538f, 17.657f, 4.929f)
                lineTo(19.071f, 6.343f)
                curveTo(19.462f, 6.734f, 19.462f, 7.367f, 19.071f, 7.757f)
                lineTo(18.669f, 8.16f)
                curveTo(18.366f, 8.462f, 18.304f, 8.922f, 18.468f, 9.318f)
                curveTo(18.632f, 9.713f, 19.003f, 10.0f, 19.431f, 10.0f)
                lineTo(20.0f, 10.0f)
                curveTo(20.552f, 10.0f, 21.0f, 10.448f, 21.0f, 11.0f)
                verticalLineTo(13.0f)
                curveTo(21.0f, 13.552f, 20.552f, 14.0f, 20.0f, 14.0f)
                horizontalLineTo(19.431f)
                curveTo(19.003f, 14.0f, 18.632f, 14.287f, 18.468f, 14.682f)
                curveTo(18.304f, 15.078f, 18.366f, 15.538f, 18.669f, 15.84f)
                lineTo(19.071f, 16.243f)
                curveTo(19.462f, 16.633f, 19.462f, 17.266f, 19.071f, 17.657f)
                lineTo(17.657f, 19.071f)
                curveTo(17.266f, 19.462f, 16.633f, 19.462f, 16.243f, 19.071f)
                lineTo(15.84f, 18.669f)
                curveTo(15.538f, 18.366f, 15.078f, 18.304f, 14.682f, 18.468f)
                curveTo(14.287f, 18.632f, 14.0f, 19.003f, 14.0f, 19.431f)
                verticalLineTo(20.0f)
                curveTo(14.0f, 20.552f, 13.552f, 21.0f, 13.0f, 21.0f)
                horizontalLineTo(11.0f)
                curveTo(10.448f, 21.0f, 10.0f, 20.552f, 10.0f, 20.0f)
                verticalLineTo(19.431f)
                curveTo(10.0f, 19.003f, 9.713f, 18.632f, 9.318f, 18.468f)
                curveTo(8.922f, 18.304f, 8.462f, 18.366f, 8.16f, 18.669f)
                lineTo(7.757f, 19.071f)
                curveTo(7.367f, 19.462f, 6.734f, 19.462f, 6.343f, 19.071f)
                lineTo(4.929f, 17.657f)
                curveTo(4.538f, 17.266f, 4.538f, 16.633f, 4.929f, 16.243f)
                lineTo(5.331f, 15.84f)
                curveTo(5.634f, 15.538f, 5.696f, 15.078f, 5.532f, 14.682f)
                curveTo(5.368f, 14.287f, 4.997f, 14.0f, 4.569f, 14.0f)
                horizontalLineTo(4.0f)
                curveTo(3.448f, 14.0f, 3.0f, 13.552f, 3.0f, 13.0f)
                verticalLineTo(11.0f)
                curveTo(3.0f, 10.448f, 3.448f, 10.0f, 4.0f, 10.0f)
                lineTo(4.569f, 10.0f)
                curveTo(4.997f, 10.0f, 5.368f, 9.713f, 5.532f, 9.318f)
                curveTo(5.696f, 8.922f, 5.634f, 8.462f, 5.331f, 8.16f)
                lineTo(4.929f, 7.757f)
                curveTo(4.538f, 7.367f, 4.538f, 6.734f, 4.929f, 6.343f)
                lineTo(6.343f, 4.929f)
                curveTo(6.734f, 4.538f, 7.367f, 4.538f, 7.757f, 4.929f)
                lineTo(8.16f, 5.331f)
                curveTo(8.462f, 5.634f, 8.922f, 5.696f, 9.318f, 5.532f)
                curveTo(9.713f, 5.368f, 10.0f, 4.997f, 10.0f, 4.569f)
                verticalLineTo(4.0f)
                curveTo(10.0f, 3.448f, 10.448f, 3.0f, 11.0f, 3.0f)
                close()
            }
            path(fill = SolidColor(Color(0x00000000)), stroke = SolidColor(Color(0xFF000000)),
                    strokeLineWidth = 1.5f, strokeLineCap = Butt, strokeLineJoin = Miter,
                    strokeLineMiter = 4.0f, pathFillType = NonZero) {
                moveTo(14.0f, 12.0f)
                curveTo(14.0f, 13.105f, 13.105f, 14.0f, 12.0f, 14.0f)
                curveTo(10.895f, 14.0f, 10.0f, 13.105f, 10.0f, 12.0f)
                curveTo(10.0f, 10.895f, 10.895f, 10.0f, 12.0f, 10.0f)
                curveTo(13.105f, 10.0f, 14.0f, 10.895f, 14.0f, 12.0f)
                close()
            }
        }
        .build()
        return _icSettings!!
    }

private var _icSettings: ImageVector? = null

@Preview
@Composable
private fun Preview(): Unit {
    Box(modifier = Modifier.padding(12.dp)) {
        Image(imageVector = Icons.IcSettings, contentDescription = "")
    }
}

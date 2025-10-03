package com.walkyriasys.pyme.facturacion.domain.print.drivers.netum

import android.graphics.Bitmap
import androidx.core.graphics.get
import zj.com.customize.sdk.Other
import kotlin.experimental.or

object PrintPicture {
    /**
     * 打印位图函数
     * 此函数是将一行作为一个图片打印，这样处理不容易出错
     * @param mBitmap
     * @param nWidth
     * @param nMode
     * @return
     */
    fun POS_PrintBMP(mBitmap: Bitmap, nWidth: Int, nMode: Int): ByteArray {
        // 先转黑白，再调用函数缩放位图
        val width = ((nWidth + 7) / 8) * 8
        var height = mBitmap.getHeight() * width / mBitmap.getWidth()
        height = ((height + 7) / 8) * 8

        var rszBitmap = mBitmap
        if (mBitmap.getWidth() != width) {
            rszBitmap = Other.resizeImage(mBitmap, width, height)
        }

        val grayBitmap = Other.toGrayscale(rszBitmap)

        val dithered = Other.thresholdToBWPic(grayBitmap)

        val data = Other.eachLinePixToCmd(dithered, width, nMode)

        return data
    }

    /**
     * 使用下传位图打印图片
     * 先收完再打印
     * @param bmp
     * @return
     */
    fun Print_1D2A(bmp: Bitmap): ByteArray? {
        /*
                     * 使用下传位图打印图片
                     * 先收完再打印
                     */

        val width = bmp.getWidth()
        val height = bmp.getHeight()
        val data: ByteArray? = ByteArray(1024 * 10)
        data!![0] = 0x1D
        data[1] = 0x2A
        data[2] = ((width - 1) / 8 + 1).toByte()
        data[3] = ((height - 1) / 8 + 1).toByte()
        var k: Byte = 0
        var position = 4
        var i: Int
        var j: Int
        var temp: Byte = 0
        i = 0
        while (i < width) {
            println("进来了...I")
            j = 0
            while (j < height) {
                println("进来了...J")
                if (bmp[i, j] != -1) {
                    temp = temp or ((0x80 shr k.toInt()).toByte())
                } // end if

                k++
                if (k.toInt() == 8) {
                    data[position++] = temp
                    temp = 0
                    k = 0
                } // end if k

                j++
            }
            if (k % 8 != 0) {
                data[position++] = temp
                temp = 0
                k = 0
            }

            i++
        }
        println("data" + data)

        if (width % 8 != 0) {
            i = height / 8
            if (height % 8 != 0) i++
            j = 8 - (width % 8)
            k = 0
            while (k < i * j) {
                data[position++] = 0
                k++
            }
        }
        return data
    }
}
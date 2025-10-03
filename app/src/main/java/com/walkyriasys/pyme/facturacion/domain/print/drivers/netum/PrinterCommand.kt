package com.walkyriasys.pyme.facturacion.domain.print.drivers.netum

import zj.com.customize.sdk.Other
import java.io.UnsupportedEncodingException

object PrinterCommand {
    /**
     * 打印机初始化
     * @return
     */
    fun POS_Set_PrtInit(): ByteArray {
        val data = Other.byteArraysToBytes(arrayOf<ByteArray?>(Command.ESC_Init))

        return data
    }

    /**
     * 打印并换行
     * @return
     */
    fun POS_Set_LF(): ByteArray {
        val data = Other.byteArraysToBytes(arrayOf<ByteArray?>(Command.LF))

        return data
    }

    /**
     * 打印并走纸 (0~255)
     * @param feed
     * @return
     */
    fun POS_Set_PrtAndFeedPaper(feed: Int): ByteArray? {
        if ((feed > 255) or (feed < 0)) return null

        Command.ESC_J[2] = feed.toByte()

        val data = Other.byteArraysToBytes(arrayOf<ByteArray?>(Command.ESC_J))

        return data
    }

    /**
     * 打印自检页
     * @return
     */
    fun POS_Set_PrtSelfTest(): ByteArray {
        val data = Other.byteArraysToBytes(
            arrayOf<ByteArray?>(
                Command.US_vt_eot
            )
        )
        return data
    }

    /**
     * 蜂鸣指令
     * @param m  蜂鸣次数
     * @param t  每次蜂鸣的时间
     * @return
     */
    fun POS_Set_Beep(m: Int, t: Int): ByteArray? {
        if ((m < 1 || m > 9) or (t < 1 || t > 9)) return null

        Command.ESC_B_m_n[2] = m.toByte()
        Command.ESC_B_m_n[3] = t.toByte()

        val data = Other.byteArraysToBytes(
            arrayOf<ByteArray?>(
                Command.ESC_B_m_n
            )
        )
        return data
    }

    /**
     * 切刀指令(走纸到切刀位置并切纸)
     * @param cut  0~255
     * @return
     */
    fun POS_Set_Cut(cut: Int): ByteArray? {
        if ((cut > 255) or (cut < 0)) return null

        Command.GS_V_m_n[3] = cut.toByte()
        val data = Other.byteArraysToBytes(
            arrayOf<ByteArray?>(
                Command.GS_V_m_n
            )
        )
        return data
    }

    /**
     * 钱箱指令
     * @param nMode
     * @param nTime1
     * @param nTime2
     * @return
     */
    fun POS_Set_Cashbox(nMode: Int, nTime1: Int, nTime2: Int): ByteArray? {
        if ((nMode < 0 || nMode > 1) or (nTime1 < 0) or (nTime1 > 255) or (nTime2 < 0) or (nTime2 > 255)) return null
        Command.ESC_p[2] = nMode.toByte()
        Command.ESC_p[3] = nTime1.toByte()
        Command.ESC_p[4] = nTime2.toByte()

        val data = Other.byteArraysToBytes(
            arrayOf<ByteArray?>(
                Command.ESC_p
            )
        )
        return data
    }

    /**
     * 设置绝对打印位置
     * @param absolute
     * @return
     */
    fun POS_Set_Absolute(absolute: Int): ByteArray? {
        if ((absolute > 65535) or (absolute < 0)) return null

        Command.ESC_Relative[2] = (absolute % 0x100).toByte()
        Command.ESC_Relative[3] = (absolute / 0x100).toByte()

        val data = Other.byteArraysToBytes(
            arrayOf<ByteArray?>(
                Command.ESC_Relative
            )
        )
        return data
    }

    /**
     * 设置相对打印位置
     * @param relative
     * @return
     */
    fun POS_Set_Relative(relative: Int): ByteArray? {
        if ((relative < 0) or (relative > 65535)) return null

        Command.ESC_Absolute[2] = (relative % 0x100).toByte()
        Command.ESC_Absolute[3] = (relative / 0x100).toByte()

        val data = Other.byteArraysToBytes(
            arrayOf<ByteArray?>(
                Command.ESC_Absolute
            )
        )
        return data
    }

    /**
     * 设置左边距
     * @param left
     * @return
     */
    fun POS_Set_LeftSP(left: Int): ByteArray? {
        if ((left > 255) or (left < 0)) return null

        Command.GS_LeftSp[2] = (left % 100).toByte()
        Command.GS_LeftSp[3] = (left / 100).toByte()

        val data = Other.byteArraysToBytes(
            arrayOf<ByteArray?>(
                Command.GS_LeftSp
            )
        )
        return data
    }

    /**
     * 设置对齐模式
     * @param align
     * @return
     */
    fun POS_S_Align(align: Int): ByteArray? {
        if ((align < 0 || align > 2) or (align < 48 || align > 50)) return null

        val data = Command.ESC_Align
        data[2] = align.toByte()
        return data
    }

    /**
     * 设置打印区域宽度
     * @param width
     * @return
     */
    fun POS_Set_PrintWidth(width: Int): ByteArray? {
        if ((width < 0) or (width > 255)) return null

        Command.GS_W[2] = (width % 100).toByte()
        Command.GS_W[3] = (width / 100).toByte()

        val data = Other.byteArraysToBytes(
            arrayOf<ByteArray?>(
                Command.GS_W
            )
        )
        return data
    }

    /**
     * 设置默认行间距
     * @return
     */
    fun POS_Set_DefLineSpace(): ByteArray {
        val data = Command.ESC_Two
        return data
    }

    /**
     * 设置行间距
     * @param space
     * @return
     */
    fun POS_Set_LineSpace(space: Int): ByteArray? {
        if ((space < 0) or (space > 255)) return null

        Command.ESC_Three[2] = space.toByte()

        val data = Other.byteArraysToBytes(
            arrayOf<ByteArray?>(
                Command.ESC_Three
            )
        )
        return data
    }

    /**
     * 选择字符代码页
     * @param page
     * @return
     */
    fun POS_Set_CodePage(page: Int): ByteArray? {
        if (page > 255) return null

        Command.ESC_t[2] = page.toByte()

        val data = Other.byteArraysToBytes(
            arrayOf<ByteArray?>(
                Command.ESC_t
            )
        )

        return data
    }

    /**
     * 打印文本文档
     * @param codepage      设置代码页
     * @param pszString    要打印的字符串
     * @param encoding    打印字符对应编码
     */
    fun POS_Print_Text(codepage: Int, pszString: String?, encoding: String): ByteArray? {
        if (codepage < 0 || codepage > 255 || pszString == null || "" == pszString || pszString.length < 1) {
            return null
        }

        var pbString: ByteArray? = null
        try {
            pbString = pszString.toByteArray(charset(encoding))
        } catch (e: UnsupportedEncodingException) {
            return null
        }

        Command.ESC_t[2] = codepage.toByte()

        if (codepage == 0) {
            val data = Other.byteArraysToBytes(
                arrayOf<ByteArray?>(
                    Command.ESC_t,
                    Command.FS_and,
                    pbString
                )
            )

            return data
        } else {
            val data = Other.byteArraysToBytes(
                arrayOf<ByteArray?>(
                    Command.ESC_t,
                    Command.FS_dot,
                    pbString
                )
            )

            return data
        }
    }

    /**
     * 加粗指令(最低位为1有效)
     * @param bold
     * @return
     */
    fun POS_Set_Bold(bold: Int): ByteArray {
        Command.ESC_E[2] = bold.toByte()
        Command.ESC_G[2] = bold.toByte()

        val data = Other.byteArraysToBytes(
            arrayOf<ByteArray?>(
                Command.ESC_E, Command.ESC_G
            )
        )
        return data
    }

    /**
     * 设置倒置打印模式(当最低位为1时有效)
     * @param brace
     * @return
     */
    fun POS_Set_LeftBrace(brace: Int): ByteArray {
        Command.ESC_LeftBrace[2] = brace.toByte()
        val data = Other.byteArraysToBytes(
            arrayOf<ByteArray?>(
                Command.ESC_LeftBrace
            )
        )
        return data
    }

    /**
     * 设置下划线
     * @param line
     * @return
     */
    fun POS_Set_UnderLine(line: Int): ByteArray? {
        if ((line < 0 || line > 2)) return null

        Command.ESC_Minus[2] = line.toByte()
        Command.FS_Minus[2] = line.toByte()

        val data = Other.byteArraysToBytes(
            arrayOf<ByteArray?>(
                Command.ESC_Minus, Command.FS_Minus
            )
        )
        return data
    }

    /**
     * 选择字体大小(倍高倍宽)
     * @param size
     * @return
     */
    fun POS_Set_FontSize(size1: Int, size2: Int): ByteArray? {
        if ((size1 < 0) or (size1 > 7) or (size2 < 0) or (size2 > 7)) return null

        val intToWidth = byteArrayOf(0x00, 0x10, 0x20, 0x30, 0x40, 0x50, 0x60, 0x70)
        val intToHeight = byteArrayOf(0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07)
        Command.GS_ExclamationMark[2] = (intToWidth[size1] + intToHeight[size2]).toByte()
        val data = Other.byteArraysToBytes(
            arrayOf<ByteArray?>(
                Command.GS_ExclamationMark
            )
        )
        return data
    }

    /**
     * 设置反显打印
     * @param inverse
     * @return
     */
    fun POS_Set_Inverse(inverse: Int): ByteArray {
        Command.GS_B[2] = inverse.toByte()

        val data = Other.byteArraysToBytes(
            arrayOf<ByteArray?>(
                Command.GS_B
            )
        )

        return data
    }

    /**
     * 设置旋转90度打印
     * @param rotate
     * @return
     */
    fun POS_Set_Rotate(rotate: Int): ByteArray? {
        if (rotate < 0 || rotate > 1) return null
        Command.ESC_V[2] = rotate.toByte()
        val data = Other.byteArraysToBytes(
            arrayOf<ByteArray?>(
                Command.ESC_V
            )
        )
        return data
    }

    /**
     * 选择字体字型
     * @param font
     * @return
     */
    fun POS_Set_ChoseFont(font: Int): ByteArray? {
        if ((font > 1) or (font < 0)) return null

        Command.ESC_M[2] = font.toByte()
        val data = Other.byteArraysToBytes(
            arrayOf<ByteArray?>(
                Command.ESC_M
            )
        )
        return data
    }

    //***********************************以下函数为公开函数***********************************************************//
    /**
     * 二维码打印函数
     * @param str                     打印二维码数据
     * @param nVersion                      二维码类型
     * @param nErrorCorrectionLevel   纠错级别
     * @param nMagnification          放大倍数
     * @return
     */
    fun getBarCommand(
        str: String, nVersion: Int, nErrorCorrectionLevel: Int,
        nMagnification: Int
    ): ByteArray? {
        if ((nVersion < 0) or (nVersion > 19) or (nErrorCorrectionLevel < 0) or (nErrorCorrectionLevel > 3
                    ) or (nMagnification < 1) or (nMagnification > 8)
        ) {
            return null
        }

        var bCodeData: ByteArray? = null
        try {
            bCodeData = str.toByteArray(charset("GBK"))
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
            return null
        }

        val command = ByteArray(bCodeData.size + 7)

        command[0] = 27
        command[1] = 90
        command[2] = (nVersion.toByte())
        command[3] = (nErrorCorrectionLevel.toByte())
        command[4] = (nMagnification.toByte())
        command[5] = (bCodeData.size and 0xff).toByte()
        command[6] = ((bCodeData.size and 0xff00) shr 8).toByte()
        System.arraycopy(bCodeData, 0, command, 7, bCodeData.size)

        return command
    }

    /**
     * 打印一维条码
     * @param str                   打印条码字符
     * @param nType                    条码类型(65~73)
     * @param nWidthX                条码宽度
     * @param nHeight                条码高度
     * @param nHriFontType            HRI字型
     * @param nHriFontPosition        HRI位置
     * @return
     */
    fun getCodeBarCommand(
        str: String, nType: Int, nWidthX: Int, nHeight: Int,
        nHriFontType: Int, nHriFontPosition: Int
    ): ByteArray? {
        if ((nType < 0x41) or (nType > 0x49) or (nWidthX < 2) or (nWidthX > 6
                    ) or (nHeight < 1) or (nHeight > 255) or (str.length == 0)
        ) return null

        var bCodeData: ByteArray? = null
        try {
            bCodeData = str.toByteArray(charset("GBK"))
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
            return null
        }

        val command = ByteArray(bCodeData.size + 16)

        command[0] = 29
        command[1] = 119
        command[2] = (nWidthX.toByte())
        command[3] = 29
        command[4] = 104
        command[5] = (nHeight.toByte())
        command[6] = 29
        command[7] = 102
        command[8] = ((nHriFontType and 0x01).toByte())
        command[9] = 29
        command[10] = 72
        command[11] = ((nHriFontPosition and 0x03).toByte())
        command[12] = 29
        command[13] = 107
        command[14] = (nType.toByte())
        command[15] = bCodeData.size.toByte()
        System.arraycopy(bCodeData, 0, command, 16, bCodeData.size)


        return command
    }

    /**
     * 设置打印模式(选择字体(font:A font:B),加粗,字体倍高倍宽(最大4倍高宽))
     * @param str          打印的字符串
     * @param bold                 加粗
     * @param font                 选择字型
     * @param widthsize    倍宽
     * @param heigthsize   倍高
     * @return
     */
    fun POS_Set_Font(
        str: String,
        bold: Int,
        font: Int,
        widthsize: Int,
        heigthsize: Int
    ): ByteArray? {
        if ((str.length == 0) or (widthsize < 0) or (widthsize > 4) or (heigthsize < 0) or (heigthsize > 4
                    ) or (font < 0) or (font > 1)
        ) return null

        var strData: ByteArray? = null
        try {
            strData = str.toByteArray(charset("GBK"))
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
            return null
        }

        val command = ByteArray(strData.size + 9)

        val intToWidth = byteArrayOf(0x00, 0x10, 0x20, 0x30) //最大四倍宽
        val intToHeight = byteArrayOf(0x00, 0x01, 0x02, 0x03) //最大四倍高

        command[0] = 27
        command[1] = 69
        command[2] = (bold.toByte())
        command[3] = 27
        command[4] = 77
        command[5] = (font.toByte())
        command[6] = 29
        command[7] = 33
        command[8] = (intToWidth[widthsize] + intToHeight[heigthsize]).toByte()

        System.arraycopy(strData, 0, command, 9, strData.size)
        return command
    } //**********************************************************************************************************//
}
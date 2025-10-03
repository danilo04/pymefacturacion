package com.walkyriasys.pyme.facturacion.domain.print.drivers.netum

object Command {
    //打印机初始化
    var ESC_Init: ByteArray = byteArrayOf(0x1b, 0x40)

    /**
     * 打印命令
     */
    //打印并换行
    var LF: ByteArray = byteArrayOf(0x0a)

    //打印并走纸
    var ESC_J: ByteArray = byteArrayOf(0x1b, 0x4a, 0x00)
    var ESC_d: ByteArray = byteArrayOf(0x1b, 0x64, 0x00)

    //打印自检页
    var US_vt_eot: ByteArray = byteArrayOf(0x1f, 0x11, 0x04)

    //蜂鸣指令
    var ESC_B_m_n: ByteArray = byteArrayOf(0x1b, 0x42, 0x00, 0x00)

    //切刀指令
    var GS_V_n: ByteArray = byteArrayOf(0x1d, 0x56, 0x00)
    var GS_V_m_n: ByteArray = byteArrayOf(0x1d, 0x56, 0x42, 0x00)
    var GS_i: ByteArray = byteArrayOf(0x1b, 0x69)
    var GS_m: ByteArray = byteArrayOf(0x1b, 0x6d)

    /**
     * 字符设置命令
     */
    //设置字符右间距
    var ESC_SP: ByteArray = byteArrayOf(0x1b, 0x20, 0x00)

    //设置字符打印字体格式
    var ESC_ExclamationMark: ByteArray = byteArrayOf(0x1b, 0x21, 0x00)

    //设置字体倍高倍宽
    var GS_ExclamationMark: ByteArray = byteArrayOf(0x1d, 0x21, 0x00)

    //设置反显打印
    var GS_B: ByteArray = byteArrayOf(0x1d, 0x42, 0x00)

    //取消/选择90度旋转打印
    var ESC_V: ByteArray = byteArrayOf(0x1b, 0x56, 0x00)

    //选择字体字型(主要是ASCII码)
    var ESC_M: ByteArray = byteArrayOf(0x1b, 0x4d, 0x00)

    //选择/取消加粗指令
    var ESC_G: ByteArray = byteArrayOf(0x1b, 0x47, 0x00)
    var ESC_E: ByteArray = byteArrayOf(0x1b, 0x45, 0x00)

    //选择/取消倒置打印模式
    var ESC_LeftBrace: ByteArray = byteArrayOf(0x1b, 0x7b, 0x00)

    //设置下划线点高度(字符)
    var ESC_Minus: ByteArray = byteArrayOf(0x1b, 0x2d, 0x00)

    //字符模式
    var FS_dot: ByteArray = byteArrayOf(0x1c, 0x2e)

    //汉字模式
    var FS_and: ByteArray = byteArrayOf(0x1c, 0x26)

    //设置汉字打印模式
    var FS_ExclamationMark: ByteArray = byteArrayOf(0x1c, 0x21, 0x00)

    //设置下划线点高度(汉字)
    var FS_Minus: ByteArray = byteArrayOf(0x1c, 0x2d, 0x00)

    //设置汉字左右间距
    var FS_S: ByteArray = byteArrayOf(0x1c, 0x53, 0x00, 0x00)

    //选择字符代码页
    var ESC_t: ByteArray = byteArrayOf(0x1b, 0x74, 0x00)

    /**
     * 格式设置指令
     */
    //设置默认行间距
    var ESC_Two: ByteArray = byteArrayOf(0x1b, 0x32)

    //设置行间距
    var ESC_Three: ByteArray = byteArrayOf(0x1b, 0x33, 0x00)

    //设置对齐模式
    var ESC_Align: ByteArray = byteArrayOf(0x1b, 0x61, 0x00)

    //设置左边距
    var GS_LeftSp: ByteArray = byteArrayOf(0x1d, 0x4c, 0x00, 0x00)

    //设置绝对打印位置
    //将当前位置设置到距离行首（nL + nH x 256）处。
    //如果设置位置在指定打印区域外，该命令被忽略
    var ESC_Relative: ByteArray = byteArrayOf(0x1b, 0x24, 0x00, 0x00)

    //设置相对打印位置
    var ESC_Absolute: ByteArray = byteArrayOf(0x1b, 0x5c, 0x00, 0x00)

    //设置打印区域宽度
    var GS_W: ByteArray = byteArrayOf(0x1d, 0x57, 0x00, 0x00)

    /**
     * 状态指令
     */
    //实时状态传送指令
    var DLE_eot: ByteArray = byteArrayOf(0x10, 0x04, 0x00)

    //实时弹钱箱指令
    var DLE_DC4: ByteArray = byteArrayOf(0x10, 0x14, 0x00, 0x00, 0x00)

    //标准弹钱箱指令
    var ESC_p: ByteArray = byteArrayOf(0x1b, 0x70, 0x00, 0x00, 0x00)

    /**
     * 条码设置指令
     */
    //选择HRI打印方式
    var GS_H: ByteArray = byteArrayOf(0x1d, 0x48, 0x00)

    //设置条码高度
    var GS_h: ByteArray = byteArrayOf(0x1d, 0x68, 0xa2.toByte())

    //设置条码宽度
    var GS_w: ByteArray = byteArrayOf(0x1d, 0x77, 0x00)

    //设置HRI字符字体字型
    var GS_f: ByteArray = byteArrayOf(0x1d, 0x66, 0x00)

    //条码左偏移指令
    var GS_x: ByteArray = byteArrayOf(0x1d, 0x78, 0x00)

    //打印条码指令
    var GS_k: ByteArray = byteArrayOf(0x1d, 0x6b, 0x41, 0x0c)

    //二维码相关指令
    var GS_k_m_v_r_nL_nH: ByteArray = byteArrayOf(0x1b, 0x5a, 0x03, 0x03, 0x08, 0x00, 0x00)
}
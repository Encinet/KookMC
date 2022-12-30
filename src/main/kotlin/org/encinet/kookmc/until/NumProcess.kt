package org.encinet.kookmc.until

import java.text.DecimalFormat

class NumProcess {
    companion object {
        fun unitByte(value: Long): String {
            if (value < 0) return "NaN"
            val standard = 1024.0
            val df = DecimalFormat("#.00")
            if (value <= standard) {
                return df.format(value) + "B"
            }
            val m = standard * standard
            if (value <= m) {
                return df.format(value / standard) + "KB"
            }
            val g = m * standard
            return if (value <= g) {
                df.format(value / m) + "MB"
            } else df.format(value / g) + "GB"
        }
    }
}
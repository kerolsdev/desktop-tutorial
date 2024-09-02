package com.kerols.phoneboost.Utils


object Converter {
    var mega: Long = 1024
    var giga = mega * 1024
    var tera = giga * 1024
    fun getSize(size: Long): String {
        var s = ""
        val mb = size.toDouble()
        val gb = mb / 1024
        val tb = gb / 1024
        if (size <= mega && size < giga) {
            s = String.format("%.2f", mb) + " MB"
        } else if (size <= giga && size < tera) {
            s = String.format("%.2f", gb) + " GB"
        } else if (size <= tera) {
            s = String.format("%.2f", tb) + " TB"
        }
        return s
    }
}
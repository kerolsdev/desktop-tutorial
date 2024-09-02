package com.kerols.phoneboost.Utils


object ConverterSize {
    var kilo: Long = 1024
    var mega = kilo * kilo
    var giga = mega * kilo
    var tera = giga * kilo
    fun getSize(size: Long): String {
        var s = ""
        val kb = size.toDouble() / kilo
        val mb = kb / kilo
        val gb = mb / kilo
        val tb = gb / kilo
        if (size < kilo) {
            s = "$size Bytes"
        } else if (size >= kilo && size < mega) {
            s = String.format("%.2f", kb) + " KB"
        } else if (size >= mega && size < giga) {
            s = String.format("%.2f", mb) + " MB"
        } else if (size >= giga && size < tera) {
            s = String.format("%.2f", gb) + " GB"
        } else if (size >= tera) {
            s = String.format("%.2f", tb) + " TB"
        }
        return s
    }
}
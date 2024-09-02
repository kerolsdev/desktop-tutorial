package com.kerols.phoneboost.Utils


class Constes {

    // what else should we count as an audio except "audio/*" mimetype
    val extraAudioMimeTypes = arrayListOf("application/ogg")
    val extraDocumentMimeTypes = arrayListOf(
        "application/pdf",
        "application/msword",
        "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
        "application/javascript"
    )

    val archiveMimeTypes = arrayListOf(
        "application/zip",
        "application/octet-stream",
        "application/json",
        "application/x-tar",
        "application/x-rar-compressed",
        "application/x-zip-compressed",
        "application/x-7z-compressed",
        "application/x-compressed",
        "application/x-gzip",
        "application/java-archive",
        "multipart/x-zip"
    )



    companion object {

        const val IMAGES = "image"
        const val VIDEOS = "video"
        const val AUDIO = "audio"
        const val DOCUMENTS = "documents"
        const val ARCHIVES = "archives"
        const val OTHERS = "others"
    }
}
package com.kerols.phoneboost.Utils

import android.os.Environment
import android.os.StatFs
import java.io.File

class StorageSize {

    fun  getStorageApiBelow ()  : Long {
        val iPath: File = Environment.getDataDirectory()
        val iStat = StatFs(iPath.path)
        val iBlockSize = iStat.blockSizeLong
        val iAvailableBlocks = iStat.availableBlocksLong
        val iTotalBlocks = iStat.blockCountLong
        //Available
        return  iAvailableBlocks * iBlockSize


    }
    fun  getStorageApiBelowTotal ()  : Long {
        val iPath: File = Environment.getDataDirectory()
        val iStat = StatFs(iPath.path)
        val iBlockSize = iStat.blockSizeLong
        val iAvailableBlocks = iStat.availableBlocksLong
        val iTotalBlocks = iStat.blockCountLong
        //Available
        return  iTotalBlocks * iBlockSize

    }


}
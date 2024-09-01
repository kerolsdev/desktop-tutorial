package com.kerolsmm.appmanager.dialogs

import android.annotation.SuppressLint
import android.app.usage.StorageStatsManager
import android.content.Context
import android.content.pm.ApplicationInfo
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Process
import android.os.storage.StorageManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.kerolsmm.appmanager.databinding.BottomAppSizeDialogBinding
import android.text.format.Formatter.formatFileSize
import java.io.File

class BottomDialogAppSize(var packageName: String?, var applicationInfo: ApplicationInfo?) : BottomSheetDialogFragment()
{

    constructor() : this(null,null)


    private lateinit var binding : BottomAppSizeDialogBinding
    private  var runUi : Handler = Handler(Looper.getMainLooper())


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BottomAppSizeDialogBinding.inflate(inflater,container,false)
        return binding.root
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Thread(Runnable {

        val storageStatsManager = context?.getSystemService(Context.STORAGE_STATS_SERVICE) as StorageStatsManager


        val appStorageStats = packageName?.let {
            storageStatsManager.queryStatsForPackage (StorageManager.UUID_DEFAULT,
                it, Process.myUserHandle() )
        }

            val file = applicationInfo?.sourceDir?.let { File(it) }
            val size = file?.length()

            runUi.post(Runnable {
                try {

                binding.appCache.text = formatFileSize(requireActivity(),appStorageStats?.cacheBytes ?: 0)
                binding.appSize.text =  formatFileSize(requireActivity(),appStorageStats?.appBytes ?: 0)
                binding.appData.text = formatFileSize(requireActivity(),appStorageStats?.dataBytes ?: 0)
                binding.apkSize.text = formatFileSize(requireActivity(),size ?: 0)

                }catch (re : RuntimeException) {

                }
            })



        }).start()





    }





}
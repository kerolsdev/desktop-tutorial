package com.kerols.appmanager.dialogs

import android.annotation.SuppressLint
import android.app.usage.StorageStatsManager
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Process
import android.os.storage.StorageManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.kerols.appmanager.databinding.BottomAppSizeDialogBinding
import android.text.format.Formatter
import android.text.format.Formatter.formatFileSize

class BottomDialogAppSize(var packageName : String) : BottomSheetDialogFragment()
{





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


        val appStorageStats = storageStatsManager.queryStatsForPackage (StorageManager.UUID_DEFAULT, packageName, Process.myUserHandle() )


            runUi.post(Runnable {
                try {

                binding.appCache.text = formatFileSize(requireActivity(),appStorageStats.cacheBytes)
                binding.appSize.text =  formatFileSize(requireActivity(),appStorageStats.appBytes)
                binding.appData.text = formatFileSize(requireActivity(),appStorageStats.dataBytes)

                }catch (re : RuntimeException) {

                }
            })



        }).start()





    }





}
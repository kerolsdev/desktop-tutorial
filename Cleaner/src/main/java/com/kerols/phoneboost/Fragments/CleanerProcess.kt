package com.kerols.phoneboost.Fragments

import android.content.Intent
import android.os.*
import android.os.storage.StorageManager.ACTION_CLEAR_APP_CACHE
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

import com.kerols.phoneboost.Utils.FileScanner
import com.kerols.phoneboost.Activitys.MainActivity.Companion.runUi
import com.kerols.phoneboost.Model.QuickCleanerModel
import com.kerols.phoneboost.Utils.StorageSize
import com.kerols.phoneboost.databinding.FragmentCleanerProcessBinding

class CleanerProcess : Fragment() {

    private lateinit var binding : FragmentCleanerProcessBinding
    private lateinit var  resultLauncher : ActivityResultLauncher<Intent>

    private var mTotalStorage : Long = 0
    private var mTotalStorageAfter : Long = 0
    private val TAG : String = "App"


    companion object {
        var isRunningCleaner : Boolean = false
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
          binding = FragmentCleanerProcessBinding.inflate(inflater, container, false)
          return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args: CleanerProcessArgs? = arguments?.let { CleanerProcessArgs.fromBundle(it) }
        val quickCleanerModel = QuickCleanerModel(args?.appcleaninfo?.get(0) ?: false,
            args?.appcleaninfo?.get(1) ?: false,
            args?.appcleaninfo?.get(2) ?: false,args?.appcleaninfo?.get(3) ?: true ,
            args?.appcleaninfo?.get(4) ?: true ,args?.appcleaninfo?.get(5) ?: true , args?.appcleaninfo?.get(6) ?: true)


        mTotalStorage = StorageSize().getStorageApiBelow()
        isRunningCleaner = true
        resultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()) {
            doCleaner(quickCleanerModel)
        }

        if (quickCleanerModel.VisiableCached && Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                val intent  =  Intent(ACTION_CLEAR_APP_CACHE)
                resultLauncher.launch(intent)
        } else { doCleaner(quickCleanerModel) }

    }

    private fun doCleaner (quickCleanerModel : QuickCleanerModel?) {
        Thread {
           try {
            val path = Environment.getExternalStorageDirectory()
            val fs : FileScanner? = quickCleanerModel?.let {
                activity?.let { it1 ->
                    FileScanner(path, activity)
                        .setEmptyDir(it.empty)
                        .setDelete(true)
                        .setCorpse(it.Corpse)
                        .setContext(it1)
                        .setThumbailesValue(it.Thumb)
                        .setCacheApps(it.CachedApps)
                        .setUpFilters(generic = it.Junks, aggressive = it.Junks, apk = it.Apk)
                }
            }

                    val sizeTo = fs?.startScan(binding.circularProgressIndicator) ?: 0

                    Thread.sleep(1000)
                    mTotalStorageAfter = StorageSize().getStorageApiBelow()
                    val mSizeToText = mTotalStorageAfter -  mTotalStorage

            runUi.post {
                if (isRunningCleaner) {
                    try {
                        findNavController().navigate(
                            CleanerProcessDirections.actionCleanerProcessToCleanerFinish(
                                mSizeToText + sizeTo
                            )
                        )
                    } catch (re : RuntimeException) {
                        Log.e("TAG", "onViewCreated: ",re )
                    }
                }

            }

            } catch (re : RuntimeException) {
                Log.e(TAG, "doCleaner: ", re )
            }

        }.start()
    }



    override fun onDestroyView() {
        super.onDestroyView()
        isRunningCleaner = false
    }
}
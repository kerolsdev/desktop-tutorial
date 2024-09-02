package com.kerols.phoneboost.Fragments

import android.media.MediaScannerConnection
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.kerols.phoneboost.Activitys.MainActivity.Companion.runUi
import com.kerols.phoneboost.databinding.FragmentMediaProcessBinding
import java.io.File

class MediaProcessFragment : Fragment() {

    lateinit var binding : FragmentMediaProcessBinding
    var boolean: Boolean = true
    var limited : Int = 0
    var wait : Int = 10

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMediaProcessBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args = arguments?.let { MediaProcessFragmentArgs.fromBundle(it) }
        val array = args?.data ?: arrayOf()
        wait = if (array.size == 1) {
            250
        } else {
            val after = 1000  / array.size
            if (10 <= after){
                after
            }else {
                10
            }
        }
        boolean = true
        binding.circularProgressIndicator.max = array.size
        Thread {
        try {
           for ( it in array )
           {
               if (boolean) mDeleteFile(it) else break
           }
        } catch (re : RuntimeException ) {
           Log.e("TAG", "onViewCreated: ", re )
        }
            runUi.post {
                try{
                    findNavController().navigate(MediaProcessFragmentDirections.actionMediaProcessFragmentToCleanerFinish(args?.size ?: 0))
                } catch (re : RuntimeException) {
                    Log.e("TAG", "onViewCreated: ", re )
                }

            }
        }.start()

    }


    private fun mDeleteFile (string: String) {
        if (File(string).exists()){

            File(string).delete()
            Thread.sleep(wait.toLong())
            runUi.post {
                limited += 1
                binding.circularProgressIndicator.progress = limited
            }
            MediaScannerConnection.scanFile(requireActivity(), arrayOf(string), null) { path, uri -> }
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        boolean = false
    }

}
package com.kerols.phoneboost.Fragments

import android.Manifest.permission.*
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.kerols.phoneboost.databinding.FragmentPermissionFileBinding


class FragmentPermissionFile : Fragment() {

    private lateinit var requestPermissionLauncher : ActivityResultLauncher<String>
    private lateinit var binding : FragmentPermissionFileBinding
    private lateinit var  someActivityResultLauncher: ActivityResultLauncher<Intent>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =  FragmentPermissionFileBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        someActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if(checkPermission()) {
                    findNavController().navigate(FragmentPermissionFileDirections.actionFragmentPermissionFileToFragmentHome2())
                }else {
                    // Toast.makeText(requireActivity(),"Try Againg" , Toast.LENGTH_LONG).show()
                }
        }


         requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
                 if (isGranted) {
                     findNavController().navigate(FragmentPermissionFileDirections.actionFragmentPermissionFileToFragmentHome2())
              } else {
                   // Toast.makeText(requireContext(),"Try Again",Toast.LENGTH_LONG).show()
            }
        }

        binding.btnFile.setOnClickListener{
              requestPermission()
        }
    }
    private fun checkPermission(): Boolean {
        return if (SDK_INT >= Build.VERSION_CODES.R) {
            Environment.isExternalStorageManager()
        } else {
            val result =
                ContextCompat.checkSelfPermission(requireActivity(), READ_EXTERNAL_STORAGE)
            val result1 =
                ContextCompat.checkSelfPermission(requireActivity(), WRITE_EXTERNAL_STORAGE)
            result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED
        }
    }

    private fun requestPermission() {
        if (SDK_INT >= Build.VERSION_CODES.R) {
            try {
                val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                intent.data = Uri.parse("package:" + requireContext().packageName)
                someActivityResultLauncher.launch(intent)
            } catch (e: Exception) {
                val intent = Intent()
                intent.action = Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION
                someActivityResultLauncher.launch(intent)
            }
        } else {
            //below android 11
            if (SDK_INT >= Build.VERSION_CODES.Q) {
                requestPermissionLauncher.launch(
                    WRITE_EXTERNAL_STORAGE
                )
            }else {
                requestPermissionLauncher.launch(
                   WRITE_EXTERNAL_STORAGE
                )
            }
        }
    }

}
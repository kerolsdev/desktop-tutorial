package com.kerols.phoneboost.Fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.kerols.phoneboost.databinding.FragmentPackageScanBinding
import java.lang.RuntimeException

class PackageScan : Fragment() {


    private var someActivityResultLauncher: ActivityResultLauncher<Intent>? = null
    private lateinit var binding: FragmentPackageScanBinding
    private var point = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPackageScanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args: PackageScanArgs = arguments?.let { PackageScanArgs.fromBundle(it) }!!
        val arrayList: Array<String> = args.packets
        binding.circularProgressIndicator.max = arrayList.size
        someActivityResultLauncher = registerForActivityResult<Intent, ActivityResult>(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (point < arrayList.size - 1) {

                try {

                point++
                binding.circularProgressIndicator.progress = point
                 val intent2 = Intent(Intent.ACTION_DELETE)
                 intent2.data = Uri.parse("package:" + arrayList[point])
                 intent2.putExtra(Intent.EXTRA_RETURN_RESULT, true);
                 someActivityResultLauncher?.launch(intent2)

                } catch (re : RuntimeException) {
                    Log.e("TAG", "onViewCreated: ",re )
                }

            } else {
                findNavController().navigate(PackageScanDirections.actionPackageScanToPackageFinish2())
            }
        }
        val intent2 = Intent(Intent.ACTION_DELETE)
        intent2.data = Uri.parse("package:" + arrayList[point])
        intent2.putExtra(Intent.EXTRA_RETURN_RESULT, true);
        someActivityResultLauncher?.launch(intent2)
    }

    override fun onDestroyView() {
        super.onDestroyView()

    }


}
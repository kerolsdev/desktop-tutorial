package com.kerolsmm.appmanager.dialogs

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.kerolsmm.appmanager.databinding.DialogBottomBatteryBinding
import com.kerolsmm.appmanager.mvvm.MvvmBottomApp

class BottomDialogBattery : BottomSheetDialogFragment() {


    private lateinit var binding : DialogBottomBatteryBinding
    private val mvvmBottomApp : MvvmBottomApp by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogBottomBatteryBinding.inflate(inflater,container,false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mvvmBottomApp.getValueBattery().observe(viewLifecycleOwner) { model ->

            Glide.with(requireActivity()).load(model.appIcon).into(binding.AppIconBattery)
            binding.PackageNameBattery.text = model.pack
            binding.appNameBattery.text = model.appName

            if (model.backGroundTime != null) {
                binding.BackgroundTimeBattery.text = model.backGroundTime
            }else {
                binding.BackgroundTimeBattery.visibility = View.GONE
                binding.backGroundText.visibility = View.GONE
            }
            if (model.screenTime != null) {
                binding.ScreenTimeBattery.text = model.screenTime
            }


            binding.appSettings.setOnClickListener {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                intent.data = Uri.parse("package:" + model?.pack)
                context?.startActivity(intent)
            }
        }

    }








}
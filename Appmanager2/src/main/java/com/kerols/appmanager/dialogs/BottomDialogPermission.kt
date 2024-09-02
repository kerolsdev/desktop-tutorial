package com.kerols.appmanager.dialogs

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.ChipGroup
import com.kerols.appmanager.R
import com.kerols.appmanager.adapters.AdapterAppPermission
import com.kerols.appmanager.databinding.BottomDialogPermissionBinding
import com.kerols.appmanager.model.AppModel


class BottomDialogPermission(var arrayListUser: ArrayList<AppModel>,
                             var arraylistSystem: ArrayList<AppModel>?,
                              var type : String) : BottomSheetDialogFragment() {


    private lateinit var binding : BottomDialogPermissionBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BottomDialogPermissionBinding.inflate(inflater,container,false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapterAppPermission  = AdapterAppPermission(arrayListUser,requireContext())
        binding.listPermission.adapter = adapterAppPermission


        binding.textType.text = type

      //  Log.e("TAG", "onViewCreated: " + arrayListUser.size )

        if (arraylistSystem == null) {
            binding.SystemChip.visibility = View.GONE
        }

        if (arrayListUser.isEmpty()) {
            binding.progressBarPermissionApp.visibility = View.VISIBLE
        }else {
            binding.progressBarPermissionApp.visibility = View.GONE
        }


        binding.chipGroup.setOnCheckedStateChangeListener(object  : ChipGroup.OnCheckedStateChangeListener{
            override fun onCheckedChanged(group: ChipGroup, checkedIds: MutableList<Int>) {
                if (checkedIds[0] == R.id.UserChip) {
                    if (arrayListUser.isEmpty()) {
                        binding.progressBarPermissionApp.visibility = View.VISIBLE
                    }else {
                        binding.progressBarPermissionApp.visibility = View.GONE
                    }
                    adapterAppPermission.setArrayList(arrayListUser)
                }else {
                    if (arraylistSystem?.isEmpty() == true) {
                        binding.progressBarPermissionApp.visibility = View.VISIBLE
                    }else {
                        binding.progressBarPermissionApp.visibility = View.GONE
                    }
                    arraylistSystem?.let { adapterAppPermission.setArrayList(it) }

                }

            }
        })

    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setOnShowListener { dialogInterface ->
            val bottomSheetDialog = dialogInterface as BottomSheetDialog
            setupFullHeight(bottomSheetDialog)
        }
        return dialog
    }


    private fun setupFullHeight(bottomSheetDialog: BottomSheetDialog) {
        val bottomSheet =
            bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout?
        val behavior: BottomSheetBehavior<*> = BottomSheetBehavior.from(bottomSheet!!)
        val layoutParams = bottomSheet.layoutParams
        val windowHeight = getWindowHeight()
        if (layoutParams != null) {
            layoutParams.height = windowHeight
        }
        bottomSheet.layoutParams = layoutParams
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    private fun getWindowHeight(): Int {
        // Calculate window height for fullscreen use
        val displayMetrics = DisplayMetrics()
        (context as Activity?)!!.windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics.heightPixels
    }


}
package com.kerolsmm.appmanager.dialogs

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.kerolsmm.appmanager.databinding.SpecialPermissionBinding


class BottomDialogPermissionSpecial: BottomSheetDialogFragment() {


    private lateinit var bindSpecial: SpecialPermissionBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bindSpecial = SpecialPermissionBinding.inflate(inflater,container,false)
        return bindSpecial.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        bindSpecial.textType.text = "Special Permission"

        bindSpecial.allFiles.setOnClickListener {
            try {

                val intent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
                } else {
                  null
                }
                intent?.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                if (intent != null) {
                    context?.startActivity(intent)
                }
            } catch (re: RuntimeException) {
                Log.e("TAG", "onViewCreated: ", re)
            }
        }
        bindSpecial.vpnSetting.setOnClickListener {
            val intent: Intent = Intent(Settings.ACTION_VPN_SETTINGS)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }
        bindSpecial.packagesSetting.setOnClickListener {
            val intent = Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }
        bindSpecial.changeSetting.setOnClickListener {
            val intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }
        bindSpecial.notificationAccess.setOnClickListener {
            val intent = Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }
    }








}
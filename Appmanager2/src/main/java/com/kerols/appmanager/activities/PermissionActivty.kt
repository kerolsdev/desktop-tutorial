package com.kerols.appmanager.activities

import android.Manifest
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.annotation.SuppressLint
import android.app.AppOpsManager
import android.app.usage.StorageStatsManager
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.os.Process
import android.os.UserHandle
import android.os.storage.StorageManager
import android.provider.Settings
import android.text.format.Formatter
import android.util.Log
import android.widget.CompoundButton
import android.widget.RadioGroup
import android.widget.RadioGroup.OnCheckedChangeListener
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.kerols.appmanager.R
import com.kerols.appmanager.databinding.ActivityPermissionBinding


class PermissionActivty : AppCompatActivity() {


    private lateinit var requestPermissionLauncher : ActivityResultLauncher<String>
    private lateinit var  someActivityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var  somePermissionResult : ActivityResultLauncher<Intent>
    lateinit var binding : ActivityPermissionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        binding = ActivityPermissionBinding.inflate(layoutInflater)
        setContentView(binding.root)


        if (getGrantStatus()) {

            splashScreen.setKeepOnScreenCondition { true }
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)

            finish()

        }else {

            if (getGrantStatus()) {
                binding.CheckboxDataUsage.isChecked = true
            }
          /*  if (checkPermission()) {
                binding.CheckboxStorage.isChecked = true
            }*/

        }




        binding.CheckboxDataUsage.setOnClickListener {
            requestPermissionBattery()
        }


        binding.CheckboxStorage.setOnClickListener {
            requestPermission()
        }



        somePermissionResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

            // Toast.makeText(requireActivity(),"Try Againg" , Toast.LENGTH_LONG).show()
            binding.CheckboxDataUsage.isChecked = getGrantStatus()

        }



        someActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

            // Toast.makeText(requireActivity(),"Try Againg" , Toast.LENGTH_LONG).show()
            binding.CheckboxStorage.isChecked = checkPermission()
        }


        requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()) { isGranted ->

            // Toast.makeText(requireContext(),"Try Again",Toast.LENGTH_LONG).show()
            binding.CheckboxStorage.isChecked = checkPermission()

        }


        binding.btnClick.setOnClickListener {
            it.isEnabled = false
            if (getGrantStatus()) {
                val intent  = Intent (this,MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                 it.isEnabled = true
                 Toast.makeText(this,"Permission required",Toast.LENGTH_LONG).show()
            }
        }




    }

    private fun getGrantStatus(): Boolean {
        val appOps = getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
        val mode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            appOps.unsafeCheckOpNoThrow(
                AppOpsManager.OPSTR_GET_USAGE_STATS,
                Process.myUid(), packageName
            )
        } else {
            appOps.checkOpNoThrow(
                AppOpsManager.OPSTR_GET_USAGE_STATS,
                Process.myUid(), packageName
            )
        }
        return if (mode == AppOpsManager.MODE_DEFAULT) {
          checkCallingOrSelfPermission(Manifest.permission.PACKAGE_USAGE_STATS) == PackageManager.PERMISSION_GRANTED
      } else {
            mode == AppOpsManager.MODE_ALLOWED
        }
    }


    private fun requestPermissionBattery () {

        try {
            val intent2 = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
            intent2.data = Uri.parse("package:$packageName")
            somePermissionResult.launch(intent2)
        }catch (re : RuntimeException) {
            val intent2 = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
            somePermissionResult.launch(intent2)
        }

    }

    private fun requestPermission() {
        if (SDK_INT >= Build.VERSION_CODES.R) {
            try {
                val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                intent.data = Uri.parse("package:$packageName")
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
    private fun checkPermission(): Boolean {
        return if (SDK_INT >= Build.VERSION_CODES.R) {
            Environment.isExternalStorageManager()
        } else {
            val result =
                ContextCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE)
            val result1 =
                ContextCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE)
            result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED
        }
    }


}
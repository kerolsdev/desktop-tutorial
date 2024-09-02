package com.kerols.appmanager.fragments

import android.Manifest
import android.accessibilityservice.AccessibilityServiceInfo
import android.annotation.SuppressLint
import android.app.AppOpsManager
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Process
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.accessibility.AccessibilityManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.kerols.appmanager.databinding.FragmentPermissionBinding
import com.kerols.appmanager.dialogs.BottomDialogPermission
import com.kerols.appmanager.functions.AppPermissionInfo
import com.kerols.appmanager.functions.MvvmData
import com.kerols.appmanager.model.AppModel
import java.util.Objects


class PermissionFragment() : Fragment(), AppPermissionInfo.QueryApps {


    private   lateinit var  binding : FragmentPermissionBinding

    private var arrayListStorage : ArrayList<AppModel> =  ArrayList()
    private var arrayListSms : ArrayList<AppModel> =  ArrayList()
    private var arrayListPhone : ArrayList<AppModel> =  ArrayList()
    private var arrayListContact : ArrayList<AppModel> =  ArrayList()
    private var arrayListAllFilesAccess : ArrayList<AppModel> =  ArrayList()
    private var arrayListCamera : ArrayList<AppModel> =  ArrayList()
    private var arrayListMic : ArrayList<AppModel> =  ArrayList()
    private var arrayListLocation : ArrayList<AppModel> =  ArrayList()
    private var arrayListDataUsage : ArrayList<AppModel> =  ArrayList()
    private var arrayListAccessibility : ArrayList<AppModel> =  ArrayList()


    private var arrayListStorageSystem : ArrayList<AppModel> =  ArrayList()
    private var arrayListSmsSystem : ArrayList<AppModel> =  ArrayList()
    private var arrayListPhoneSystem : ArrayList<AppModel> =  ArrayList()
    private var arrayListContactSystem : ArrayList<AppModel> =  ArrayList()
    private var arrayListAllFilesAccessSystem : ArrayList<AppModel> =  ArrayList()
    private var arrayListCameraSystem : ArrayList<AppModel> =  ArrayList()
    private var arrayListMicSystem : ArrayList<AppModel> =  ArrayList()
    private var arrayListLocationSystem : ArrayList<AppModel> =  ArrayList()
    private var arrayListDataUsageSystem : ArrayList<AppModel> =  ArrayList()
    private var arrayListAccessibilitySystem : ArrayList<AppModel> =  ArrayList()

    private val viewmodel by activityViewModels<MvvmData>()

    lateinit var packageManager2 : PackageManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPermissionBinding.inflate(inflater,container,false)
        return binding.root
    }


    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.swipeRefresh.isEnabled = false

        packageManager2 = requireActivity().packageManager

    /*    AppPermissionInfo.getInstalledAppsPermissions(requireContext(),this)*/


        viewmodel.getAllApp().observe(viewLifecycleOwner){
          Thread (Runnable {
            for (app in it){
                if (isSystem(app.applicationInfo!!)) {
                    mAppSystemPermission(app.applicationInfo!!,app.appTitle)
                }else {
                    mAppUserPermission(app.applicationInfo!!,app.appTitle)
                }
            }

              isAccessibilityServiceEnabled(requireContext())

/*              Log.e("TAG", "onFinish: arrayListAccessibility " + arrayToString(arrayListAccessibility))
              Log.e("TAG", "onFinish: arrayListMic " + arrayToString(arrayListMic) )*/
              Handler(Looper.getMainLooper()).post(Runnable {


                  binding.layoutPermission.visibility = View.VISIBLE
                  binding.progressBarPermission.visibility = View.GONE
                  binding.swipeRefresh.isRefreshing = false
                  binding.swipeRefresh.isEnabled = true

                  binding.appSms.text = " ${arrayListSms.size + arrayListSmsSystem.size} apps"
                  binding.appAccessibility.text = " ${arrayListAccessibility.size + arrayListAccessibilitySystem.size} apps"
                  binding.appCamare.text = " ${arrayListCamera.size + arrayListCameraSystem.size} apps"
                  binding.appContact.text = " ${arrayListContact.size + arrayListContactSystem.size} apps"
                  binding.appBattery.text = " ${arrayListDataUsage.size + arrayListDataUsageSystem.size} apps"
                  binding.appFiles.text = " ${arrayListAllFilesAccess.size + arrayListAllFilesAccessSystem.size} apps"
                  binding.appStorage.text = " ${arrayListStorage.size + arrayListStorageSystem.size} apps"
                  binding.appLocation.text = " ${arrayListLocation.size + arrayListLocationSystem.size} apps"
                  binding.appMicrophone.text = " ${arrayListMic.size + arrayListMicSystem.size} apps"
                  binding.appPhone.text = " ${arrayListPhone.size + arrayListPhoneSystem.size} apps"
              })
          }).start()
        }


        binding.swipeRefresh.setOnRefreshListener {
             arrayListStorage  =  ArrayList()
             arrayListSms  =  ArrayList()
             arrayListPhone  =  ArrayList()
             arrayListContact  =  ArrayList()
             arrayListAllFilesAccess  =  ArrayList()
             arrayListCamera  =  ArrayList()
             arrayListMic  =  ArrayList()
             arrayListLocation  =  ArrayList()
             arrayListDataUsage  =  ArrayList()
             arrayListAccessibility  =  ArrayList()


             arrayListStorageSystem  =  ArrayList()
             arrayListSmsSystem  =  ArrayList()
             arrayListPhoneSystem  =  ArrayList()
             arrayListContactSystem  =  ArrayList()
             arrayListAllFilesAccessSystem  =  ArrayList()
             arrayListCameraSystem  =  ArrayList()
             arrayListMicSystem  =  ArrayList()
             arrayListLocationSystem  =  ArrayList()
             arrayListDataUsageSystem  =  ArrayList()
             arrayListAccessibilitySystem  =  ArrayList()
            binding.layoutPermission.visibility = View.GONE
            AppPermissionInfo.getInstalledAppsPermissions(requireContext(),this)


        }

        binding.PhoneClick.setOnClickListener {
            if (arrayListPhone.isNotEmpty() || arrayListPhoneSystem.isNotEmpty()) {
                val bottomDialogPermission =
                    BottomDialogPermission(arrayListPhone, arrayListPhoneSystem, "Phone")
                bottomDialogPermission.show(
                    requireActivity().supportFragmentManager,
                    bottomDialogPermission.tag
                )
            } else {
                Toast.makeText(requireActivity(),"No application found",Toast.LENGTH_LONG).show()
            }
        }

        binding.cameraClick.setOnClickListener {
            if (arrayListCamera.isNotEmpty() || arrayListCameraSystem.isNotEmpty()) {
                val bottomDialogPermission =
                    BottomDialogPermission(arrayListCamera, arrayListCameraSystem, "Camera")
                bottomDialogPermission.show(
                    requireActivity().supportFragmentManager,
                    bottomDialogPermission.tag
                )
            }else {
                Toast.makeText(requireActivity(),"No application found",Toast.LENGTH_LONG).show()
            }
        }
        binding.MicrophoneClick.setOnClickListener {
            if (arrayListMic.isNotEmpty() || arrayListMicSystem.isNotEmpty()) {
                val bottomDialogPermission =
                    BottomDialogPermission(arrayListMic, arrayListMicSystem, "MicroPhone")
                bottomDialogPermission.show(
                    requireActivity().supportFragmentManager,
                    bottomDialogPermission.tag
                )
            } else {
                Toast.makeText(requireActivity(),"No application found",Toast.LENGTH_LONG).show()
            }
        }
        binding.StorageClick.setOnClickListener {
            if (arrayListStorage.isNotEmpty() || arrayListStorageSystem.isNotEmpty()) {
                val bottomDialogPermission =
                    BottomDialogPermission(arrayListStorage, arrayListStorageSystem, "Storage")
                bottomDialogPermission.show(
                    requireActivity().supportFragmentManager,
                    bottomDialogPermission.tag
                )
            }else {
                Toast.makeText(requireActivity(),"No application found",Toast.LENGTH_LONG).show()
            }
        }
        binding.ContactClick.setOnClickListener {
            if (arrayListContact.isNotEmpty() || arrayListContactSystem.isNotEmpty()) {
                val bottomDialogPermission =
                    BottomDialogPermission(arrayListContact, arrayListContactSystem, "Contact")
                bottomDialogPermission.show(
                    requireActivity().supportFragmentManager,
                    bottomDialogPermission.tag
                )
            } else {
                Toast.makeText(requireActivity(),"No application found",Toast.LENGTH_LONG).show()
            }
        }
        binding.SmsClick.setOnClickListener {
            if (arrayListSms.isNotEmpty() || arrayListSmsSystem.isNotEmpty()) {
                val bottomDialogPermission =
                    BottomDialogPermission(arrayListSms, arrayListSmsSystem, "Sms")
                bottomDialogPermission.show(
                    requireActivity().supportFragmentManager,
                    bottomDialogPermission.tag
                )
            }else {
                Toast.makeText(requireActivity(),"No application found",Toast.LENGTH_LONG).show()
            }
        }
        binding.DataClick.setOnClickListener {
            if (arrayListDataUsage.isNotEmpty() || arrayListDataUsageSystem.isNotEmpty()) {
                val bottomDialogPermission = BottomDialogPermission(
                    arrayListDataUsage,
                    arrayListDataUsageSystem,
                    "Data Usage"
                )
                bottomDialogPermission.show(
                    requireActivity().supportFragmentManager,
                    bottomDialogPermission.tag
                )
            }else {
                Toast.makeText(requireActivity(),"No application found",Toast.LENGTH_LONG).show()
            }
        }
        binding.AccessibilityClick.setOnClickListener {
            if (arrayListAccessibility.isNotEmpty() || arrayListAccessibilitySystem.isNotEmpty()) {
                val bottomDialogPermission = BottomDialogPermission(
                    arrayListAccessibility,
                    arrayListAccessibilitySystem,
                    "Accessibility"
                )
                bottomDialogPermission.show(
                    requireActivity().supportFragmentManager,
                    bottomDialogPermission.tag
                )
            }else {
                Toast.makeText(requireActivity(),"No application found",Toast.LENGTH_LONG).show()
            }
        }
        binding.LocationClick.setOnClickListener {
            if (arrayListLocation.isNotEmpty() || arrayListLocationSystem.isNotEmpty()) {
                val bottomDialogPermission =
                    BottomDialogPermission(arrayListLocation, arrayListLocationSystem, "Location")
                bottomDialogPermission.show(
                    requireActivity().supportFragmentManager,
                    bottomDialogPermission.tag
                )
            } else {
                Toast.makeText(requireActivity(),"No application found",Toast.LENGTH_LONG).show()
            }
        }
        binding.FilesClick.setOnClickListener {
            if (arrayListAllFilesAccess.isNotEmpty() || arrayListAllFilesAccessSystem.isNotEmpty()) {
                val bottomDialogPermission = BottomDialogPermission(
                    arrayListAllFilesAccess,
                    arrayListAllFilesAccessSystem,
                    "All Files Access"
                )
                bottomDialogPermission.show(
                    requireActivity().supportFragmentManager,
                    bottomDialogPermission.tag
                )
            } else {
                Toast.makeText(requireActivity(),"No application found",Toast.LENGTH_LONG).show()
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            binding.FilesClick.visibility = View.VISIBLE
        }else {
            binding.FilesClick.visibility = View.GONE
        }


    }

    @SuppressLint("SuspiciousIndentation")
    override fun onChange(app: ApplicationInfo) {

        val appName = "App Name"
        val it = app.packageName


        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            if (isPermissionGranted(it, "android.permission.WRITE_EXTERNAL_STORAGE") ||
                isPermissionGranted(it, "android.permission.READ_EXTERNAL_STORAGE") ||
                isPermissionGranted(it, "android.permission.ACCESS_MEDIA_LOCATION")
            ) {
                arrayListStorage.add(AppModel(0, appName.toString(), app.packageName, null, "null",app))

            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

            if (isPermissionGranted(it, "android.permission.READ_MEDIA_VIDEO") ||
                isPermissionGranted(it, "android.permission.READ_MEDIA_AUDIO") ||
                isPermissionGranted(it, "android.permission.READ_MEDIA_IMAGES")
            ) {
                arrayListStorage.add(AppModel(0, appName.toString(), app.packageName, null, "null",app))

            }
        }
        if (isPermissionGranted(it ,"android.permission.CAMERA")) {
                arrayListCamera.add(AppModel(0, appName.toString(), app.packageName, null, "null",app))

            }
        if (isPermissionGranted(it ,"android.permission.RECORD_AUDIO") ) {

            arrayListMic.add(AppModel(
                0, appName, app.packageName,
                null, "null",app
            ))

            }
        if ( isPermissionGranted(it ,"android.permission.READ_CONTACTS") ||
                isPermissionGranted(it ,"android.permission.WRITE_CONTACTS")
            ) {
                arrayListContact.add(AppModel(0, appName.toString(), app.packageName, null, "null",app))

            }
        if (isPermissionGranted(it ,"android.permission.ACCESS_FINE_LOCATION") ||
                isPermissionGranted(it ,"android.permission.ACCESS_COARSE_LOCATION")
            ) {
                arrayListLocation.add(AppModel(0, appName.toString(), app.packageName, null, "null",app))

            }
        if (isPermissionGranted(it ,"android.permission.CALL_PHONE") ||
                isPermissionGranted(it ,"android.permission.READ_PHONE_NUMBERS") ||
                isPermissionGranted(it ,"android.permission.READ_PHONE_STATE")
            ) {
                arrayListPhone.add(AppModel(0, appName.toString(), app.packageName, null, "null",app))


            }
        if (isPermissionGranted(it ,"android.permission.READ_SMS") ||
                       isPermissionGranted(it ,"android.permission.SEND_SMS"))
            {
                arrayListSms.add(AppModel(0, appName.toString(), app.packageName, null, "null",app))
            }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (checkPermission(app)) {
                arrayListAllFilesAccess.add(
                    AppModel(
                        1,
                        appName.toString(),
                        app.packageName,
                        null,
                        "null",app
                    )
                )

            }
        }
        if (getGrantStatus(app)) {
                arrayListDataUsage.add(AppModel(
                    2,
                    appName.toString(), app.packageName, null, "null",app
                ))

            }




    }

    override fun onChangeSystem(app : ApplicationInfo) {
        val it = app.packageName
       // val appName = packageManager2?.getApplicationLabel(app).toString()
        val appName = "App Name"


        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            if (isPermissionGranted(it, "android.permission.WRITE_EXTERNAL_STORAGE") ||
                isPermissionGranted(it, "android.permission.READ_EXTERNAL_STORAGE") ||
                isPermissionGranted(it, "android.permission.ACCESS_MEDIA_LOCATION")
            ) {
                arrayListStorageSystem.add(AppModel(0, appName.toString(), app.packageName, null, "null",app))

            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

            if (isPermissionGranted(it, "android.permission.READ_MEDIA_VIDEO") ||
                isPermissionGranted(it, "android.permission.READ_MEDIA_AUDIO") ||
                isPermissionGranted(it, "android.permission.READ_MEDIA_IMAGES")
            ) {
                arrayListStorageSystem.add(AppModel(0, appName.toString(), app.packageName, null, "null",app))

            }
        }


        /* for (it in permissions) {*/
/*        if ( isPermissionGranted(it ,"android.permission.WRITE_EXTERNAL_STORAGE") ||
            isPermissionGranted(it ,"android.permission.READ_EXTERNAL_STORAGE")||
            isPermissionGranted(it ,"android.permission.READ_MEDIA_VIDEO")||
            isPermissionGranted(it ,"android.permission.READ_MEDIA_AUDIO")||
            isPermissionGranted(it ,"android.permission.READ_MEDIA_IMAGES")||
            isPermissionGranted(it ,"android.permission.ACCESS_MEDIA_LOCATION")
        ) {
            arrayListStorageSystem.add(AppModel(0, appName.toString(), app.packageName, null, "null"))

        }*/
        if (isPermissionGranted(it ,"android.permission.CAMERA")) {
            arrayListCameraSystem.add(AppModel(0, appName.toString(), app.packageName, null, "null",app))

        }
        if (isPermissionGranted(it ,"android.permission.RECORD_AUDIO") ) {

            arrayListMicSystem.add(AppModel(
                0, appName, app.packageName,
                null, "null",app
            ))

        }
        if ( isPermissionGranted(it ,"android.permission.READ_CONTACTS") ||
            isPermissionGranted(it ,"android.permission.WRITE_CONTACTS")
        ) {
            arrayListContactSystem.add(AppModel(0, appName.toString(), app.packageName, null, "null",app))

        }
        if (isPermissionGranted(it ,"android.permission.ACCESS_FINE_LOCATION") ||
            isPermissionGranted(it ,"android.permission.ACCESS_COARSE_LOCATION")
        ) {
            arrayListLocationSystem.add(AppModel(0, appName.toString(), app.packageName, null, "null",app))

        }
        if (isPermissionGranted(it ,"android.permission.CALL_PHONE") ||
            isPermissionGranted(it ,"android.permission.READ_PHONE_NUMBERS") ||
            isPermissionGranted(it ,"android.permission.READ_PHONE_STATE")
        ) {
            arrayListPhoneSystem.add(AppModel(0, appName.toString(), app.packageName, null, "null",app))


        }
        if (isPermissionGranted(it ,"android.permission.READ_SMS") ||
            isPermissionGranted(it ,"android.permission.SEND_SMS"))
        {
            arrayListSmsSystem.add(AppModel(0, appName.toString(), app.packageName, null, "null",app))
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (checkPermission(app)) {
                arrayListAllFilesAccessSystem.add(
                    AppModel(
                        1,
                        appName.toString(),
                        app.packageName,
                        null,
                        "null",app
                    )
                )

            }
        }
        if (getGrantStatus(app)) {
            arrayListDataUsageSystem.add(AppModel(
                2,
                appName.toString(), app.packageName, null, "null",app
            ))

        }

    }

    @SuppressLint("SetTextI18n")
    override fun onFinish() {

        isAccessibilityServiceEnabled(requireContext())

  /*      Log.e("TAG", "onFinish: arrayListAccessibility " + arrayToString(arrayListAccessibility))
        Log.e("TAG", "onFinish: arrayListMic " + arrayToString(arrayListMic) )*/
        Handler(Looper.getMainLooper()).post(Runnable {


         binding.layoutPermission.visibility = View.VISIBLE
         binding.progressBarPermission.visibility = View.GONE
         binding.swipeRefresh.isRefreshing = false
         binding.swipeRefresh.isEnabled = true

        binding.appSms.text = " ${arrayListSms.size + arrayListSmsSystem.size} apps"
        binding.appAccessibility.text = " ${arrayListAccessibility.size + arrayListAccessibilitySystem.size} apps"
        binding.appCamare.text = " ${arrayListCamera.size + arrayListCameraSystem.size} apps"
        binding.appContact.text = " ${arrayListContact.size + arrayListContactSystem.size} apps"
        binding.appBattery.text = " ${arrayListDataUsage.size + arrayListDataUsageSystem.size} apps"
        binding.appFiles.text = " ${arrayListAllFilesAccess.size + arrayListAllFilesAccessSystem.size} apps"
        binding.appStorage.text = " ${arrayListStorage.size + arrayListStorageSystem.size} apps"
        binding.appLocation.text = " ${arrayListLocation.size + arrayListLocationSystem.size} apps"
        binding.appMicrophone.text = " ${arrayListMic.size + arrayListMicSystem.size} apps"
        binding.appPhone.text = " ${arrayListPhone.size + arrayListPhoneSystem.size} apps"
        })
    }

    @SuppressLint("NewApi")
    private fun checkPermission(app: ApplicationInfo): Boolean {
        val appOps = context?.getSystemService(AppOpsManager::class.java)
        val mode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            appOps?.unsafeCheckOpNoThrow(
                "android:manage_external_storage",
                app.uid, app.packageName
            )
        } else {
            try {
                appOps?.checkOpNoThrow(
                    "android:manage_external_storage",
                    app.uid, app.packageName
                )
            }catch (re: RuntimeException) {
                454554
            }
        }
        return when (mode) {
            AppOpsManager.MODE_DEFAULT -> (PackageManager.PERMISSION_GRANTED
                    == context?.checkPermission(
                Manifest.permission.MANAGE_EXTERNAL_STORAGE, Process.myPid(), app.uid
            ))

            AppOpsManager.MODE_ALLOWED -> true
            AppOpsManager.MODE_ERRORED, AppOpsManager.MODE_IGNORED -> false
            else -> false
        }
    }

    private fun getGrantStatus( app: ApplicationInfo): Boolean {
        val appOps = requireActivity().getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
        val mode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            appOps.unsafeCheckOpNoThrow(
                AppOpsManager.OPSTR_GET_USAGE_STATS,
                app.uid, app.packageName
            )
        } else {
            try {

            appOps.checkOpNoThrow(
                AppOpsManager.OPSTR_GET_USAGE_STATS,
                app.uid, app.packageName
            )

            }catch (re :RuntimeException) {
              456646
            }
        }
/*        return if (mode == AppOpsManager.MODE_DEFAULT) {
            requireActivity().checkCallingOrSelfPermission(Manifest.permission.PACKAGE_USAGE_STATS) == PackageManager.PERMISSION_GRANTED
        } else {*/
         return   mode == AppOpsManager.MODE_ALLOWED
     //   }
    }

   fun isAccessibilityServiceEnabled(
        context: Context,
    ) {
        val am = context.getSystemService(Context.ACCESSIBILITY_SERVICE) as AccessibilityManager
        val enabledServices =
            am.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_ALL_MASK)
           for (enabledService in enabledServices) {
          //  val appName = packageManager2?.getApplicationLabel(enabledService.resolveInfo.activityInfo.applicationInfo).toString()
            if (enabledService != null){
            val appName = packageManager2?.getApplicationLabel(enabledService.resolveInfo.serviceInfo.applicationInfo).toString()
            val packageInfo = enabledService.resolveInfo.serviceInfo.packageName
            if (isSystem(enabledService.resolveInfo.serviceInfo.applicationInfo)){
                 arrayListAccessibilitySystem.add(AppModel(3, appName.toString() ,packageInfo,null,"null",enabledService.resolveInfo.serviceInfo.applicationInfo))
            }else{
                 arrayListAccessibility.add(AppModel(3, appName.toString() ,packageInfo,null,"null",enabledService.resolveInfo.serviceInfo.applicationInfo))

            }
            }
           }

    }

   /* fun isAccessibilityServiceEnabled(
        context: Context,
        app: ApplicationInfo
    ) : Boolean {
        val appOps = context?.getSystemService(AppOpsManager::class.java)
        val mode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            appOps?.unsafeCheckOpNoThrow(
                AppOpsManager.OPSTR_READ_CELL_BROADCASTS,
                app.uid, app.packageName
            )
        } else {
            appOps?.checkOpNoThrow(
                "android:manage_external_storage",
                app.uid, app.packageName
            )
        }
        return when (mode) {
            AppOpsManager.MODE_DEFAULT -> (PackageManager.PERMISSION_GRANTED
                    == context?.checkPermission(
                Manifest.permission.BIND_ACCESSIBILITY_SERVICE, Process.myPid(), app.uid
            ))

            AppOpsManager.MODE_ALLOWED -> true
            AppOpsManager.MODE_ERRORED, AppOpsManager.MODE_IGNORED -> false
            else -> false
        }

    }*/


    private fun isPermissionGranted(packageName: String, permission: String): Boolean {

        val packageInfo: PackageInfo =
            packageManager2.getPackageInfo(packageName, PackageManager.GET_PERMISSIONS)
        val requestedPermissions = packageInfo.requestedPermissions

        if (requestedPermissions != null) {
            for (i in requestedPermissions.indices) {
                if (requestedPermissions[i] == permission ) {
                   // Log.e("TAG",   packageName + " ******  "+ "isPermissionGranted: ${requestedPermissions[i]} ", )
                    return packageManager2.checkPermission(permission,packageName) == PackageManager.PERMISSION_GRANTED
                }
            }
        }
        return false

   //  return packageManager2.checkPermission(permission,packageName) == PackageManager.PERMISSION_GRANTED

    }
    private fun arrayToString(array: ArrayList<AppModel>): String {
        val result = StringBuilder()
        for (item in array) {
            result.append(item).append(", ")
        }
        return result.toString()
    }

    fun isSystem (applicationInfo: ApplicationInfo) : Boolean {
        return applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM != 0
    }


    fun mAppSystemPermission (app: ApplicationInfo , appName : String) {
        val it = app.packageName
       // val appName = packageManager2?.getApplicationLabel(app).toString()

        /* for (it in permissions) {*/
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            if (isPermissionGranted(it, "android.permission.WRITE_EXTERNAL_STORAGE") ||
                isPermissionGranted(it, "android.permission.READ_EXTERNAL_STORAGE") ||
                isPermissionGranted(it, "android.permission.ACCESS_MEDIA_LOCATION")
            ) {
                arrayListStorageSystem.add(AppModel(0, appName.toString(), app.packageName, null, "null",app))

            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

            if (isPermissionGranted(it, "android.permission.READ_MEDIA_VIDEO") ||
                isPermissionGranted(it, "android.permission.READ_MEDIA_AUDIO") ||
                isPermissionGranted(it, "android.permission.READ_MEDIA_IMAGES")
            ) {
                arrayListStorageSystem.add(AppModel(0, appName.toString(), app.packageName, null, "null",app))

            }
        }

        if (isPermissionGranted(it ,"android.permission.CAMERA")) {
            arrayListCameraSystem.add(AppModel(0, appName.toString(), app.packageName, null, "null",app))

        }
        if (isPermissionGranted(it ,"android.permission.RECORD_AUDIO") ) {

            arrayListMicSystem.add(AppModel(
                0, appName, app.packageName,
                null, "null",app
            ))

        }
        if ( isPermissionGranted(it ,"android.permission.READ_CONTACTS") ||
            isPermissionGranted(it ,"android.permission.WRITE_CONTACTS")
        ) {
            arrayListContactSystem.add(AppModel(0, appName.toString(), app.packageName, null, "null",app))

        }
        if (isPermissionGranted(it ,"android.permission.ACCESS_FINE_LOCATION") ||
            isPermissionGranted(it ,"android.permission.ACCESS_COARSE_LOCATION")
        ) {
            arrayListLocationSystem.add(AppModel(0, appName.toString(), app.packageName, null, "null",app))

        }
        if (isPermissionGranted(it ,"android.permission.CALL_PHONE") ||
            isPermissionGranted(it ,"android.permission.READ_PHONE_NUMBERS") ||
            isPermissionGranted(it ,"android.permission.READ_PHONE_STATE")
        ) {
            arrayListPhoneSystem.add(AppModel(0, appName.toString(), app.packageName, null, "null",app))


        }
        if (isPermissionGranted(it ,"android.permission.READ_SMS") ||
            isPermissionGranted(it ,"android.permission.SEND_SMS"))
        {
            arrayListSmsSystem.add(AppModel(0, appName.toString(), app.packageName, null, "null",app))
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (checkPermission(app)) {
                arrayListAllFilesAccessSystem.add(
                    AppModel(
                        1,
                        appName.toString(),
                        app.packageName,
                        null,
                        "null",app
                    )
                )

            }
        }
        if (getGrantStatus(app)) {
            arrayListDataUsageSystem.add(AppModel(
                2,
                appName.toString(), app.packageName, null, "null",app
            ))

        }

    }

    fun mAppUserPermission (app: ApplicationInfo , appName: String) {
       // val appName = packageManager2.getApplicationLabel(app).toString()
        val it = app.packageName

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            if (isPermissionGranted(it, "android.permission.WRITE_EXTERNAL_STORAGE") ||
                isPermissionGranted(it, "android.permission.READ_EXTERNAL_STORAGE") ||
                isPermissionGranted(it, "android.permission.ACCESS_MEDIA_LOCATION")
            ) {
                arrayListStorage.add(AppModel(0, appName.toString(), app.packageName, null, "null",app))

            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

            if (isPermissionGranted(it, "android.permission.READ_MEDIA_VIDEO") ||
                isPermissionGranted(it, "android.permission.READ_MEDIA_AUDIO") ||
                isPermissionGranted(it, "android.permission.READ_MEDIA_IMAGES")
            ) {
                arrayListStorage.add(AppModel(0, appName.toString(), app.packageName, null, "null",app))

            }
        }

        if (isPermissionGranted(it ,"android.permission.CAMERA")) {
            arrayListCamera.add(AppModel(0, appName.toString(), app.packageName, null, "null",app))

        }
        if (isPermissionGranted(it ,"android.permission.RECORD_AUDIO") ) {

            arrayListMic.add(AppModel(
                0, appName, app.packageName,
                null, "null",app
            ))

        }
        if ( isPermissionGranted(it ,"android.permission.READ_CONTACTS") ||
            isPermissionGranted(it ,"android.permission.WRITE_CONTACTS")
        ) {
            arrayListContact.add(AppModel(0, appName.toString(), app.packageName, null, "null",app))

        }
        if (isPermissionGranted(it ,"android.permission.ACCESS_FINE_LOCATION") ||
            isPermissionGranted(it ,"android.permission.ACCESS_COARSE_LOCATION")
        ) {
            arrayListLocation.add(AppModel(0, appName.toString(), app.packageName, null, "null",app))

        }
        if (isPermissionGranted(it ,"android.permission.CALL_PHONE") ||
            isPermissionGranted(it ,"android.permission.READ_PHONE_NUMBERS") ||
            isPermissionGranted(it ,"android.permission.READ_PHONE_STATE")
        ) {
            arrayListPhone.add(AppModel(0, appName.toString(), app.packageName, null, "null",app))


        }
        if (isPermissionGranted(it ,"android.permission.READ_SMS") ||
            isPermissionGranted(it ,"android.permission.SEND_SMS"))
        {
            arrayListSms.add(AppModel(0, appName.toString(), app.packageName, null, "null",app))
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (checkPermission(app)) {
                arrayListAllFilesAccess.add(
                    AppModel(
                        1,
                        appName.toString(),
                        app.packageName,
                        null,
                        "null",app
                    )
                )

            }
        }
        if (getGrantStatus(app)) {
            arrayListDataUsage.add(AppModel(
                2,
                appName.toString(), app.packageName, null, "null",app
            ))

        }

    }

}
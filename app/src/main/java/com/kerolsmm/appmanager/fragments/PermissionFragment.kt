package com.kerolsmm.appmanager.fragments

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
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.accessibility.AccessibilityManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.kerolsmm.appmanager.databinding.FragmentPermissionBinding
import com.kerolsmm.appmanager.dialogs.BottomDialogPermission
import com.kerolsmm.appmanager.dialogs.BottomDialogPermissionSpecial
import com.kerolsmm.appmanager.functions.AppPermissionInfo
import com.kerolsmm.appmanager.functions.MvvmData
import com.kerolsmm.appmanager.model.AppModel
import com.kerolsmm.appmanager.model.AppModelPermission
import com.kerolsmm.appmanager.mvvm.MvvmBottomApp


class PermissionFragment() : Fragment(), AppPermissionInfo.QueryApps {

    private var mLastClickTime: Long = 0

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

    private var arrayListNearbly : ArrayList<AppModel> =  ArrayList()
    private var arrayListBlueTooth : ArrayList<AppModel> =  ArrayList()
    private var arrayListSetting : ArrayList<AppModel> =  ArrayList()
    private var arrayListPackages : ArrayList<AppModel> =  ArrayList()


    private val mvvmBottomApp : MvvmBottomApp by activityViewModels()

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


    private var arrayListNearblySystem : ArrayList<AppModel> =  ArrayList()
/*
    private var arrayListBlueToothSystem : ArrayList<AppModel> =  ArrayList()
*/
    private var arrayListSettingSystem : ArrayList<AppModel> =  ArrayList()
    private var arrayListPackagesSystem : ArrayList<AppModel> =  ArrayList()

    private val viewmodel by activityViewModels<MvvmData>()

    lateinit var packageManager2 : PackageManager

    private var isFinish : Boolean = true;

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


    @SuppressLint("SetTextI18n", "SuspiciousIndentation")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (context != null && activity != null) {

            binding.swipeRefresh.isEnabled = false

                Thread(Runnable {

                    packageManager2 = requireActivity().packageManager
                    val pm: PackageManager? = context?.packageManager
                    val applicationInfos: MutableList<ApplicationInfo>? =
                        pm?.getInstalledApplications(PackageManager.GET_META_DATA)

                    deleteArray()

                    if (applicationInfos != null) {

                   for (applicationInfo in applicationInfos) {
                       if (isSystem(applicationInfo)) {
                           mAppSystemPermission(applicationInfo, applicationInfo.packageName)
                       } else {
                           mAppUserPermission(applicationInfo, applicationInfo.packageName)
                       }
                }

                 /*   for (app in it) {
                        if (isSystem(app.applicationInfo!!)) {
                            mAppSystemPermission(app.applicationInfo!!, app.appTitle)
                        } else {
                            mAppUserPermission(app.applicationInfo!!, app.appTitle)
                        }
                    }*/

                    if (context != null) { isAccessibilityServiceEnabled(requireContext()) }

                    Handler(Looper.getMainLooper()).post(Runnable {

                        binding.layoutPermission.visibility = View.VISIBLE
                        binding.progressBarPermission.visibility = View.GONE
                        binding.swipeRefresh.isRefreshing = false
                        binding.swipeRefresh.isEnabled = true

                        binding.appSms.text = " ${arrayListSms.size + arrayListSmsSystem.size} apps"
                        binding.appAccessibility.text =
                            " ${arrayListAccessibility.size + arrayListAccessibilitySystem.size} apps"
                        binding.appCamare.text =
                            " ${arrayListCamera.size + arrayListCameraSystem.size} apps"
                        binding.appContact.text =
                            " ${arrayListContact.size + arrayListContactSystem.size} apps"
                        binding.appBattery.text =
                            " ${arrayListDataUsage.size + arrayListDataUsageSystem.size} apps"
                     /*   binding.appFiles.text =
                            " ${arrayListAllFilesAccess.size + arrayListAllFilesAccessSystem.size} apps"*/
                        binding.appStorage.text =
                            " ${arrayListStorage.size + arrayListStorageSystem.size} apps"
                        binding.appLocation.text =
                            " ${arrayListLocation.size + arrayListLocationSystem.size} apps"
                        binding.appMicrophone.text =
                            " ${arrayListMic.size + arrayListMicSystem.size} apps"
                        binding.appPhone.text =
                            " ${arrayListPhone.size + arrayListPhoneSystem.size} apps"
                        binding.appNearbly.text =
                            " ${arrayListNearbly.size + arrayListNearblySystem.size} apps"
                      /*  binding.appPackages.text =
                            " ${arrayListPackages.size + arrayListPackagesSystem.size} apps"*/
                    /*    binding.appSetting.text =
                            " ${arrayListSetting.size + arrayListSettingSystem.size} apps"*/

                    })
                    }
                }).start()


            binding.swipeRefresh.setOnRefreshListener {
                deleteArray()
                binding.layoutPermission.visibility = View.GONE
                AppPermissionInfo.getInstalledAppsPermissions(requireContext(), this)


            }

            binding.PhoneClick.setOnClickListener {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
                    return@setOnClickListener;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                if (arrayListPhone.isNotEmpty() || arrayListPhoneSystem.isNotEmpty()) {
                    val bottomDialogPermission =
                        BottomDialogPermission("Phone")
                    bottomDialogPermission.show(
                        requireActivity().supportFragmentManager,
                        bottomDialogPermission.tag
                    )
                    mvvmBottomApp.setValuePermission(
                        AppModelPermission(
                            arrayListPhone,
                            arrayListPhoneSystem
                        )
                    )
                } else {
                    Toast.makeText(requireActivity(), "No application found", Toast.LENGTH_LONG)
                        .show()
                }
            }

            binding.cameraClick.setOnClickListener {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
                    return@setOnClickListener;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                if (arrayListCamera.isNotEmpty() || arrayListCameraSystem.isNotEmpty()) {
                    val bottomDialogPermission =
                        BottomDialogPermission("Camera")
                    bottomDialogPermission.show(
                        requireActivity().supportFragmentManager,
                        bottomDialogPermission.tag
                    )
                    mvvmBottomApp.setValuePermission(
                        AppModelPermission(
                            arrayListCamera,
                            arrayListCameraSystem
                        )
                    )
                } else {
                    Toast.makeText(requireActivity(), "No application found", Toast.LENGTH_LONG)
                        .show()
                }
            }
            binding.MicrophoneClick.setOnClickListener {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
                    return@setOnClickListener;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                if (arrayListMic.isNotEmpty() || arrayListMicSystem.isNotEmpty()) {
                    val bottomDialogPermission =
                        BottomDialogPermission("MicroPhone")
                    bottomDialogPermission.show(
                        requireActivity().supportFragmentManager,
                        bottomDialogPermission.tag
                    )
                    mvvmBottomApp.setValuePermission(
                        AppModelPermission(
                            arrayListMic,
                            arrayListMicSystem
                        )
                    )
                } else {
                    Toast.makeText(requireActivity(), "No application found", Toast.LENGTH_LONG)
                        .show()
                }
            }
            binding.StorageClick.setOnClickListener {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
                    return@setOnClickListener;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                if (arrayListStorage.isNotEmpty() || arrayListStorageSystem.isNotEmpty()) {
                    val bottomDialogPermission =
                        BottomDialogPermission("Storage")
                    bottomDialogPermission.show(
                        requireActivity().supportFragmentManager,
                        bottomDialogPermission.tag
                    )
                    mvvmBottomApp.setValuePermission(
                        AppModelPermission(
                            arrayListStorage,
                            arrayListStorageSystem
                        )
                    )
                } else {
                    Toast.makeText(requireActivity(), "No application found", Toast.LENGTH_LONG)
                        .show()
                }
            }
            binding.ContactClick.setOnClickListener {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
                    return@setOnClickListener;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                if (arrayListContact.isNotEmpty() || arrayListContactSystem.isNotEmpty()) {
                    val bottomDialogPermission =
                        BottomDialogPermission("Contact")
                    bottomDialogPermission.show(
                        requireActivity().supportFragmentManager,
                        bottomDialogPermission.tag
                    )
                    mvvmBottomApp.setValuePermission(
                        AppModelPermission(
                            arrayListContact,
                            arrayListContactSystem
                        )
                    )
                } else {
                    Toast.makeText(requireActivity(), "No application found", Toast.LENGTH_LONG)
                        .show()
                }
            }
            binding.SmsClick.setOnClickListener {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
                    return@setOnClickListener;
                }
                mLastClickTime = SystemClock.elapsedRealtime();

                if (arrayListSms.isNotEmpty() || arrayListSmsSystem.isNotEmpty()) {
                    val bottomDialogPermission =
                        BottomDialogPermission("Sms")
                    bottomDialogPermission.show(
                        requireActivity().supportFragmentManager,
                        bottomDialogPermission.tag
                    )
                    mvvmBottomApp.setValuePermission(
                        AppModelPermission(
                            arrayListSms,
                            arrayListSmsSystem
                        )
                    )
                } else {
                    Toast.makeText(requireActivity(), "No application found", Toast.LENGTH_LONG)
                        .show()
                }
            }
            binding.DataClick.setOnClickListener {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
                    return@setOnClickListener;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                if (arrayListDataUsage.isNotEmpty() || arrayListDataUsageSystem.isNotEmpty()) {
                    val bottomDialogPermission = BottomDialogPermission(
                        "Data Usage"
                    )
                    bottomDialogPermission.show(
                        requireActivity().supportFragmentManager,
                        bottomDialogPermission.tag
                    )
                    mvvmBottomApp.setValuePermission(
                        AppModelPermission(
                            arrayListDataUsage,
                            arrayListDataUsageSystem
                        )
                    )
                } else {
                    Toast.makeText(requireActivity(), "No application found", Toast.LENGTH_LONG)
                        .show()
                }
            }
            binding.AccessibilityClick.setOnClickListener {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
                    return@setOnClickListener;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                if (arrayListAccessibility.isNotEmpty() || arrayListAccessibilitySystem.isNotEmpty()) {
                    val bottomDialogPermission = BottomDialogPermission(
                        "Accessibility"
                    )
                    bottomDialogPermission.show(
                        requireActivity().supportFragmentManager,
                        bottomDialogPermission.tag
                    )
                    mvvmBottomApp.setValuePermission(
                        AppModelPermission(
                            arrayListAccessibility,
                            arrayListAccessibilitySystem
                        )
                    )
                } else {
                    Toast.makeText(requireActivity(), "No application found", Toast.LENGTH_LONG)
                        .show()
                }
            }
            binding.LocationClick.setOnClickListener {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
                    return@setOnClickListener;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                if (arrayListLocation.isNotEmpty() || arrayListLocationSystem.isNotEmpty()) {
                    val bottomDialogPermission =
                        BottomDialogPermission("Location")
                    bottomDialogPermission.show(
                        requireActivity().supportFragmentManager,
                        bottomDialogPermission.tag
                    )
                    mvvmBottomApp.setValuePermission(
                        AppModelPermission(
                            arrayListLocation,
                            arrayListLocationSystem
                        )
                    )
                } else {
                    Toast.makeText(requireActivity(), "No application found", Toast.LENGTH_LONG)
                        .show()
                }
            }
            binding.SpcialClick.setOnClickListener {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
                    return@setOnClickListener;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
               val bottomDialogPermissionSpecial = BottomDialogPermissionSpecial()
                bottomDialogPermissionSpecial.show(requireActivity().supportFragmentManager,bottomDialogPermissionSpecial.tag)
            }
    /*        binding.FilesClick.setOnClickListener {
                if (arrayListAllFilesAccess.isNotEmpty() || arrayListAllFilesAccessSystem.isNotEmpty()) {
                    val bottomDialogPermission = BottomDialogPermission(
                        "All Files Access"
                    )
                    bottomDialogPermission.show(
                        requireActivity().supportFragmentManager,
                        bottomDialogPermission.tag
                    )

                    mvvmBottomApp.setValuePermission(
                        AppModelPermission(
                            arrayListAllFilesAccess,
                            arrayListAllFilesAccessSystem
                        )
                    )
                } else {
                    Toast.makeText(requireActivity(), "No application found", Toast.LENGTH_LONG)
                        .show()
                }
            }
            binding.SettingClick.setOnClickListener {
                if (arrayListSetting.isNotEmpty() || arrayListSettingSystem.isNotEmpty()) {
                    val bottomDialogPermission = BottomDialogPermission(
                        "All Files Access"
                    )
                    bottomDialogPermission.show(
                        requireActivity().supportFragmentManager,
                        bottomDialogPermission.tag
                    )

                    mvvmBottomApp.setValuePermission(
                        AppModelPermission(
                            arrayListSetting,
                            arrayListSettingSystem
                        )
                    )
                } else {
                    Toast.makeText(requireActivity(), "No application found", Toast.LENGTH_LONG)
                        .show()
                }
            }
            binding.packagesClick.setOnClickListener {
                if (arrayListPackages.isNotEmpty() || arrayListPackagesSystem.isNotEmpty()) {
                    val bottomDialogPermission = BottomDialogPermission(
                        "All Files Access"
                    )
                    bottomDialogPermission.show(
                        requireActivity().supportFragmentManager,
                        bottomDialogPermission.tag
                    )

                    mvvmBottomApp.setValuePermission(
                        AppModelPermission(
                            arrayListPackages,
                            arrayListPackagesSystem
                        )
                    )
                } else {
                    Toast.makeText(requireActivity(), "No application found", Toast.LENGTH_LONG)
                        .show()
                }
            }*/
            binding.nearblyClick.setOnClickListener {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
                    return@setOnClickListener;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                if (arrayListNearbly.isNotEmpty() || arrayListNearblySystem.isNotEmpty()) {
                    val bottomDialogPermission = BottomDialogPermission(
                        "Nearby Devices"
                    )
                    bottomDialogPermission.show(
                        requireActivity().supportFragmentManager,
                        bottomDialogPermission.tag
                    )

                    mvvmBottomApp.setValuePermission(
                        AppModelPermission(
                            arrayListNearbly,
                            arrayListNearblySystem
                        )
                    )
                } else {
                    Toast.makeText(requireActivity(), "No application found", Toast.LENGTH_LONG)
                        .show()
                }
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
              /*  binding.FilesClick.visibility = View.VISIBLE
                binding.nearblyClick.visibility = View.VISIBLE*/
                binding.SpcialClick.visibility = View.VISIBLE
            } else {
             /*   binding.nearblyClick.visibility = View.GONE
                binding.FilesClick.visibility = View.GONE*/
                binding.SpcialClick.visibility = View.GONE
            }

        }
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onChange(app: ApplicationInfo) {

          val appName = "App Name"
   /*     val it = app.packageName*/
          mAppUserPermission(app = app,appName)


    }

    override fun onChangeSystem(app : ApplicationInfo) {
        val it = app.packageName
        val appName = "App Name"
        mAppSystemPermission(app = app,appName)

    }

    @SuppressLint("SetTextI18n")
    override fun onFinish() {

        isAccessibilityServiceEnabled(requireContext())

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
   /*     binding.appFiles.text = " ${arrayListAllFilesAccess.size + arrayListAllFilesAccessSystem.size} apps"*/
        binding.appStorage.text = " ${arrayListStorage.size + arrayListStorageSystem.size} apps"
        binding.appLocation.text = " ${arrayListLocation.size + arrayListLocationSystem.size} apps"
        binding.appMicrophone.text = " ${arrayListMic.size + arrayListMicSystem.size} apps"
        binding.appPhone.text = " ${arrayListPhone.size + arrayListPhoneSystem.size} apps"
        binding.appNearbly.text = " ${arrayListNearbly.size + arrayListNearblySystem.size} apps"
   /*     binding.appPackages.text = " ${arrayListPackages.size +arrayListPackagesSystem.size} apps"*/
      /*  binding.appSetting.text = " ${arrayListSetting.size + arrayListSettingSystem.size} apps"*/

        })
    }



    private fun getGrantStatus( app: ApplicationInfo): Boolean {
        try {
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
            return   mode == AppOpsManager.MODE_ALLOWED
        }catch (re : RuntimeException ) {
            return   false

        }
/*        return if (mode == AppOpsManager.MODE_DEFAULT) {
            requireActivity().checkCallingOrSelfPermission(Manifest.permission.PACKAGE_USAGE_STATS) == PackageManager.PERMISSION_GRANTED
        } else {*/

     //   }
    }

   private fun isAccessibilityServiceEnabled(
        context: Context,
    ) {
       if (arrayListAccessibility.isNotEmpty()){
           arrayListAccessibility.clear()
       }
       if (arrayListAccessibilitySystem.isNotEmpty()) {
           arrayListAccessibilitySystem.clear()
       }

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

    private fun isSystem (applicationInfo: ApplicationInfo) : Boolean {
        return applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM != 0
    }


    private fun mAppSystemPermission (app: ApplicationInfo, appName : String) {
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
            isPermissionGranted(it ,"android.permission.SEND_SMS") ||
            isPermissionGranted(it ,"android.permission.RECEIVE_SMS"))
        {
            arrayListSmsSystem.add(AppModel(0, appName.toString(), app.packageName, null, "null",app))
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (isPermissionGranted(it, "android.permission.NEARBY_WIFI_DEVICES")) {
                arrayListNearblySystem.add(
                    AppModel(
                        0,
                        appName.toString(),
                        app.packageName,
                        null,
                        "null",
                        app
                    )
                )
            }
        }

        if (isPermissionGranted(it ,"android.permission.INSTALL_PACKAGES"))
        {
            arrayListPackagesSystem.add(AppModel(0, appName.toString(), app.packageName, null, "null",app))
        }

        if (isPermissionGranted(it ,"android.permission.WRITE_SETTINGS"))
        {
            arrayListSettingSystem.add(AppModel(0, appName.toString(), app.packageName, null, "null",app))
        }

      /*  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
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
        }*/
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
            isPermissionGranted(it ,"android.permission.SEND_SMS") ||
            isPermissionGranted(it ,"android.permission.RECEIVE_SMS"))
        {
            arrayListSms.add(AppModel(0, appName.toString(), app.packageName, null, "null",app))
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (isPermissionGranted(it, "android.permission.NEARBY_WIFI_DEVICES")) {
                arrayListNearbly.add(
                    AppModel(
                        0,
                        appName.toString(),
                        app.packageName,
                        null,
                        "null",
                        app
                    )
                )
            }
        }

     /*   if (checkPermissionPackages(app))
        {
            arrayListPackages.add(AppModel(0, appName.toString(), app.packageName, null, "null",app))
        }

        if (checkPermissionChangeSetting(app))
        {
            arrayListSetting.add(AppModel(0, appName.toString(), app.packageName, null, "null",app))
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
        }*/

        if (getGrantStatus(app)) {
            arrayListDataUsage.add(AppModel(
                2,
                appName.toString(), app.packageName, null, "null",app
            ))

        }

    }

    private fun deleteArray () {

/*        if (arrayListCamera.isNotEmpty()){
            arrayListCamera.clear()
        }
        if (arrayListCameraSystem.isNotEmpty()){
            arrayListCameraSystem.clear()
        }



        if (arrayListStorage.isNotEmpty()){
            arrayListStorage.clear()
        }
        if (arrayListStorageSystem.isNotEmpty()){
            arrayListStorageSystem.clear()
        }



        if (arrayListSetting.isNotEmpty()){
            arrayListSetting.clear()
        }
        if (arrayListSettingSystem.isNotEmpty()){
            arrayListSettingSystem.clear()
        }



        if (arrayListContact.isNotEmpty()){
            arrayListContact.clear()
        }
        if (arrayListContactSystem.isNotEmpty()){
            arrayListContactSystem.clear()
        }




        if (arrayListAccessibility.isNotEmpty()){
            arrayListAccessibility.clear()
        }
        if (arrayListAccessibilitySystem.isNotEmpty()){
            arrayListAccessibilitySystem.clear()
        }




        if (arrayListAllFilesAccess.isNotEmpty()){
            arrayListAllFilesAccess.clear()
        }
        if (arrayListAllFilesAccessSystem.isNotEmpty()){
            arrayListAllFilesAccessSystem.clear()
        }



        if (arrayListDataUsage.isNotEmpty()){
            arrayListDataUsage.clear()
        }
        if (arrayListDataUsageSystem.isNotEmpty()){
            arrayListDataUsageSystem.clear()
        }



        if (arrayListNearbly.isNotEmpty()){
            arrayListNearbly.clear()
        }
        if (arrayListNearblySystem.isNotEmpty()){
            arrayListNearblySystem.clear()
        }



        if (arrayListPackages.isNotEmpty()){
            arrayListPackages.clear()
        }
        if (arrayListPackagesSystem.isNotEmpty()){
            arrayListPackagesSystem.clear()
        }


        if (arrayListMic.isNotEmpty()){
            arrayListMic.clear()
        }
        if (arrayListMicSystem.isNotEmpty()){
            arrayListMicSystem.clear()
        }



        if (arrayListPhone.isNotEmpty()){
            arrayListPhone.clear()
        }
        if (arrayListPhoneSystem.isNotEmpty()){
            arrayListPhoneSystem.clear()
        }


        if (arrayListLocation.isNotEmpty()){
            arrayListLocation.clear()
        }
        if (arrayListLocationSystem.isNotEmpty()){
            arrayListLocationSystem.clear()
        }



        if (arrayListSms.isNotEmpty()){
            arrayListSms.clear()
        }

        if (arrayListSmsSystem.isNotEmpty()){
            arrayListSmsSystem.clear()
        }*/
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
        arrayListNearbly  =  ArrayList()
        arrayListBlueTooth  =  ArrayList()
        arrayListSetting  =  ArrayList()
        arrayListPackages  =  ArrayList()
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
        arrayListNearblySystem  =  ArrayList()
        arrayListSettingSystem  =  ArrayList()
        arrayListPackagesSystem  =  ArrayList()

    }

}

/*         arrayListStorage  =  ArrayList()
           arrayListSms  =  ArrayList()
           arrayListPhone  =  ArrayList()
           arrayListContact  =  ArrayList()
           arrayListAllFilesAccess  =  ArrayList()
           arrayListCamera  =  ArrayList()
           arrayListMic  =  ArrayList()
           arrayListLocation  =  ArrayList()
           arrayListDataUsage  =  ArrayList()
           arrayListAccessibility  =  ArrayList()
           arrayListNearbly  =  ArrayList()
           arrayListBlueTooth  =  ArrayList()
           arrayListSetting  =  ArrayList()
           arrayListPackages  =  ArrayList()
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
           arrayListNearblySystem  =  ArrayList()
           arrayListBlueToothSystem  =  ArrayList()
           arrayListSettingSystem  =  ArrayList()
           arrayListPackagesSystem  =  ArrayList()*/

/* arrayListStorage  =  ArrayList()
             arrayListSms  =  ArrayList()
             arrayListPhone  =  ArrayList()
             arrayListContact  =  ArrayList()
             arrayListAllFilesAccess  =  ArrayList()
             arrayListCamera  =  ArrayList()
             arrayListMic  =  ArrayList()
             arrayListLocation  =  ArrayList()
             arrayListDataUsage  =  ArrayList()
             arrayListAccessibility  =  ArrayList()
             arrayListNearbly  =  ArrayList()
             arrayListBlueTooth  =  ArrayList()
             arrayListSetting  =  ArrayList()
             arrayListPackages  =  ArrayList()
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
             arrayListNearblySystem  =  ArrayList()
             arrayListSettingSystem  =  ArrayList()
             arrayListPackagesSystem  =  ArrayList()*/


/*    private fun checkPermission(app: ApplicationInfo): Boolean {
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

    private fun checkPermissionPackages(app: ApplicationInfo): Boolean {
        val appOps = context?.getSystemService(AppOpsManager::class.java)
        val mode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            appOps?.unsafeCheckOpNoThrow(
                "android:request_install_packages",
                app.uid, app.packageName
            )
        } else {
            try {
                appOps?.checkOpNoThrow(
                    "android:request_install_packages",
                    app.uid, app.packageName
                )
            }catch (re: RuntimeException) {
                454554
            }
        }
        return when (mode) {
            AppOpsManager.MODE_DEFAULT -> (PackageManager.PERMISSION_GRANTED
                    == context?.checkPermission(
                Manifest.permission.INSTALL_PACKAGES, Process.myPid(), app.uid
            ))

            AppOpsManager.MODE_ALLOWED -> true
            AppOpsManager.MODE_ERRORED, AppOpsManager.MODE_IGNORED -> false
            else -> false
        }
    }

    private fun checkPermissionChangeSetting(app: ApplicationInfo): Boolean {
        val appOps = context?.getSystemService(AppOpsManager::class.java)
        val mode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            appOps?.unsafeCheckOpNoThrow(
                "android:write_settings",
                app.uid, app.packageName
            )
        } else {
            try {
                appOps?.checkOpNoThrow(
                    "android:write_settings",
                    app.uid, app.packageName
                )
            }catch (re: RuntimeException) {
                454554
            }
        }
        return when (mode) {
            AppOpsManager.MODE_DEFAULT -> (PackageManager.PERMISSION_GRANTED
                    == context?.checkPermission(
                Manifest.permission.WRITE_SETTINGS, Process.myPid(), app.uid
            ))

            AppOpsManager.MODE_ALLOWED -> true
            AppOpsManager.MODE_ERRORED, AppOpsManager.MODE_IGNORED -> false
            else -> false
        }
    }*/
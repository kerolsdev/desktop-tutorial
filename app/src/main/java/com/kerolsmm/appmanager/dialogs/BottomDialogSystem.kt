package com.kerolsmm.appmanager.dialogs

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.FileUriExposedException
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.kerolsmm.appmanager.databinding.BottomDialogSystemBinding
import com.kerolsmm.appmanager.model.AppModel
import com.kerolsmm.appmanager.mvvm.MvvmBottomApp
import java.io.File

class BottomDialogSystem()  : BottomSheetDialogFragment() {



    private lateinit var binding: BottomDialogSystemBinding

    private val mvvmBottomApp : MvvmBottomApp by activityViewModels()



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BottomDialogSystemBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mvvmBottomApp.getValue().observe(viewLifecycleOwner) {
            appModel ->

            binding.openAppStore.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    data = Uri.parse("market://details?id=${appModel.appPackage}")
                }
                startActivity(intent)
            }

            val appName = appModel?.applicationInfo?.let {
                context?.packageManager?.getApplicationLabel(
                    it
                )
            }

            binding.textAppName.text = appName ?: "appName"
            binding.packageManager.text = appModel.appPackage



            binding.openApp.setOnClickListener {

                val launchIntent = requireActivity().packageManager.getLaunchIntentForPackage(appModel.appPackage)
                if (launchIntent != null) {
                    launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(launchIntent)

                } else {
                    Toast.makeText(requireActivity(),"The application cannot be opened",Toast.LENGTH_LONG).show()
                }

            }

            binding.shareApk.setOnClickListener {
                shareApplication(appModel)
            }

            binding.shareLink.setOnClickListener {

                shareAppLink(appModel.appPackage)

            }



            binding.AppSize.setOnClickListener {

                val  bottomSize = appModel.applicationInfo?.let { it1 ->
                    BottomDialogAppSize(
                        packageName = appModel.appPackage,
                        it1
                    )
                }
                bottomSize?.show(requireActivity().supportFragmentManager,bottomSize.tag)
                dismiss()


            }
            binding.Appinfo.setOnClickListener {
                try {

                    val packageName = appModel.appPackage // Replace with your app's package name

                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    intent.data = Uri.parse("package:$packageName")
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)

                }catch (re :RuntimeException) {

                    Toast.makeText(requireActivity(),"The application cannot be opened",Toast.LENGTH_LONG).show()

                }

            }
        }



    }

    private fun getApkFile(packageName: String): File {
        val packageManager = requireActivity().packageManager
        val packageInfo = packageManager.getPackageInfo(packageName, 0)
        val sourceDir = packageInfo.applicationInfo.sourceDir
        return File(sourceDir)
    }

    fun shareAppLink(string: String) {
        // Replace "your.package.name" with your app's package name
        val appPackageName = string
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(
            Intent.EXTRA_TEXT,
            "https://play.google.com/store/apps/details?id=$appPackageName"
        )

        // Optionally, set a subject for the share
        intent.putExtra(Intent.EXTRA_SUBJECT, "Check out this app!")
        startActivity(Intent.createChooser(intent, "Share via"))
    }
    fun uninstallApp(context: Context, packageName: String) {
        val intent = Intent(Intent.ACTION_DELETE)
        intent.data = Uri.parse("package:$packageName")
        context.startActivity(intent)
    }
    private fun shareApplication(appModel: AppModel) {
        try {
            val filePath = File (appModel.appSource)
            val intent = Intent(Intent.ACTION_SEND)
            intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION

            // MIME of .apk is "application/vnd.android.package-archive".
            // but Bluetooth does not accept this. Let's use "*/*" instead.
            intent.type = "application/vnd.android.package-archive"


            // Append file and send Intent
            intent.putExtra(Intent.EXTRA_STREAM, Uri.parse(filePath.path))
            startActivity(Intent.createChooser(intent, "Share app via"))

        } catch (re : FileUriExposedException){

            val filePath = File (appModel.appSource)
            val uri = FileProvider.getUriForFile(requireContext(),"com.kerolsmm.appmanager.provider",filePath)
            val intent = Intent(Intent.ACTION_SEND)
            intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION

            // MIME of .apk is "application/vnd.android.package-archive".
            // but Bluetooth does not accept this. Let's use "*/*" instead.
            intent.type = "application/vnd.android.package-archive"


            // Append file and send Intent
            intent.putExtra(Intent.EXTRA_STREAM, uri)
            startActivity(Intent.createChooser(intent, "Share app via"))

        }
    }
}
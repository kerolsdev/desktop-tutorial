package com.kerolsmm.appmanager.dialogs

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.kerolsmm.appmanager.databinding.BottomDialogAppBinding
import com.kerolsmm.appmanager.model.AppModel
import com.kerolsmm.appmanager.mvvm.MvvmBottomApp
import java.io.File

class BottomDialogApp( private var clickBottomDialog: ClickBottomDialog?)  : BottomSheetDialogFragment() {

    constructor() : this(null)

    private lateinit var binding: BottomDialogAppBinding
    private val mvvmBottomApp : MvvmBottomApp by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BottomDialogAppBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        mvvmBottomApp.getValue().observe(viewLifecycleOwner) {
            appModel ->

        if ((appModel?.appPackage ?: " ") == requireActivity().packageName){
            binding.AppUninstall.visibility = View.GONE
            binding.openApp.visibility = View.GONE
            binding.shareApk.visibility  = View.GONE
        }


        val appName = appModel?.applicationInfo?.let {
            context?.packageManager?.getApplicationLabel(
                it
            )
        }

        binding.textAppName.text = appName ?: "appName"
        binding.packageManager.text = appModel?.appPackage



        binding.openAppStore.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                data = Uri.parse("market://details?id=${appModel?.appPackage}")
            }
            startActivity(intent)
        }

        binding.openApp.setOnClickListener {

            val launchIntent = appModel?.let { it1 ->
                requireActivity().packageManager.getLaunchIntentForPackage(
                    it1.appPackage)
            }
            if (launchIntent != null) {
                launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(launchIntent)

            } else {
                Toast.makeText(requireActivity(),"The application cannot be opened",Toast.LENGTH_LONG).show()
            }

        }

        binding.shareApk.setOnClickListener {
            appModel?.let { it1 -> shareApplication(it1) }
        }

        binding.shareLink.setOnClickListener {

            appModel?.let { it1 -> shareAppLink(it1.appPackage) }

        }

        binding.AppUninstall.setOnClickListener {

            appModel?.let { it1 -> clickBottomDialog?.onClickBottom(packageName = it1) }
            dismiss()

        }



        binding.AppSize.setOnClickListener {

            val  bottomSize = appModel?.let { it1 -> appModel.applicationInfo?.let { it2 ->
                BottomDialogAppSize(
                    packageName = it1.appPackage,
                    it2
                )
            } }
            bottomSize?.show(requireActivity().supportFragmentManager,bottomSize.tag)
            dismiss()

        }

        binding.Appinfo.setOnClickListener {
            try {

            val packageName = appModel?.appPackage // Replace with your app's package name

            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            intent.data = Uri.parse("package:$packageName")
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            } catch (re :  RuntimeException) {

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

    private fun shareApplication(appModel: AppModel) {
        try {

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


        } catch (re : RuntimeException){

            val filePath = File (appModel.appSource)
            val intent = Intent(Intent.ACTION_SEND)
            intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION

            // MIME of .apk is "application/vnd.android.package-archive".
            // but Bluetooth does not accept this. Let's use "*/*" instead.
            intent.type = "application/vnd.android.package-archive"


            // Append file and send Intent
            intent.putExtra(Intent.EXTRA_STREAM, Uri.parse(filePath.path))
            startActivity(Intent.createChooser(intent, "Share app via"))

        } catch (_: RuntimeException ){


        }

    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = BottomSheetDialog(requireContext(), theme)
        dialog.setOnShowListener {

            val bottomSheetDialog = it as BottomSheetDialog
            val parentLayout =
                bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            parentLayout?.let { it ->
                val behaviour = BottomSheetBehavior.from(it)
                behaviour.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }
        return dialog
    }

    private fun setupFullHeight(bottomSheet: View) {
        val layoutParams = bottomSheet.layoutParams
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
        bottomSheet.layoutParams = layoutParams
    }


    interface ClickBottomDialog {
        fun onClickBottom (packageName: AppModel)
    }


}
package com.kerols.phoneboost.Fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.text.format.Formatter
import android.util.Log
import android.view.*
import androidx.core.content.ContextCompat
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.play.core.review.ReviewManagerFactory
import com.kerols.phoneboost.BuildConfig
import com.kerols.phoneboost.R
import com.kerols.phoneboost.Utils.StorageSize
import com.kerols.phoneboost.Utils.Utils
import com.kerols.phoneboost.databinding.FragmentHomeBinding


class FragmentHome : Fragment() {


    private lateinit var binding : FragmentHomeBinding
    private val TAG : String = "App"
    private val utils : Utils? by  lazy { context?.let { Utils(it) } }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val menuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                // Add menu items here
                menuInflater.inflate(R.menu.home, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when (menuItem.itemId) {

                    R.id.Rate -> {

                        try {
                            val appPackageName = requireActivity().packageName // getPackageName() from Context or Activity object
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName"))
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
                        } catch (runtimeException: RuntimeException) {
                            Log.e("TAG", "onOptionsItemSelected: ", runtimeException.cause)
                        }
                    }

                    R.id.share -> {
                        try {
                            val shareIntent = Intent(Intent.ACTION_SEND)
                            shareIntent.type = "text/plain"
                            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My application name")
                            var shareMessage = """
                     ${getString(R.string.let_recommend)} """.trimIndent()
                            shareMessage =
                                """
                      ${shareMessage}https://play.google.com/store/apps/details?id=${BuildConfig.APPLICATION_ID} """.trimIndent()
                            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
                            startActivity(Intent.createChooser(shareIntent, "choose one"))
                        } catch (e: Exception) {
                            //e.toString();
                        }


                    }
                    R.id.support -> {
                        // Creating new intent
                        // Creating new intent
                        val email = Intent(Intent.ACTION_SEND)
                        email.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        /*Write here Email to */
                        /*Write here Email to */email.putExtra(Intent.EXTRA_EMAIL, arrayOf("example@gmail.com"))
                        /*Write Subject */
                        /*Write Subject */email.putExtra(Intent.EXTRA_SUBJECT, "${getString(R.string.app_name)} - Feedback")
                        /*Write Message*/
                        /*Write Message*/email.putExtra(Intent.EXTRA_TEXT, "")
                        email.type = "message/rfc822"
                        startActivity(email)
                    }
                    R.id.Privacy -> {
                        findNavController().navigate(FragmentHomeDirections.actionFragmentHomeToPrivacy2())
                    }

                }
                return true
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

        val boolean : BooleanArray = BooleanArray(4)
        boolean[0] = false
        boolean[1] = true
        boolean[2] = false
        boolean[3] = true


        val tot = StorageSize().getStorageApiBelowTotal()
        val ava = StorageSize().getStorageApiBelow()
        val usedStorage = tot - ava

        binding.freeusedstorage.text = Formatter.formatFileSize(requireActivity(),StorageSize().getStorageApiBelow())

        binding.cardQuick.setOnClickListener{
            if (checkPermission()){
               findNavController().navigate(FragmentHomeDirections.actionFragmentHomeToCleanerHome())
            }else {
               findNavController().navigate(FragmentHomeDirections.actionFragmentHomeToFragmentPermissionFile())
            }

        }

        if (utils?.getMedia() == true){
              binding.notification.visibility = View.INVISIBLE
        }

        if (utils?.getApps() == true){
            binding.notification3.visibility = View.INVISIBLE
        }


        if (utils?.getApps() == true && utils?.getMedia() == true && utils?.getReview() == true){
            try {
                mRateApp()
            }catch (re: RuntimeException) {
                Log.e(TAG, "onViewCreated: ", re )
            }

        }

        binding.cardMedia.setOnClickListener(View.OnClickListener {
            if (checkPermission()){
                findNavController().navigate(FragmentHomeDirections.actionFragmentHomeToMediaHomeFragment())
            }else {
                findNavController().navigate(FragmentHomeDirections.actionFragmentHomeToFragmentPermissionFile())
            }

        })

        binding.cardApps.setOnClickListener(View.OnClickListener {
            findNavController().navigate(FragmentHomeDirections.actionFragmentHomeToNavPackages())
        })

    }

    override fun onDestroyView() {
        super.onDestroyView()

    }

    override fun onResume() {
        super.onResume()
    }


    private fun checkPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            Environment.isExternalStorageManager()
        } else {
            val result =
                ContextCompat.checkSelfPermission(requireActivity(),
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
            val result1 =
                ContextCompat.checkSelfPermission(requireActivity(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED
        }
    }

    @SuppressLint("SuspiciousIndentation")
    private fun mRateApp() {
        val manager = ReviewManagerFactory.create(requireActivity())
        val request = manager.requestReviewFlow()
        request.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Log.e(TAG, "mRateApp: " + "Review App Succeed" )
                // We got the ReviewInfo object
                utils?.saveReview()
                val reviewInfo = task.result
                val flow = manager.launchReviewFlow(requireActivity(), reviewInfo)
                    flow.addOnCompleteListener {
                        // The flow has finished. The API does not indicate whether the user
                        // reviewed or not, or even whether the review dialog was shown. Thus, no
                        // matter the result, we continue our app flow.
                    }


            } else {
                // There was some problem, log or handle the error code.
                /*utils?.getBattery() == true && utils?.getApps() == true
            &&*/
            }
        }

    }
}
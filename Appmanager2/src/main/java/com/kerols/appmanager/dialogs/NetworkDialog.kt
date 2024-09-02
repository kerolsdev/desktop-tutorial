package com.kerols.appmanager.dialogs

import android.app.Dialog
import android.app.usage.UsageEvents
import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Typeface
import android.net.Uri
import android.opengl.Visibility
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.kerols.appmanager.R
import com.kerols.appmanager.databinding.NetworkDialogBinding
import com.kerols.appmanager.functions.AppDataUsageModel
import com.kerols.appmanager.functions.NetworkStatsHelper
import com.kerols.appmanager.functions.NetworkStatsHelper.formatData


class NetworkDialog(var model: AppDataUsageModel) : BottomSheetDialogFragment() {



    private lateinit var binding: NetworkDialogBinding


    private val handler : Handler = Handler(Looper.getMainLooper())


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = NetworkDialogBinding.inflate(inflater,container,false)
        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)




        try {
            if (model.packageName == requireActivity().getString(R.string.package_tethering)) {


                Glide.with(requireActivity()).load(R.drawable.mobile_hotspot_svgrepo_com).into(binding.AppIcon)
                binding.sentData.text = formatData(model.sentMobile, model.receivedMobile)[0]
                binding.ReceivedData.text = formatData(model.sentMobile, model.receivedMobile)[1]
                binding.appName.text = model.appName

                binding.appSettings.visibility = View.GONE
                binding.UID.visibility = View.GONE
                binding.BackgroundTime.visibility = View.GONE
                binding.ScreenTime.visibility = View.GONE
                binding.TotalData.visibility = View.GONE
                binding.PackageName.visibility = View.GONE


                binding.viewBackgroundTime.visibility = View.GONE
                binding.viewPackageName.visibility = View.GONE
                binding.viewUID.visibility = View.GONE
                binding.viewTotalUsage.visibility = View.GONE
                binding.viewScreenTime.visibility = View.GONE

            } else if (model.packageName == requireActivity().getString(R.string.package_removed)) {


                Glide.with(requireActivity()).load(R.drawable.delete_svgrepo_com).into(binding.AppIcon)
                binding.sentData.text = formatData(model.sentMobile, model.receivedMobile)[0]
                binding.ReceivedData.text = formatData(model.sentMobile, model.receivedMobile)[1]
                binding.appName.text = model.appName

                binding.appSettings.visibility = View.GONE
                binding.UID.visibility = View.GONE
                binding.BackgroundTime.visibility = View.GONE
                binding.ScreenTime.visibility = View.GONE
                binding.TotalData.visibility = View.GONE
                binding.PackageName.visibility = View.GONE


                binding.viewBackgroundTime.visibility = View.GONE
                binding.viewPackageName.visibility = View.GONE
                binding.viewUID.visibility = View.GONE
                binding.viewTotalUsage.visibility = View.GONE
                binding.viewScreenTime.visibility = View.GONE



            } else {

                val uid = resources.getString(R.string.app_label_uid, model.uid)

                val packageName = resources.getString(R.string.app_label_package_name, model.packageName)

                val total: Long = model.sentMobile + model.receivedMobile + model.sentWifi + model.receivedWifi

                val combinedTotal: String = formatData(0L, total)[2]

                Glide.with(requireActivity()).load(model.packageName?.let { requireActivity().packageManager.getApplicationIcon(it) }).into(binding.AppIcon)
                binding.UID.text =  model.uid.toString()
                binding.PackageName.text = model.packageName
                binding.sentData.text = formatData(model.sentMobile, model.receivedMobile)[0]
                binding.ReceivedData.text = formatData(model.sentMobile, model.receivedMobile)[1]
                binding.TotalData.text = combinedTotal
                binding.appName.text = model.appName
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    Thread(Runnable {

                        try {


                        var appScreenTime: String = "null"
                        var appBackgroundTime: String = "null"

                        if (model.packageName !== getString(R.string.package_tethering)) {

                            val usageTime = model.packageName?.let { it1 ->
                                getUsageTime(
                                    requireContext(),
                                    it1,
                                    model.session
                                )
                            }
                            if ((usageTime?.get(1) ?: -1) == -1) {
                                // If value is -1, build version is below Q

                                val screenTime = formatTime((usageTime?.get(0) ?: 1) / 60f)
                                appScreenTime = setBoldSpan(
                                    screenTime,
                                    formatTime((usageTime?.get(0) ?: 1) / 60f)
                                ).toString()
                                appBackgroundTime = "0 min"


                            } else {
                                val screenTime = formatTime((usageTime?.get(0) ?: 1) / 60f)
                                val backgroundTime = formatTime((usageTime?.get(1) ?: 1) / 60f)
                                appScreenTime =
                                    formatTime((usageTime?.get(0) ?: 1) / 60f).toString()
                                appBackgroundTime =
                                    formatTime((usageTime?.get(1) ?: 1) / 60f).toString()

                            }

                        } else {
                            appScreenTime = "0 min"
                            appBackgroundTime = "0 min"
                        }

                        handler.post(Runnable {

                            binding.ScreenTime.text = appScreenTime
                            binding.BackgroundTime.text = appBackgroundTime

                        })
                        }catch (re : RuntimeException) {
                            binding.viewScreenTime.visibility = View.GONE
                            binding.viewBackgroundTime.visibility = View.GONE
                        }

                    }).start()
                } else {
                    binding.viewScreenTime.visibility = View.GONE
                    binding.viewBackgroundTime.visibility = View.GONE
                }

            }
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }



        binding.appSettings.setOnClickListener {
            try {

            val packageName = model.packageName // Replace with your app's package name
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            intent.data = Uri.parse("package:$packageName")
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)

            }catch (re : RuntimeException) {
                Log.e("TAG", "onViewCreated: ",re )
            }
        }





    }

    private fun setBoldSpan(text: String, spanText: String): SpannableString {
        val boldSpan = SpannableString(text)
        var start = text.indexOf(spanText)
        if (start < 0) {
            start = 0
        }
        val end = start + spanText.length
        boldSpan.setSpan(StyleSpan(Typeface.BOLD), start, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
        return boldSpan
    }


    private fun formatTime(minutes: Float): String {
        if (minutes < 1 && minutes > 0) {
            return "Less than a minute"
        }
        if (minutes >= 60) {
            val f = minutes / 3.6f
            val hours = (minutes / 60).toInt()
            val mins = (minutes % 60).toInt()
            val hourLabel: String
            val minuteLabel: String
            hourLabel = if (hours > 1) {
                "hours"
            } else {
                "hour"
            }
            minuteLabel = if (mins == 1) {
                "minute"
            } else {
                "minutes"
            }
            return requireActivity().getString(
                R.string.usage_time_label,
                hours,
                hourLabel,
                mins,
                minuteLabel
            )
        }
        return if (minutes == 1f) {
            Math.round(minutes).toString() + " minute"
        } else Math.round(minutes).toString() + " minutes"
    }



    private fun getUsageTime(context: Context, packageName: String, session: Int): IntArray {

        var currentEvent: UsageEvents.Event
        val allEvents: MutableList<UsageEvents.Event> = ArrayList<UsageEvents.Event>()
        val appScreenTime = HashMap<String, Int?>()
        val appBackgroundTime = HashMap<String, Int?>()
        val usageStatsManager: UsageStatsManager =
            context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        var usageEvents: UsageEvents? = null
        var usageStats: List<UsageStats>? = null
        assert(usageStatsManager != null)
        try {
            usageEvents = usageStatsManager.queryEvents(
                NetworkStatsHelper.getTimePeriod(context, session, 1).get(0),
                NetworkStatsHelper.getTimePeriod(context, session, 1).get(1)
            )
            usageStats = usageStatsManager.queryUsageStats(
                UsageStatsManager.INTERVAL_DAILY,
                NetworkStatsHelper.getTimePeriod(context, session, 1).get(0),
                NetworkStatsHelper.getTimePeriod(context, session, 1).get(1)
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
        if (usageEvents != null) {
            if (usageEvents.hasNextEvent()) {
                while (usageEvents.hasNextEvent()) {
                    currentEvent = UsageEvents.Event()
                    usageEvents.getNextEvent(currentEvent)
                    if (currentEvent.packageName == packageName) {
                        if (currentEvent.eventType == UsageEvents.Event.ACTIVITY_RESUMED || currentEvent.eventType == UsageEvents.Event.ACTIVITY_PAUSED || currentEvent.getEventType() == UsageEvents.Event.FOREGROUND_SERVICE_START || currentEvent.getEventType() == UsageEvents.Event.FOREGROUND_SERVICE_STOP) {
                            allEvents.add(currentEvent)
                            val key: String = currentEvent.packageName
                            if (appScreenTime[key] == null) appScreenTime[key] = 0
                        }
                    }
                }
                if (allEvents.size > 0) {
                    for (i in 0 until allEvents.size - 1) {
                        val E0: UsageEvents.Event = allEvents[i]
                        val E1: UsageEvents.Event = allEvents[i + 1]
                        if (E0.eventType == UsageEvents.Event.ACTIVITY_RESUMED && E1.getEventType() == UsageEvents.Event.ACTIVITY_PAUSED && E0.getClassName() == E1.getClassName()) {
                            var diff: Int = (E1.timeStamp - E0.timeStamp).toInt()
                            diff /= 1000
                            var prev = appScreenTime[E0.packageName]
                            if (prev == null) prev = 0
                            appScreenTime[E0.packageName] = prev + diff
                        }
                    }
                    val lastEvent: UsageEvents.Event = allEvents[allEvents.size - 1]
                    if (lastEvent.eventType == UsageEvents.Event.ACTIVITY_RESUMED) {
                        var diff: Int =
                            System.currentTimeMillis().toInt() - lastEvent.getTimeStamp().toInt()
                        diff /= 1000
                        var prev = appScreenTime[lastEvent.packageName]
                        if (prev == null) prev = 0
                        appScreenTime[lastEvent.packageName] = prev + diff
                    }
                } else {
                    appScreenTime[packageName] = 0
                }
            }
        }

        // Check background app usage time
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (usageStats!!.isNotEmpty()) {
                for (i in usageStats.indices) {
                    if (usageStats[i].packageName == packageName) {
                        val backgroundTime: Int =
                            usageStats[i].totalTimeForegroundServiceUsed.toInt() / 1000
                        appBackgroundTime[packageName] = backgroundTime
                        break
                    }
                }
            } else {
                appBackgroundTime[packageName] = 0
            }
        } else {
            appBackgroundTime[packageName] = -1
        }
        if (appBackgroundTime[packageName] == null) {
            appBackgroundTime[packageName] = 0
        }


//        return appScreenTime.get(packageName);
        return intArrayOf(appScreenTime[packageName]!!, appBackgroundTime[packageName]!!)
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


}



/*
 * Copyright (C) 2021 Dr.NooB
 *
 * This file is a part of Data Monitor <https://github.com/itsdrnoob/DataMonitor>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *//*

package com.kerols.appmanager.adapters

import android.annotation.SuppressLint
import android.app.Activity
import android.app.usage.UsageEvents
import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.drnoob.datamonitor.Common.isAppInstalled
import com.google.android.material.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.kerols.appmanager.functions.AppDataUsageModel
import com.kerols.appmanager.functions.NetworkStatsHelper
import com.kerols.appmanager.functions.NetworkStatsHelper.formatData
import com.kerols.appmanager.functions.Values.DAILY_DATA_HOME_ACTION
import com.kerols.appmanager.functions.Values.DATA_USAGE_SESSION
import com.kerols.appmanager.functions.Values.DATA_USAGE_SYSTEM
import com.kerols.appmanager.functions.Values.DATA_USAGE_TYPE
import com.kerols.appmanager.functions.Values.GENERAL_FRAGMENT_ID
import com.skydoves.progressview.ProgressView

class AppDataUsageAdapter(mList: List<AppDataUsageModel>, mContext: Context) :
    RecyclerView.Adapter<AppDataUsageAdapter.AppDataUsageViewHolder?>() {
    private val mList: List<AppDataUsageModel>
    private val mContext: Context
    private val animate: Boolean? = null
    var fromHome: Boolean? = null
    var activity: Activity? = null

    init {
        this.mList = mList
        this.mContext = mContext
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppDataUsageViewHolder {
        val view: View =
            LayoutInflater.from(mContext).inflate(R.layout.app_data_usage_item, parent, false)
        return AppDataUsageViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: AppDataUsageViewHolder, position: Int) {
        val model: AppDataUsageModel = mList[position]
        try {
            if (model.packageName == "com.android.tethering") {
                holder.mAppIcon.setImageResource(R.drawable.hotspot)
            } else if (model.packageName == "com.android.deleted") {
                holder.mAppIcon.setImageResource(R.drawable.deleted_apps)
            } else {
                if (isAppInstalled(mContext, model.packageName)) {
                    holder.mAppIcon.setImageDrawable(
                        mContext.packageManager.getApplicationIcon(
                            model.packageName
                        )
                    )
                } else {
                    holder.mAppIcon.setImageResource(R.drawable.deleted_apps)
                }
            }
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        val totalDataUsage: String = formatData(
            model.sentMobile,
            model.receivedMobile
        ).get(2)
        if (model.progress > 0) {
            holder.mProgress.progress = model.progress
        } else {
            holder.mProgress.progress = 1
        }
        holder.mAppName.setText(model.appName)
        holder.mDataUsage.text = totalDataUsage
        holder.itemView.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                if (model.packageName == mContext.getString(R.string.package_system)) {
                    val intent = Intent(mContext, ContainerActivity::class.java)
                    //                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra(GENERAL_FRAGMENT_ID, DATA_USAGE_SYSTEM)
                    intent.putExtra(DATA_USAGE_SESSION, model.session)
                    intent.putExtra(DATA_USAGE_TYPE, model.type)
                    intent.putExtra(DAILY_DATA_HOME_ACTION, fromHome)
                    mContext.startActivity(intent)
                } else {
                    val dialog = BottomSheetDialog(mContext, R.style.BottomSheet)
                    val dialogView: View =
                        LayoutInflater.from(mContext).inflate(R.layout.app_detail_view, null)
                    dialog.setContentView(dialogView)
                    val appIcon = dialogView.findViewById<ImageView>(R.id.icon)
                    val appName = dialogView.findViewById<TextView>(R.id.name)
                    val dataSent = dialogView.findViewById<TextView>(R.id.data_sent)
                    val dataReceived = dialogView.findViewById<TextView>(R.id.data_received)
                    val appPackage = dialogView.findViewById<TextView>(R.id.app_package)
                    val appUid = dialogView.findViewById<TextView>(R.id.app_uid)
                    val appScreenTime = dialogView.findViewById<TextView>(R.id.app_screen_time)
                    val appBackgroundTime =
                        dialogView.findViewById<TextView>(R.id.app_background_time)
                    val appCombinedTotal =
                        dialogView.findViewById<TextView>(R.id.app_combined_total)
                    val appSettings: MaterialButton =
                        dialogView.findViewById<MaterialButton>(R.id.app_open_settings)
                    appName.setText(model.appName)
                    val packageName = mContext.resources.getString(
                        R.string.app_label_package_name,
                        model.packageName
                    )
                    val uid = mContext.resources.getString(
                        R.string.app_label_uid,
                        model.uid
                    )
                    appPackage.setText(setBoldSpan(packageName, model.packageName))
                    appUid.setText(setBoldSpan(uid, model.uid.toString()))
                    if (model.packageName !== mContext.getString(R.string.package_tethering)) {
                        val screenTime = mContext.getString(
                            R.string.app_label_screen_time,
                            mContext.getString(R.string.label_loading)
                        )
                        val backgroundTime = mContext.getString(
                            R.string.app_label_background_time,
                            mContext.getString(R.string.label_loading)
                        )
                        appScreenTime.setText(
                            setBoldSpan(
                                screenTime,
                                mContext.getString(R.string.label_loading)
                            )
                        )
                        appBackgroundTime.setText(
                            setBoldSpan(
                                backgroundTime,
                                mContext.getString(R.string.label_loading)
                            )
                        )
                        val loadScreenTime =
                            LoadScreenTime(model, activity, appScreenTime, appBackgroundTime)
                        loadScreenTime.execute()
                    } else {
                        appScreenTime.visibility = View.GONE
                        appBackgroundTime.visibility = View.GONE
                    }
                    val total: Long =
                        model.sentMobile + model.receivedMobile + model.sentWifi + model.receivedWifi
                    val combinedTotal: String = formatData(0L, total).get(2)
                    dataSent.setText(formatData(model.sentMobile, model.receivedMobile).get(0))
                    dataReceived.setText(formatData(model.sentMobile, model.receivedMobile).get(1))
                    appCombinedTotal.setText(
                        setBoldSpan(
                            mContext.getString(R.string.app_label_combined_total, combinedTotal),
                            combinedTotal
                        )
                    )
                    appSettings.setOnClickListener(View.OnClickListener {
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        val uri = Uri.fromParts("package", model.packageName, null)
                        intent.setData(uri)
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        mContext.startActivity(intent)
                    })
                    try {
                        if (model.packageName == mContext.getString(R.string.package_tethering)) {
                            appIcon.setImageResource(R.drawable.hotspot)
                            appPackage.visibility = View.GONE
                            appUid.visibility = View.GONE
                            appSettings.setVisibility(View.GONE)
                            appCombinedTotal.visibility = View.GONE
                        } else if (model.packageName == mContext.getString(R.string.package_removed)) {
                            appIcon.setImageResource(R.drawable.deleted_apps)
                            appPackage.visibility = View.GONE
                            appUid.visibility = View.GONE
                            appSettings.setVisibility(View.GONE)
                        } else {
                            if (isAppInstalled(mContext, model.packageName)) {
                                appIcon.setImageDrawable(
                                    mContext.packageManager.getApplicationIcon(
                                        model.packageName
                                    )
                                )
                            } else {
                                appIcon.setImageResource(R.drawable.deleted_apps)
                            }
                        }
                    } catch (e: PackageManager.NameNotFoundException) {
                        e.printStackTrace()
                    }
                    dialog.setOnShowListener { dialogInterface ->
                        val bottomSheetDialog: BottomSheetDialog =
                            dialogInterface as BottomSheetDialog
                        val bottomSheet: FrameLayout? =
                            bottomSheetDialog.findViewById<FrameLayout>(
                                R.id.design_bottom_sheet
                            )
                        val behavior: BottomSheetBehavior<FrameLayout>? =
                            bottomSheet?.let { BottomSheetBehavior.from<FrameLayout>(it) }
                        behavior?.state = BottomSheetBehavior.STATE_EXPANDED
                        behavior?.setSkipCollapsed(true)
                    }
                    dialog.show()
                }
            }
        })
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

    private inner class LoadScreenTime(
        model: AppDataUsageModel,
        activity: Activity?,
        appScreenTime: TextView,
        appBackgroundTime: TextView
    ) : AsyncTask<Any?, Any?, Any?>() {
        private val model: AppDataUsageModel
        private val activity: Activity?
        var appScreenTime: TextView
        var appBackgroundTime: TextView

        init {
            this.model = model
            this.activity = activity
            this.appScreenTime = appScreenTime
            this.appBackgroundTime = appBackgroundTime
        }

        override fun doInBackground(objects: Array<Any?>): Any? {
            assert(this.activity != null)
            if (model.packageName !== mContext.getString(R.string.package_tethering)) {
                val usageTime = getUsageTime(mContext, model.packageName, model.session)
                if (usageTime[1] == -1) {
                    // If value is -1, build version is below Q
                    activity!!.runOnUiThread {
                        val screenTime = mContext.getString(
                            R.string.app_label_screen_time,
                            formatTime(usageTime[0] / 60f)
                        )
                        appScreenTime.setText(
                            setBoldSpan(
                                screenTime,
                                formatTime(usageTime[0] / 60f)
                            )
                        )
                        appBackgroundTime.visibility = View.GONE
                    }
                } else {
                    activity!!.runOnUiThread {
                        val screenTime = mContext.getString(
                            R.string.app_label_screen_time,
                            formatTime(usageTime[0] / 60f)
                        )
                        val backgroundTime = mContext.getString(
                            R.string.app_label_background_time,
                            formatTime(usageTime[1] / 60f)
                        )
                        appScreenTime.setText(
                            setBoldSpan(
                                screenTime,
                                formatTime(usageTime[0] / 60f)
                            )
                        )
                        appBackgroundTime.setText(
                            setBoldSpan(
                                backgroundTime, formatTime(
                                    usageTime[1] / 60f
                                )
                            )
                        )
                    }
                }
            } else {
                activity!!.runOnUiThread {
                    appScreenTime.visibility = View.GONE
                    appBackgroundTime.visibility = View.GONE
                }
            }
            return null
        }
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
            return mContext.getString(
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

    override fun getItemCount(): Int {
        return mList.size
    }

    private fun getUsageTime(context: Context, packageName: String, session: Int): IntArray {
        */
/**
         * Returns app usage time as an array like [screenTime, backgroundTime]
         * ScreenTime source credit: https://stackoverflow.com/questions/61677505/how-to-count-app-usage-time-while-app-is-on-foreground
         *//*

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
                    if (currentEvent.getPackageName() == packageName) {
                        if (currentEvent.getEventType() == UsageEvents.Event.ACTIVITY_RESUMED || currentEvent.getEventType() == UsageEvents.Event.ACTIVITY_PAUSED || currentEvent.getEventType() == UsageEvents.Event.FOREGROUND_SERVICE_START || currentEvent.getEventType() == UsageEvents.Event.FOREGROUND_SERVICE_STOP) {
                            allEvents.add(currentEvent)
                            val key: String = currentEvent.getPackageName()
                            if (appScreenTime[key] == null) appScreenTime[key] = 0
                        }
                    }
                }
                if (allEvents.size > 0) {
                    for (i in 0 until allEvents.size - 1) {
                        val E0: UsageEvents.Event = allEvents[i]
                        val E1: UsageEvents.Event = allEvents[i + 1]
                        if (E0.getEventType() == UsageEvents.Event.ACTIVITY_RESUMED && E1.getEventType() == UsageEvents.Event.ACTIVITY_PAUSED && E0.getClassName() == E1.getClassName()) {
                            var diff: Int = (E1.getTimeStamp() - E0.getTimeStamp()).toInt()
                            diff /= 1000
                            var prev = appScreenTime[E0.getPackageName()]
                            if (prev == null) prev = 0
                            appScreenTime[E0.getPackageName()] = prev + diff
                        }
                    }
                    val lastEvent: UsageEvents.Event = allEvents[allEvents.size - 1]
                    if (lastEvent.getEventType() == UsageEvents.Event.ACTIVITY_RESUMED) {
                        var diff: Int =
                            System.currentTimeMillis().toInt() - lastEvent.getTimeStamp().toInt()
                        diff /= 1000
                        var prev = appScreenTime[lastEvent.getPackageName()]
                        if (prev == null) prev = 0
                        appScreenTime[lastEvent.getPackageName()] = prev + diff
                    }
                } else {
                    appScreenTime[packageName] = 0
                }
            }
        }

        // Check background app usage time
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (usageStats!!.size > 0) {
                for (i in usageStats.indices) {
                    if (usageStats[i].getPackageName() == packageName) {
                        val backgroundTime: Int =
                            usageStats[i].getTotalTimeForegroundServiceUsed().toInt() / 1000
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

    inner class AppDataUsageViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val mAppIcon: ImageView
        val mAppName: TextView
        val mDataUsage: TextView
        val mProgress: ProgressView

        init {
            mAppIcon = itemView.findViewById<ImageView>(R.id.app_icon)
            mAppName = itemView.findViewById<TextView>(R.id.app_name)
            mDataUsage = itemView.findViewById<TextView>(R.id.data_usage)
            mProgress = itemView.findViewById<ProgressView>(R.id.progress)
        }
    }

    companion object {
        private const val TAG = "AppDataUsageAdapter"
    }
}*/

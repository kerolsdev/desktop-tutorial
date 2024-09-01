package com.kerolsmm.appmanager.fragments

import android.Manifest
import android.app.AppOpsManager
import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Context.USAGE_STATS_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Process
import android.os.SystemClock
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.kerolsmm.appmanager.R
import com.kerolsmm.appmanager.adapters.AdapterBattery
import com.kerolsmm.appmanager.databinding.FragmentBatteryBinding
import com.kerolsmm.appmanager.dialogs.BottomDialogBattery
import com.kerolsmm.appmanager.dialogs.BottomDialogFilterBattery
import com.kerolsmm.appmanager.functions.MvvmData
import com.kerolsmm.appmanager.mvvm.MvvmBottomApp
import com.kerolsmm.phonecleaner.Activitys.App
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Collections
import java.util.Date
import java.util.Locale
import java.util.TreeMap
import java.util.concurrent.TimeUnit
import java.util.stream.Collectors


public class BatteryFragment() : Fragment(), BottomDialogFilterBattery.ClickFilterBattery,
    AdapterBattery.OnClick {


    private lateinit var binding: FragmentBatteryBinding
    private var mUsageStatsManager: UsageStatsManager? = null
    private var runUi: Handler = Handler(Looper.getMainLooper())
    private var appsList : ArrayList<App?> = ArrayList()
    private var adapterBattery : AdapterBattery?  = null
    private var session : String = "Daily"
    private var finish : Boolean = false
    private val mvvmBottomApp : MvvmBottomApp by activityViewModels()
    private val mvvmData : MvvmData by activityViewModels()
    private var mLastClickTime: Long = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBatteryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (context != null && activity != null) {
            mUsageStatsManager = activity
                ?.getSystemService(USAGE_STATS_SERVICE) as UsageStatsManager //Context.USAGE_STATS_SERVICE

            adapterBattery = AdapterBattery(ArrayList(), requireContext(), this)

            binding.listitems.adapter = adapterBattery

            binding.FilterBattery.setOnClickListener {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return@setOnClickListener;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                if (finish) {
                    val bottomFilterBattery = BottomDialogFilterBattery(session, this)
                    bottomFilterBattery.show(
                        requireActivity().supportFragmentManager,
                        bottomFilterBattery.tag
                    )
                }
            }

            isPermission()

            val menuHost = requireActivity()
            menuHost.addMenuProvider(object : MenuProvider {
                override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                    // Add menu items here
                    menuInflater.inflate(R.menu.search, menu)
                    val myActionMenuItem = menu.findItem(R.id.action_search)
                    val searchView = myActionMenuItem.actionView as SearchView?

                    searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                        override fun onQueryTextSubmit(query: String): Boolean {
                            return false
                        }

                        override fun onQueryTextChange(s: String?): Boolean {
                            if (finish) {
                                s?.let { filter(it) }
                            }
                            return true
                        }
                    })
                }

                override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                    // Handle the menu selection
                    return true
                }
            }, viewLifecycleOwner, androidx.lifecycle.Lifecycle.State.RESUMED)

            binding.swipeRefresh.setOnRefreshListener {
                finish = false
                binding.swipeRefresh.isEnabled = false
                binding.swipeRefresh.isRefreshing = false;
                binding.animationView.visibility = View.VISIBLE
                binding.viewFullScreen.visibility = View.GONE
                binding.FilterBattery.visibility = View.GONE
                isPermission()
            }
        }

    }


    private fun isPermission() {
        if (getGrantStatus()) {
            try {
                finish = false
                binding.viewFullScreen.visibility = View.GONE
                binding.animationView.visibility = View.VISIBLE
                binding.FilterBattery.visibility = View.GONE
                binding.swipeRefresh.isEnabled = false
                val statsUsageInterval = StatsUsageInterval.getValue(session)
                statsUsageInterval?.mInterval?.let { updateAppsList(it) }
            } catch (re: RuntimeException) {

            }
        } else {
            try {
                val intent2 = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
                intent2.data = Uri.parse("package:" + requireContext().packageName)
                startActivity(intent2)
            } catch (re: RuntimeException) {
                val intent2 = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
                startActivity(intent2)
            }
        }

    }

    private fun isAppInfoAvailable(usageStats: UsageStats): Boolean {
        return try {
            requireContext().packageManager.getApplicationInfo(usageStats.packageName, 0)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }

    private fun getGrantStatus(): Boolean {
        val appOps = requireContext()
            .getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
        val mode = appOps.checkOpNoThrow(
            AppOpsManager.OPSTR_GET_USAGE_STATS,
            Process.myUid(), requireContext().packageName
        )
        return if (mode == AppOpsManager.MODE_DEFAULT) {
            requireContext().checkCallingOrSelfPermission(Manifest.permission.PACKAGE_USAGE_STATS) == PackageManager.PERMISSION_GRANTED
        } else {
            mode == AppOpsManager.MODE_ALLOWED
        }
    }

    private fun getDurationBreakdown(millis: Long): String {
        var millis = millis
        require(millis >= 0) { "Duration must be greater than zero!" }
        val hours = TimeUnit.MILLISECONDS.toHours(millis)
        millis -= TimeUnit.HOURS.toMillis(hours)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(millis)
        millis -= TimeUnit.MINUTES.toMillis(minutes)
        val seconds = TimeUnit.MILLISECONDS.toSeconds(millis)
        return "$hours h $minutes m $seconds s"
    }


    internal enum class StatsUsageInterval(
         val mStringRepresentation: String,
         val mInterval: Int
    ) {
        DAILY("Daily", UsageStatsManager.INTERVAL_DAILY),
        WEEKLY("Weekly", UsageStatsManager.INTERVAL_WEEKLY),
        MONTHLY("Monthly", UsageStatsManager.INTERVAL_MONTHLY),
        YEARLY("Yearly", UsageStatsManager.INTERVAL_YEARLY);

        companion object {
            fun getValue(stringRepresentation: String): StatsUsageInterval? {
                for (statsUsageInterval in entries) {
                    if (statsUsageInterval.mStringRepresentation == stringRepresentation) {
                        return statsUsageInterval
                    }
                }
                return null
            }
        }
    }

   private class LastTimeLaunchedComparatorDesc : Comparator<UsageStats?> {
        override fun compare(left: UsageStats?, right: UsageStats?): Int {
            return java.lang.Long.compare(right!!.lastTimeUsed, left!!.lastTimeUsed)
        }
    }


    private fun updateAppsList(intervalType : Int) {

        Thread(Runnable {
     try {

        var usageStatsList: List<UsageStats> = ArrayList()
            val cal: Calendar = Calendar.getInstance()

            if (appsList.isNotEmpty()){
                appsList.clear()
            }

            when(intervalType){
                UsageStatsManager.INTERVAL_DAILY -> {
                    cal.add(Calendar.DAY_OF_YEAR, -1)
                }

                UsageStatsManager.INTERVAL_YEARLY -> {
                    cal.add(Calendar.YEAR, -1)
                }
                UsageStatsManager.INTERVAL_WEEKLY -> {
                    cal.add(Calendar.WEEK_OF_YEAR, -1)
                }
                UsageStatsManager.INTERVAL_MONTHLY -> {
                    cal.add(Calendar.MONTH, -1)
                }
            }

         // Convert milliseconds to Date object

         // Convert milliseconds to Date object
      /*   val date = Date(timestamp_ms)

         // Format the date string according to your desired format (e.g., yyyy-MM-dd)

         // Format the date string according to your desired format (e.g., yyyy-MM-dd)
         val sdf = SimpleDateFormat("yyyy-MM-dd")
         val dateString: String = sdf.format(date)*/


            var queryUsageStats: List<UsageStats> = mUsageStatsManager!!.queryUsageStats(
            intervalType ,  cal.timeInMillis,
            System.currentTimeMillis()
        )

        if (queryUsageStats.isEmpty()) {
            Log.i("TAG", "The user may not allow the access to apps usage. ")
        } else {


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                queryUsageStats =
                    queryUsageStats.stream()
                        .filter { app: UsageStats -> app.totalTimeInForeground > 0 || app.totalTimeForegroundServiceUsed > 0  }.collect(
                        Collectors.toList()
                    )

            }else {

                queryUsageStats =
                    queryUsageStats.stream()
                        .filter { app: UsageStats -> app.totalTimeInForeground > 0 }.collect(
                            Collectors.toList()
                        )
            }


            // Group the usageStats by application and sort them by total time in foreground
            if (queryUsageStats.isNotEmpty()) {
                val mySortedMap: MutableMap<String, UsageStats> = TreeMap()
                for (usageStats in queryUsageStats) {
                    mySortedMap[usageStats.packageName] = usageStats
                }
                usageStatsList = ArrayList(mySortedMap.values)
            }


           // Collections.sort(usageStatsList, LastTimeLaunchedComparatorDesc())

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                Collections.sort(
                    usageStatsList,
                    java.util.Comparator.comparingLong { obj: UsageStats -> obj.totalTimeInForeground + obj.totalTimeForegroundServiceUsed }
                )
            } else {
                Collections.sort(
                    usageStatsList,
                    java.util.Comparator.comparingLong { obj: UsageStats -> obj.totalTimeInForeground }
                )
            }

            val totalTime =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    usageStatsList.stream()
                        .map { obj: UsageStats -> obj.totalTimeInForeground }
                        .mapToLong { obj: Long -> obj }
                        .sum()
                } else {
                    1
                }

            val totalTimeBackground =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    usageStatsList.stream()
                        .map { obj: UsageStats -> obj.totalTimeForegroundServiceUsed }
                        .mapToLong { obj: Long -> obj }
                        .sum()
                } else {
                    1
                }

          /*  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                binding.ScreenTimeBatteryTotal.text = getDurationBreakdown(totalTime)
                binding.BackgroundTimeBatteryTotal.text = getDurationBreakdown(totalTimeBackground)
            }else {
                binding.ScreenTimeBatteryTotal.text = getDurationBreakdown(totalTime)
                binding.viewBackgroundTime.visibility = View.GONE
            }*/

            for (usageStats in usageStatsList) {

                try {
                    val packageName = usageStats.packageName
                    var icon = ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.no_image
                    ) //getDrawable();
                    var appName = usageStats.packageName

                    if (isAppInfoAvailable(usageStats)) {
                        val ai = requireContext().packageManager.getApplicationInfo(packageName, 0)
                        icon = requireContext().packageManager.getApplicationIcon(ai)
                        appName = requireContext().packageManager.getApplicationLabel(ai).toString()
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        val backgroundScreen = getDurationBreakdown(usageStats.totalTimeForegroundServiceUsed)
                        val screenTime = getDurationBreakdown(usageStats.totalTimeInForeground)
                        val usageDuration: String =
                            getDurationBreakdown(usageStats.totalTimeInForeground + usageStats.totalTimeForegroundServiceUsed)
                        val usagePercentage =
                            ((usageStats.totalTimeInForeground + usageStats.totalTimeForegroundServiceUsed )* 100 /(totalTimeBackground + totalTime)).toInt()
                        val usageStatDTO =
                            App(icon!!, appName, usagePercentage, usageDuration, usageStats.packageName,backgroundScreen,screenTime)

                        appsList.add(usageStatDTO)
                    }else {
                        val usageDuration: String =
                            getDurationBreakdown(usageStats.totalTimeInForeground)
                        val usagePercentage =
                            (usageStats.totalTimeInForeground * 100 / totalTime).toInt()
                        val usageStatDTO =
                            App(icon!!, appName, usagePercentage, usageDuration, usageStats.packageName,null,usageDuration)

                        appsList.add(usageStatDTO)
                    }


                } catch (re: RuntimeException) {
                    Log.e("TAG", "updateAppsList: ", re)

                }
            }
            appsList.reverse()
            runUi.post(Runnable {
                adapterBattery?.setAppInfos(appInfos = appsList)
                binding.viewFullScreen.visibility = View.VISIBLE
                binding.animationView.visibility = View.GONE
                binding.FilterBattery.visibility = View.VISIBLE
                finish = true
                binding.swipeRefresh.isEnabled = true
            })
        }

}catch (re : RuntimeException) {
    Log.e("TAG", "updateAppsList: ", re )
}

        }).start()

    }

    override fun onClick(session: String) {
        this.session = session
        isPermission()
    }

    private fun filter(text: String) {
        val filteredList: ArrayList<App?> = ArrayList<App?>()
        for (item in appsList) {
            if (item?.appName?.lowercase(Locale.ROOT)?.contains(text.lowercase(Locale.getDefault())) == true) {
                filteredList.add(item)
            }
        }
        adapterBattery?.setAppInfos(filteredList)
    }

    override fun onEventClick(appInfo: App?) {
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();
        val bottomDialogBattery : BottomDialogBattery = BottomDialogBattery()
        bottomDialogBattery.show(requireActivity().supportFragmentManager,bottomDialogBattery.tag)
        mvvmBottomApp.setValueAppModelBattery(appInfo ?: App())
    }

}
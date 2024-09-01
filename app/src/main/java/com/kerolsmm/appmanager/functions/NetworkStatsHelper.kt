package com.kerolsmm.appmanager.functions

import android.annotation.SuppressLint
import android.app.usage.NetworkStats
import android.app.usage.NetworkStatsManager
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.RemoteException

import android.telephony.TelephonyManager
import com.google.android.material.datepicker.MaterialDatePicker
import com.kerolsmm.appmanager.R

import com.kerolsmm.appmanager.functions.Values.SESSION_ALL_TIME
import com.kerolsmm.appmanager.functions.Values.SESSION_CUSTOM
import com.kerolsmm.appmanager.functions.Values.SESSION_LAST_MONTH
import com.kerolsmm.appmanager.functions.Values.SESSION_MONTHLY
import com.kerolsmm.appmanager.functions.Values.SESSION_THIS_MONTH
import com.kerolsmm.appmanager.functions.Values.SESSION_THIS_YEAR
import com.kerolsmm.appmanager.functions.Values.SESSION_TODAY
import com.kerolsmm.appmanager.functions.Values.SESSION_YESTERDAY
import com.kerolsmm.appmanager.model.AppModel
import org.checkerframework.checker.units.qual.C
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date


/* Created by Dr.NooB on 23/09/2021 */
object NetworkStatsHelper {
    private val TAG = NetworkStatsHelper::class.java.simpleName
/*    private val gson: com.google.gson.Gson = com.google.gson.Gson()
    private val type: Type =
        object : com.google.common.reflect.TypeToken<List<AppModel?>?>() {}.getType()*/

    private fun getSubscriberId(context: Context): String? {
        return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            val telephonyManager =
                context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            var subscriberId: String? = ""
            try {
                subscriberId = telephonyManager.subscriberId
            } catch (e: Exception) {
                e.printStackTrace()
            }
            subscriberId
        } else {
            null
        }
    }

    @Throws(ParseException::class, RemoteException::class)
    fun getDeviceWifiDataUsage(context: Context, session: Int): Array<Long> {
        val data: Array<Long>
        val resetTimeMillis = getTimePeriod(context, session, 1)[0]
        val endTimeMillis = getTimePeriod(context, session, 1)[1]
        var sent: Long
        var received: Long
        var total: Long
        var excludedSent = 0L
        var excludedReceived = 0L
        var excludedTotal = 0L

        val networkStatsManager =
            context.getSystemService(Context.NETWORK_STATS_SERVICE) as NetworkStatsManager
        var bucket = NetworkStats.Bucket()
        bucket = networkStatsManager.querySummaryForDevice(
            NetworkCapabilities.TRANSPORT_WIFI,
            getSubscriberId(context),
            resetTimeMillis,
            endTimeMillis
        )
        received = bucket.rxBytes
        sent = bucket.txBytes
        total = sent + received
        sent = sent - excludedSent
        received = received - excludedReceived
        total = total - excludedTotal
        data = arrayOf(sent, received, total)
        return data
    }

    @Throws(ParseException::class, RemoteException::class)
    fun getDeviceMobileDataUsage(
        context: Context,
        session: Int,
        startDate: Int
    ): Array<Long> {
        val networkStatsManager =
            context.getSystemService(Context.NETWORK_STATS_SERVICE) as NetworkStatsManager
        var bucket = NetworkStats.Bucket()
        val resetTimeMillis =
            getTimePeriod(context, session, startDate)[0]
        val endTimeMillis =
            getTimePeriod(context, session, startDate)[1]
        var sent = 0L
        var received = 0L
        var total = 0L
        var excludedSent = 0L
        var excludedReceived = 0L
        var excludedTotal = 0L


        bucket = networkStatsManager.querySummaryForDevice(
            NetworkCapabilities.TRANSPORT_CELLULAR,
            getSubscriberId(context),
            resetTimeMillis,
            endTimeMillis
        )

//        NetworkStats networkStats = networkStatsManager.querySummary(NetworkCapabilities.TRANSPORT_CELLULAR,
//                getSubscriberId(context),
//                resetTimeMillis,
//                endTimeMillis);

//        Long rxBytes = 0L;
//        Long txBytes = 0L;
//
//        do {
//            networkStats.getNextBucket(bucket);
//            rxBytes += bucket.getRxBytes();
//            txBytes += bucket.getTxBytes();
//        }
//        while (networkStats.hasNextBucket());
        val rxBytes = bucket.rxBytes
        val txBytes = bucket.txBytes
        sent = txBytes
        received = rxBytes
        total = sent + received
        sent = sent - excludedSent
        received = received - excludedReceived
        total = total - excludedTotal
        return arrayOf(sent, received, total)
    }

    @Throws(ParseException::class, RemoteException::class)
    fun getTotalAppWifiDataUsage(context: Context, session: Int): Array<Long> {
        val data: Array<Long>
        val resetTimeMillis = getTimePeriod(context, session, 1)[0]
        val endTimeMillis = getTimePeriod(context, session, 1)[1]
        var sent: Long
        var received: Long
        var total: Long
        var excludedSent = 0L
        var excludedReceived = 0L
        var excludedTotal = 0L


        val networkStatsManager =
            context.getSystemService(Context.NETWORK_STATS_SERVICE) as NetworkStatsManager
        val bucket = NetworkStats.Bucket()
        val networkStats = networkStatsManager.querySummary(
            NetworkCapabilities.TRANSPORT_WIFI,
            getSubscriberId(context),
            resetTimeMillis,
            endTimeMillis
        )
        received = 0L
        sent = 0L
        do {
            networkStats.getNextBucket(bucket)
            received += bucket.rxBytes
            sent += bucket.txBytes
        } while (networkStats.hasNextBucket())
        total = sent + received
        sent = sent - excludedSent
        received = received - excludedReceived
        total = total - excludedTotal
        data = arrayOf(sent, received, total)
        return data
    }

    @Throws(ParseException::class, RemoteException::class)
    fun getTotalAppMobileDataUsage(
        context: Context,
        session: Int,
        startDate: Int
    ): Array<Long> {
        val networkStatsManager =
            context.getSystemService(Context.NETWORK_STATS_SERVICE) as NetworkStatsManager
        val bucket = NetworkStats.Bucket()
        val resetTimeMillis =
            getTimePeriod(context, session, startDate)[0]
        val endTimeMillis =
            getTimePeriod(context, session, startDate)[1]
        var sent = 0L
        var received = 0L
        var total = 0L
        var excludedSent = 0L
        var excludedReceived = 0L
        var excludedTotal = 0L


        val networkStats = networkStatsManager.querySummary(
            NetworkCapabilities.TRANSPORT_CELLULAR,
            getSubscriberId(context),
            resetTimeMillis,
            endTimeMillis
        )
        var rxBytes = 0L
        var txBytes = 0L
        do {
            networkStats.getNextBucket(bucket)
            rxBytes += bucket.rxBytes
            txBytes += bucket.txBytes
        } while (networkStats.hasNextBucket())
        sent = txBytes
        received = rxBytes
        total = sent + received
        sent = sent - excludedSent
        received = received - excludedReceived
        total = total - excludedTotal
        return arrayOf(sent, received, total)
    }

    @Throws(ParseException::class, RemoteException::class)
    fun getAppWifiDataUsage(
        context: Context,
        uid: Int,
        session: Int
    ): Array<Long> {
        val networkStatsManager =
            context.applicationContext.getSystemService(Context.NETWORK_STATS_SERVICE) as NetworkStatsManager
        var networkStats: NetworkStats? = null
        var sent = 0L
        var received = 0L
        var total = 0L
        val resetTimeMillis =
            getTimePeriod(context, session, 1)[0]
        val endTimeMillis =
            getTimePeriod(context, session, 1)[1]
        networkStats = networkStatsManager
            .querySummary(
                NetworkCapabilities.TRANSPORT_WIFI,
                getSubscriberId(context),
                resetTimeMillis,
                endTimeMillis
            )
        do {
            val bucket = NetworkStats.Bucket()
            networkStats.getNextBucket(bucket)
            if (bucket.uid == uid) {
                sent = sent + bucket.txBytes
                received = received + bucket.rxBytes
            }
        } while (networkStats.hasNextBucket())
        total = sent + received
        networkStats.close()
        return arrayOf(sent, received, total)
    }

    @Throws(RemoteException::class, ParseException::class)
    fun getAppMobileDataUsage(
        context: Context,
        uid: Int,
        session: Int
    ): Array<Long> {
        val networkStatsManager =
            context.applicationContext.getSystemService(Context.NETWORK_STATS_SERVICE) as NetworkStatsManager
        var networkStats: NetworkStats? = null
        var total = 0L
        var sent = 0L
        var received = 0L
        val resetTimeMillis =
            getTimePeriod(context, session, 1)[0]
        val endTimeMillis =
            getTimePeriod(context, session, 1)[1]
        networkStats = networkStatsManager.querySummary(
            NetworkCapabilities.TRANSPORT_CELLULAR,
            getSubscriberId(context),
            resetTimeMillis,
            endTimeMillis
        )
        do {
            val bucket = NetworkStats.Bucket()
            networkStats.getNextBucket(bucket)
            if (bucket.uid == uid) {
                sent = sent + bucket.txBytes
                received = received + bucket.rxBytes
            }
        } while (networkStats.hasNextBucket())
        total = sent + received
        networkStats.close()
        return arrayOf(sent, received, total)
    }

    fun formatData(sent: Long, received: Long): Array<String> {
        val total = sent + received
        val data: Array<String>
        val totalBytes = total / 1024f
        val sentBytes = sent / 1024f
        val receivedBytes = received / 1024f
        val totalMB = totalBytes / 1024f
        val totalGB: Float
        val sentGB: Float
        val sentMB: Float
        val receivedGB: Float
        val receivedMB: Float
        sentMB = sentBytes / 1024f
        receivedMB = receivedBytes / 1024f
        var sentData = ""
        var receivedData = ""
        val totalData: String
        if (totalMB > 1024) {
            totalGB = totalMB / 1024f
            totalData = String.format("%.2f", totalGB) + " GB"
        } else {
            totalData = String.format("%.2f", totalMB) + " MB"
        }
        if (sentMB > 1024) {
            sentGB = sentMB / 1024f
            sentData = String.format("%.2f", sentGB) + " GB"
        } else {
            sentData = String.format("%.2f", sentMB) + " MB"
        }
        if (receivedMB > 1024) {
            receivedGB = receivedMB / 1024f
            receivedData = String.format("%.2f", receivedGB) + " GB"
        } else {
            receivedData = String.format("%.2f", receivedMB) + " MB"
        }
        data = arrayOf(sentData, receivedData, totalData)
        return data
    }


    @Throws(ParseException::class, RemoteException::class)
    fun getTetheringDataUsage(
        context: Context,
        session: Int
    ): Array<Long> {
        val networkStatsManager =
            context.applicationContext.getSystemService(Context.NETWORK_STATS_SERVICE) as NetworkStatsManager
        var networkStats: NetworkStats? = null
        var total = 0L
        var sent = 0L
        var received = 0L
        val resetTimeMillis =
            getTimePeriod(context, session, 1)[0]
        val endTimeMillis =
            getTimePeriod(context, session, 1)[1]
        networkStats = networkStatsManager.querySummary(
            NetworkCapabilities.TRANSPORT_CELLULAR,
            getSubscriberId(context),
            resetTimeMillis,
            endTimeMillis
        )
        do {
            val bucket = NetworkStats.Bucket()
            networkStats.getNextBucket(bucket)
            if (bucket.uid == NetworkStats.Bucket.UID_TETHERING) {
                sent = sent + bucket.txBytes
                received = received + bucket.rxBytes
            }
        } while (networkStats.hasNextBucket())
        total = sent + received
        networkStats.close()
        return arrayOf(sent, received, total)
    }

    @Throws(RemoteException::class, ParseException::class)
    fun getDeletedAppsMobileDataUsage(
        context: Context,
        session: Int
    ): Array<Long> {
        val networkStatsManager =
            context.applicationContext.getSystemService(Context.NETWORK_STATS_SERVICE) as NetworkStatsManager
        var networkStats: NetworkStats? = null
        var total = 0L
        var sent = 0L
        var received = 0L
        val resetTimeMillis =
            getTimePeriod(context, session, 1)[0]
        val endTimeMillis =
            getTimePeriod(context, session, 1)[1]
        networkStats = networkStatsManager.querySummary(
            NetworkCapabilities.TRANSPORT_CELLULAR,
            getSubscriberId(context),
            resetTimeMillis,
            endTimeMillis
        )
        do {
            val bucket = NetworkStats.Bucket()
            networkStats.getNextBucket(bucket)
            if (bucket.uid == NetworkStats.Bucket.UID_REMOVED) {
                sent = sent + bucket.txBytes
                received = received + bucket.rxBytes
            }
        } while (networkStats.hasNextBucket())
        total = sent + received
        networkStats.close()
        return arrayOf(sent, received, total)
    }

    @Throws(RemoteException::class, ParseException::class)
    fun getDeletedAppsWifiDataUsage(
        context: Context,
        session: Int
    ): Array<Long> {
        val networkStatsManager =
            context.applicationContext.getSystemService(Context.NETWORK_STATS_SERVICE) as NetworkStatsManager
        var networkStats: NetworkStats? = null
        var total = 0L
        var sent = 0L
        var received = 0L
        val resetTimeMillis =
            getTimePeriod(context, session, 1)[0]
        val endTimeMillis =
            getTimePeriod(context, session, 1)[1]
        networkStats = networkStatsManager.querySummary(
            NetworkCapabilities.TRANSPORT_WIFI,
            getSubscriberId(context),
            resetTimeMillis,
            endTimeMillis
        )
        do {
            val bucket = NetworkStats.Bucket()
            networkStats.getNextBucket(bucket)
            if (bucket.uid == NetworkStats.Bucket.UID_REMOVED) {
                sent = sent + bucket.txBytes
                received = received + bucket.rxBytes
            }
        } while (networkStats.hasNextBucket())
        total = sent + received
        networkStats.close()
        return arrayOf(sent, received, total)
    }

    @SuppressLint("SimpleDateFormat")
    @Throws(ParseException::class)
    fun getTimePeriod(context: Context, session: Int, startDate: Int): Array<Long> {
        var year: Int
        var month: Int
        var day: Int
        var resetTimeMillis = 0L
        var endTimeMillis = 0L
        var planStartDateMillis: Long
        var planEndDateMillis: Long
        try {
            planStartDateMillis =  MaterialDatePicker.todayInUtcMilliseconds()
            planEndDateMillis =  MaterialDatePicker.todayInUtcMilliseconds()
        } catch (e: ClassCastException) {
            val planStartIntValue = -1
            val planEndIntValue = -1
            planStartDateMillis = (planStartIntValue as Number).toLong()
            planEndDateMillis = (planEndIntValue as Number).toLong()
        }
        val resetHour = 0
        val resetMin = 0
        val customStartHour =  0
        val customStartMin =  0
        val customEndHour =11
        val customEndMin = 59
        val date = Date()
        val yearFormat = SimpleDateFormat("yyyy")
        val monthFormat = SimpleDateFormat("MM")
        val dayFormat = SimpleDateFormat("dd")
        val dateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
        var startTime: String
        var endTime: String
        var resetDate: Date
        var endDate: Date
        val calendar = Calendar.getInstance()
        val monthlyResetDate = 1
        val today = calendar[Calendar.DAY_OF_MONTH] + 1
        when (session) {
            SESSION_TODAY -> {
                year = yearFormat.format(date).toInt()
                month = monthFormat.format(date).toInt()
                day = dayFormat.format(date).toInt()
                startTime = context.resources.getString(
                    R.string.reset_time,
                    year,
                    month,
                    day,
                    resetHour,
                    resetMin
                )
                resetDate = dateFormat.parse(startTime)
                resetTimeMillis = resetDate.time
                day = dayFormat.format(date).toInt() + 1
                endTime = context.resources.getString(
                    R.string.reset_time,
                    year,
                    month,
                    day,
                    resetHour,
                    resetMin
                )
                endDate = dateFormat.parse(endTime)
                endTimeMillis = endDate.time
                calendar.add(Calendar.DATE, 1)
            }

            SESSION_YESTERDAY -> {
                year = yearFormat.format(date).toInt()
                month = monthFormat.format(date).toInt()
                day = dayFormat.format(date).toInt() - 1
                startTime = context.resources.getString(
                    R.string.reset_time,
                    year,
                    month,
                    day,
                    resetHour,
                    resetMin
                )
                resetDate = dateFormat.parse(startTime)
                resetTimeMillis = resetDate.time
                day = dayFormat.format(date).toInt()
                endTime = context.resources.getString(
                    R.string.reset_time,
                    year,
                    month,
                    day,
                    resetHour,
                    resetMin
                )
                endDate = dateFormat.parse(endTime)
                endTimeMillis = endDate.time
            }

            SESSION_THIS_MONTH -> {
                year = yearFormat.format(date).toInt()
                month = monthFormat.format(date).toInt()
                day = startDate
                startTime = context.resources.getString(
                    R.string.reset_time,
                    year,
                    month,
                    day,
                    resetHour,
                    resetMin
                )
                resetDate = dateFormat.parse(startTime)
                resetTimeMillis = resetDate.time
                day = dayFormat.format(date).toInt() + 1
                endTime = context.resources.getString(
                    R.string.reset_time,
                    year,
                    month,
                    day,
                    resetHour,
                    resetMin
                )
                endDate = dateFormat.parse(endTime)
                endTimeMillis = endDate.time
            }

            SESSION_LAST_MONTH -> { //                year = Integer.parseInt(yearFormat.format(date));
//                month = Integer.parseInt(monthFormat.format(date)) - 1;
//                day = 1;
//                startTime = context.getResources().getString(R.string.reset_time, year, month, day, resetHour, resetMin);
//                resetDate = dateFormat.parse(startTime);
//                resetTimeMillis = resetDate.getTime();
//
//                month = Integer.parseInt(monthFormat.format(date));
//                endTime = context.getResources().getString(R.string.reset_time, year, month, day, resetHour, resetMin);
//                endDate = dateFormat.parse(endTime);
//                endTimeMillis = endDate.getTime();
                /**
                 * When data reset date is ahead of today's date, reducing 1 from the current month will
                 * only give the month when the current plan started.
                 * So to get the last month's period, 2 has to be subtracted to get the starting month
                 * and 1 to get the ending month
                 * For eg: Today is 4th of August and plan resets on 8th of August, subtracting 2 & 1
                 * respectively will wive the period of June 8th to July 8th, i.e period of last month.
                 */
                if (monthlyResetDate >= today) {
                    // Time period from reset date of previous month till today
                    year = Integer.parseInt(yearFormat.format(date));
                    month = Integer.parseInt(monthFormat.format(date)) - 2;
                    day = monthlyResetDate;
                    startTime = context.getResources().getString(R.string.reset_time, year, month, day, resetHour, resetMin);
                    resetDate = dateFormat.parse(startTime);
                    resetTimeMillis = resetDate.getTime();

                    month = Integer.parseInt(monthFormat.format(date)) - 1;
                    day = monthlyResetDate;
                    endTime = context.getResources().getString(R.string.reset_time, year, month, day, resetHour, resetMin);
                    endDate = dateFormat.parse(endTime);
                    endTimeMillis = endDate.getTime();
                }
                else {
                    // Reset date is in the current month.
                    year = Integer.parseInt(yearFormat.format(date));
                    month = Integer.parseInt(monthFormat.format(date)) - 1;
                    day = monthlyResetDate;
                    startTime = context.getResources().getString(R.string.reset_time, year, month, day, resetHour, resetMin);
                    resetDate = dateFormat.parse(startTime);
                    resetTimeMillis = resetDate.getTime();

//                    day = monthlyResetDate;
                    month += 1; // To restore back the current month.
                    endTime = context.getResources().getString(R.string.reset_time, year, month, day, resetHour, resetMin);
                    endDate = dateFormat.parse(endTime);
                    endTimeMillis = endDate.getTime();
                }
            }

            SESSION_THIS_YEAR -> {
                year = yearFormat.format(date).toInt()
                month = 1
                day = 1
                startTime = context.resources.getString(
                    R.string.reset_time,
                    year,
                    month,
                    day,
                    resetHour,
                    resetMin
                )
                resetDate = dateFormat.parse(startTime)
                resetTimeMillis = resetDate.time
                month = monthFormat.format(date).toInt()
                day = dayFormat.format(date).toInt() + 1
                endTime = context.resources.getString(
                    R.string.reset_time,
                    year,
                    month,
                    day,
                    resetHour,
                    resetMin
                )
                endDate = dateFormat.parse(endTime)
                endTimeMillis = endDate.time
            }

            SESSION_ALL_TIME -> {
                resetTimeMillis = 0L
                year = yearFormat.format(date).toInt()
                month = monthFormat.format(date).toInt()
                day = dayFormat.format(date).toInt() + 1
                endTime = context.resources.getString(
                    R.string.reset_time,
                    year,
                    month,
                    day,
                    resetHour,
                    resetMin
                )
                endDate = dateFormat.parse(endTime)
                endTimeMillis = endDate.time
            }

            SESSION_MONTHLY -> if (monthlyResetDate >= today) {
                // Time period from reset date of previous month till today
                year = yearFormat.format(date).toInt()
                month = monthFormat.format(date).toInt() - 1
                day = monthlyResetDate
                startTime = context.resources.getString(
                    R.string.reset_time,
                    year,
                    month,
                    day,
                    resetHour,
                    resetMin
                )
                resetDate = dateFormat.parse(startTime)
                resetTimeMillis = resetDate.time
                month = monthFormat.format(date).toInt()
                day = today
                endTime = context.resources.getString(
                    R.string.reset_time,
                    year,
                    month,
                    day,
                    resetHour,
                    resetMin
                )
                endDate = dateFormat.parse(endTime)
                endTimeMillis = endDate.time
            } else {
                // Reset date is in the current month.
                year = yearFormat.format(date).toInt()
                month = monthFormat.format(date).toInt()
                day = monthlyResetDate
                startTime = context.resources.getString(
                    R.string.reset_time,
                    year,
                    month,
                    day,
                    resetHour,
                    resetMin
                )
                resetDate = dateFormat.parse(startTime)
                resetTimeMillis = resetDate.time
                day = dayFormat.format(date).toInt() + 1
                endTime = context.resources.getString(
                    R.string.reset_time,
                    year,
                    month,
                    day,
                    resetHour,
                    resetMin
                )
                endDate = dateFormat.parse(endTime)
                endTimeMillis = endDate.time
            }

            SESSION_CUSTOM -> {
                year = yearFormat.format(planStartDateMillis).toInt()
                month = monthFormat.format(planStartDateMillis).toInt()
                day = dayFormat.format(planStartDateMillis).toInt()
                startTime = context.resources
                    .getString(
                        R.string.reset_time,
                        year,
                        month,
                        day,
                        customStartHour,
                        customStartMin
                    )
                resetDate = dateFormat.parse(startTime)
                resetTimeMillis = resetDate.time
                year = yearFormat.format(planEndDateMillis).toInt()
                month = monthFormat.format(planEndDateMillis).toInt()
                day = dayFormat.format(planEndDateMillis).toInt()
                endTime = context.resources
                    .getString(R.string.reset_time, year, month, day, customEndHour, customEndMin)
                endDate = dateFormat.parse(endTime)
                endTimeMillis = endDate.time
            }
        }
        if (resetTimeMillis > System.currentTimeMillis()) {
            year = yearFormat.format(date).toInt()
            month = monthFormat.format(date).toInt()
            day = dayFormat.format(date).toInt()
            day = day - 1
            startTime = context.resources.getString(
                R.string.reset_time,
                year,
                month,
                day,
                resetHour,
                resetMin
            )
            resetDate = dateFormat.parse(startTime)
            resetTimeMillis = resetDate.time
            startTime = context.resources.getString(
                R.string.reset_time,
                year,
                month,
                day,
                resetHour,
                resetMin
            )
            resetDate = dateFormat.parse(startTime)
            resetTimeMillis = resetDate.time
            day = dayFormat.format(date).toInt()
            endTime = context.resources.getString(
                R.string.reset_time,
                year,
                month,
                day,
                resetHour,
                resetMin
            )
            endDate = dateFormat.parse(endTime)
            endTimeMillis = endDate.time
        } else {
            if (session == SESSION_TODAY) {
                year = yearFormat.format(date).toInt()
                month = monthFormat.format(date).toInt()
                day = dayFormat.format(date).toInt()
                startTime = context.resources.getString(
                    R.string.reset_time,
                    year,
                    month,
                    day,
                    resetHour,
                    resetMin
                )
                resetDate = dateFormat.parse(startTime)
                resetTimeMillis = resetDate.time
                day = dayFormat.format(date).toInt() + 1
                endTime = context.resources.getString(
                    R.string.reset_time,
                    year,
                    month,
                    day,
                    resetHour,
                    resetMin
                )
                endDate = dateFormat.parse(endTime)
                endTimeMillis = endDate.time
            }
        }
        return arrayOf(resetTimeMillis, endTimeMillis)
    }

}
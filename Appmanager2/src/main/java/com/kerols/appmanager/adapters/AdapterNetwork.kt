package com.kerols.appmanager.adapters

import android.Manifest
import android.annotation.SuppressLint
import android.app.usage.UsageEvents
import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Typeface
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kerols.appmanager.R
import com.kerols.appmanager.dialogs.NetworkDialog
import com.kerols.appmanager.functions.AppDataUsageModel
import com.kerols.appmanager.functions.NetworkStatsHelper
import com.kerols.appmanager.functions.NetworkStatsHelper.formatData
import com.skydoves.progressview.ProgressView
import kotlinx.coroutines.CoroutineScope


class AdapterNetwork(val context: Context , val clickNetwork: ClickNetwork ,
                     var arrayList: ArrayList<AppDataUsageModel>)
    : RecyclerView.Adapter<AdapterNetwork.ViewModel>(){




    /* class NetworkDiffCallback : DiffUtil.ItemCallback<AppDataUsageModel>() {
        override fun areItemsTheSame(oldItem: AppDataUsageModel, newItem: AppDataUsageModel): Boolean {
            return oldItem == newItem
        }


        override fun areContentsTheSame(oldItem: AppDataUsageModel, newItem: AppDataUsageModel): Boolean {
            return oldItem == newItem
        }
    }*/

    class ViewModel(itemView: View) : RecyclerView.ViewHolder(itemView) {
         val   mAppIcon: ImageView = itemView.findViewById<ImageView>(R.id.app_icon)
         val   mAppName: TextView = itemView.findViewById<TextView>(R.id.app_name)
         val   mDataUsage: TextView = itemView.findViewById<TextView>(R.id.data_usage)
         val   mProgress: ProgressBar = itemView.findViewById<ProgressBar>(R.id.progress)
         val relativeParams  : RecyclerView.LayoutParams = itemView.layoutParams as RecyclerView.LayoutParams

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewModel {
       return ViewModel(LayoutInflater.from(parent.context).inflate(R.layout.app_data_network,parent,false))
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    override fun onBindViewHolder(holder: ViewModel, position: Int) {

        val model = arrayList[position]

        try {
            if (model.packageName == "com.android.tethering") {
                holder.mAppIcon.setImageResource(R.drawable.mobile_hotspot_svgrepo_com)
            } else if (model.packageName == "com.android.deleted") {
                holder.mAppIcon.setImageResource(R.drawable.delete_svgrepo_com)
            } else {
                if (isAppInstalled(context, model.packageName) == true) {
                    Glide.with(context).load(model.packageName?.let { context.packageManager.getApplicationIcon(it) }).into(holder.mAppIcon)
                 //   holder.mAppIcon.setImageDrawable(model.packageName?.let { context.packageManager.getApplicationIcon(it) })
                } else {
                    holder.mAppIcon.setImageResource(R.drawable.delete_svgrepo_com)
                }
            }
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        if (model.progress > 0) {
            holder.mProgress.progress = model.progress
        } else {
            holder.mProgress.progress = 1
        }


        val totalDataUsage: String = formatData(model.sentMobile, model.receivedMobile)[2]

        holder.mAppName.text = model.appName
        holder.mDataUsage.text = totalDataUsage

        holder.itemView.setOnClickListener {
            clickNetwork.onClick(model)
        }

        if (position ==  itemCount - 1) {
            holder.relativeParams.setMargins(0,0,0,250)
        }else { holder.relativeParams.setMargins(0,0,0,0) }


    }


    interface ClickNetwork {
        fun onClick (model: AppDataUsageModel)
    }



    fun isAppInstalled(context: Context, packageName: String?): Boolean? {
        val pm = context.packageManager
        try {
            pm.getPackageInfo(packageName!!, PackageManager.GET_ACTIVITIES)
            return true
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return false
    }

    fun setArray (arrayList: ArrayList<AppDataUsageModel>) {
        this.arrayList = arrayList
        notifyDataSetChanged()
    }


}

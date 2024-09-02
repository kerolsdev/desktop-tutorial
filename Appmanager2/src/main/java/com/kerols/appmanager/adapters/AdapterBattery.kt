package com.kerols.appmanager.adapters


import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kerols.appmanager.R
import com.kerols.phonecleaner.Activitys.App

class AdapterBattery(private var appInfos: ArrayList<App?>, var context: Context?) :
    RecyclerView.Adapter<AdapterBattery.AdapterViewHolder?>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterViewHolder {
        return AdapterViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_app_battery, parent, false)
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: AdapterViewHolder, position: Int) {
        val usageStats = getAppInfos()[position]

        holder.itemView.setOnClickListener(View.OnClickListener {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            intent.data = Uri.parse("package:" + usageStats?.pack)
            context?.startActivity(intent)
        })

        if (position ==  getAppInfos().size - 1) {
            holder.relativeParams.setMargins(0,0,0,250)
        }else { holder.relativeParams.setMargins(0,0,0,0) }

        // Populate the data into the template view using the data object
        usageStats?.let {
            holder. app_name_tv.text = usageStats.appName
            holder. usage_duration_tv.text = usageStats.usageDuration
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                holder. usage_perc_tv.visibility = View.VISIBLE
                holder.progressBar.visibility = View.VISIBLE
                holder. usage_perc_tv.text = "${usageStats.usagePercentage} %"
                holder.progressBar.progress = usageStats.usagePercentage ?: 0
            } else {
                holder. usage_perc_tv.visibility = View.INVISIBLE
                holder.progressBar.visibility = View.INVISIBLE
            }

        }

        context?.let { Glide.with(it).load(usageStats?.appIcon).into(holder.icon_img) };
    }



    inner class AdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        // Lookup view for data population


        val relativeParams  : RecyclerView.LayoutParams
        var app_name_tv: TextView
        var usage_duration_tv: TextView
        var usage_perc_tv: TextView
        var icon_img: ImageView
        var progressBar: ProgressBar

        init {
            relativeParams = itemView.layoutParams as RecyclerView.LayoutParams
            app_name_tv = itemView.findViewById<TextView>(R.id.app_name_tv)
            usage_duration_tv = itemView.findViewById<TextView>(R.id.usage_duration_tv)
            usage_perc_tv = itemView.findViewById<TextView>(R.id.usage_perc_tv)
            icon_img = itemView.findViewById<ImageView>(R.id.icon_img)
            progressBar = itemView.findViewById<ProgressBar>(R.id.progressBar)
        }
    }

    fun getAppInfos(): ArrayList<App?> {
        return appInfos
    }

    fun setAppInfos(appInfos: ArrayList<App?>) {
        this.appInfos = appInfos
        notifyDataSetChanged()
    }

    interface OnClick {
        fun onEventClick(appInfo: App?, check: Boolean)
    }

    override fun getItemCount(): Int {
        return appInfos.size
    }
}
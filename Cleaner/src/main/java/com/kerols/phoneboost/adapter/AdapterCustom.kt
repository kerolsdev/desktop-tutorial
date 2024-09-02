package com.kerols.phoneboost.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kerols.phoneboost.Model.AppInfo
import com.kerols.phoneboost.R
import kotlin.collections.ArrayList

class AdapterCustom(var appInfo : ArrayList<AppInfo> , var context: Context?, var onClick: OnClick) :
    RecyclerView.Adapter<AdapterCustom.AdapterViewHolder?>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterViewHolder {
        return AdapterViewHolder(
            LayoutInflater.from(parent.getContext()).inflate(R.layout.item_app, parent, false)
        )
    }

    override fun onBindViewHolder(holder: AdapterViewHolder, position: Int) {
        val appInfo = getAppInfos()[position]
        holder.app_title.text = appInfo.app_title
        holder.app_size.text = appInfo.app_Size
        context?.let { Glide.with(it).load(appInfo.App_icon).into(holder.app_icon) }
        holder.checkBox.isChecked = appInfo.isChecked
        holder.checkBox.setOnClickListener(View.OnClickListener {
            onClick.onEventClick(appInfo, holder.checkBox.isChecked)
            appInfo.isChecked = holder.checkBox.isChecked
        })

        if (position ==  getAppInfos().size - 1) {
            holder.relativeParams.setMargins(0,0,0,200)
        } else { holder.relativeParams.setMargins(0,0,0,0) }


        holder.itemView.setOnClickListener(View.OnClickListener {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            intent.data = Uri.parse("package:" + appInfo.pack)
            context?.startActivity(intent)
        })
    }



    inner class AdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var  relativeParams = itemView.layoutParams as RecyclerView.LayoutParams
            var  app_icon = itemView.findViewById<ImageView>(R.id.icon_app)
            var  app_size = itemView.findViewById<TextView>(R.id.app_size)
            var  app_title = itemView.findViewById<TextView>(R.id.app_name)
            var  checkBox = itemView.findViewById<CheckBox>(R.id.checkbox)
    }

    fun getAppInfos(): ArrayList<AppInfo> {
        return appInfo ?: ArrayList()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setAppInfos(appInfo: ArrayList<AppInfo>) {
        this.appInfo = appInfo
        notifyDataSetChanged()
    }

    interface OnClick {
        fun onEventClick(appInfo: AppInfo?, check: Boolean)
    }

    override fun getItemCount(): Int {
           return appInfo.size

    }
}
package com.kerolsmm.appmanager.adapters

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kerolsmm.appmanager.R
import com.kerolsmm.appmanager.fragments.UsersAppFragment
import com.kerolsmm.appmanager.functions.AppDataUsageModel
import com.kerolsmm.appmanager.model.AppModel
import java.text.Format
import java.util.Formatter


class AdapterUserApp(var context: Context, private val onClick: AppUserOnClick , var arrayList: ArrayList<AppModel>)
    : RecyclerView.Adapter<AdapterUserApp.ViewModel>() {


  /*  class SleepNightDiffCallback : DiffUtil.ItemCallback<AppModel>() {
        override fun areItemsTheSame(oldItem: AppModel, newItem: AppModel): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: AppModel, newItem: AppModel): Boolean {
            return oldItem.appPackage == newItem.appPackage
        }
    }*/

    class ViewModel(itemView: View) : RecyclerView.ViewHolder(itemView) {




        val textName: TextView = itemView.findViewById<TextView>(R.id.app_name)
        val textSize: TextView = itemView.findViewById<TextView>(R.id.app_size)
        val mImageFile: ImageView = itemView.findViewById<ImageView>(R.id.icon_app)
        val mCheckBox: CheckBox = itemView.findViewById<CheckBox>(R.id.checkbox)
        val mImageMore: ImageView = itemView.findViewById<ImageView>(R.id.imageMore)
        val  relativeParams = itemView.layoutParams as RecyclerView.LayoutParams


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewModel {
        return ViewModel(LayoutInflater.from(parent.context).inflate(R.layout.app_model,parent,false))
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    override fun onBindViewHolder(holder: ViewModel, position: Int) {
        val item  = arrayList[position]
        val appName =     if (item.appTitle == "appName") {
            try {
                item.applicationInfo?.let {
                    context.packageManager?.getApplicationLabel(it).toString()
                }
            } catch (re: RuntimeException) {
                "App Name"
            }
        }else {
            item.appTitle
        }
        try {
            Glide.with(context).load(item.applicationInfo?.let {
                context.packageManager.getApplicationIcon(
                    it
                )
            }).error(R.drawable.no_image).into(holder.mImageFile)
        }catch (re : RuntimeException) {
            holder.mImageFile.setImageResource(R.drawable.no_image)
        }
        holder.textSize.text = android.text.format.Formatter.formatFileSize(context,item.sizeApp)
       /* Glide.with(context).load(item.appIcon).into(holder.mImageFile)
       android.text.format.Formatter.formatFileSize(context,item.sizeApp)*/
        holder.mCheckBox.setOnClickListener {  }
        holder.textName.text = appName ?: "appName"
      /*  holder.mImageMore.setOnClickListener {
            onClick.onClick(item)
        }*/

        holder.itemView.setOnClickListener {
            onClick.onClick(item)
         /*   try {

                val packageName = item.appPackage // Replace with your app's package name
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                intent.data = Uri.parse("package:$packageName")
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                context.startActivity(intent)

            }catch (re : RuntimeException) {
                Log.e("TAG", "onViewCreated: ",re )
            }*/

        }
    }


    fun setArray (arrayList: ArrayList<AppModel>) {
        this.arrayList =  arrayList
        notifyDataSetChanged()
    }

    interface AppUserOnClick {
        fun onClick (appModel: AppModel)
    }


}

/* // Get the storage manager and the storage volumes
        val storageManager = context.getSystemService(Context.STORAGE_SERVICE) as StorageManager
        val storageVolumes = storageManager.storageVolumes

        // Get the package manager and the user handle
        val packageManager = context.packageManager
        val userHandle = Process.myUserHandle()

        // Get the storage stats manager
        val storageStatsManager = context.getSystemService(Context.STORAGE_STATS_SERVICE) as StorageStatsManager

// Loop through the storage volumes
        for (storageVolume in storageVolumes) {
            // Get the UUID of the storage volume
            val uuid = storageVolume.uuid?.let { UUID.fromString(it) } ?: StorageManager.UUID_DEFAULT

            // Loop through the installed packages
            for (packageInfo in packageManager.getInstalledPackages(0)) {
                // Get the package name
                val packageName = packageInfo.packageName

                // Try to get the storage stats for the package
                try {
                    val storageStats = storageStatsManager.queryStatsForPackage(uuid, packageName, userHandle)

                    // Get the cache size in bytes
                    val cacheSize = storageStats.cacheBytes

                    // Print the package name and the cache size
                    Log.d("CacheSize", "$packageName: $cacheSize")
                } catch (e: Exception) {
                    // Handle the exception
                    e.printStackTrace()
                }
            }
        }*/




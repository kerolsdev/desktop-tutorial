package com.kerols.appmanager.adapters

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
import com.kerols.appmanager.R
import com.kerols.appmanager.model.AppModel

class AdapterSystemApp(var context: Context,
                       private val onClick: AppSystemOnClick, var arrayList: ArrayList<AppModel>)
    : RecyclerView.Adapter<AdapterSystemApp.ViewModel>() {



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
        val item  = arrayList.get(position)
        holder.textSize.text = item.appPackage
        /*android.text.format.Formatter.formatFileSize(context,item.sizeApp)*/
        val appName = try {
            item.applicationInfo?.let { context.packageManager?.getApplicationLabel(it).toString() }
        }catch (re: RuntimeException) {
            "App Name"
        }


        try {
            Glide.with(context).load(item.applicationInfo?.let {
                context.packageManager.getApplicationIcon(
                    it
                )
            }).error(R.drawable.no_image).into(holder.mImageFile)
        } catch (re : RuntimeException) {
            holder.mImageFile.setImageResource(R.drawable.no_image)
        }


        holder.mCheckBox.setOnClickListener {  }
        holder.textName.text = appName ?: "appName"
        holder.mImageMore.setOnClickListener {
            onClick.onClick(item)
        }

        holder.itemView.setOnClickListener {
            try {

                val packageName = item.appPackage // Replace with your app's package name
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                intent.data = Uri.parse("package:$packageName")
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                context.startActivity(intent)

            }catch (re : RuntimeException) {
                Log.e("TAG", "onViewCreated: ",re )
            }

        }

    }


    fun setArray (arrayList: ArrayList<AppModel> ) {
        this.arrayList = arrayList
        notifyDataSetChanged()
    }


    interface AppSystemOnClick {
        fun onClick (appModel: AppModel)
    }


}
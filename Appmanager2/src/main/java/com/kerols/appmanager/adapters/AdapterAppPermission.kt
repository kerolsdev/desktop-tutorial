package com.kerols.appmanager.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kerols.appmanager.R
import com.kerols.appmanager.model.AppModel


class AdapterAppPermission(private var arrayList : ArrayList<AppModel>, var context : Context) : RecyclerView.Adapter<AdapterAppPermission.PermissionViewHolder>() {

    class PermissionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val textName: TextView = itemView.findViewById<TextView>(R.id.app_name)
        val mImageMore: ImageView = itemView.findViewById<ImageView>(R.id.icon_app)




    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PermissionViewHolder {
        return PermissionViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.app_model_permission,parent,false))
    }

    override fun getItemCount(): Int {
      return  arrayList.size
    }

    override fun onBindViewHolder(holder: PermissionViewHolder, position: Int) {
        val item  = arrayList[position]

        Glide.with(context).load(item.applicationInfo?.let { context.packageManager.getApplicationIcon(it) }).error(R.drawable.no_image).into(holder.mImageMore)
        holder.textName.text =  try {
            item.applicationInfo?.let { context.packageManager?.getApplicationLabel(it).toString() }
        } catch (re: RuntimeException) {
            "App Name"
        }
        holder.itemView.setOnClickListener {

            if (item.appPackage != context.packageName) {

                if (item.sizeApp.toInt() == 1) {
                    try {
                        val intent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                            Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                        } else {
                            return@setOnClickListener
                        }
                        intent.data = Uri.parse("package:${item.appPackage}")
                        context.startActivity((intent))
                    } catch (e: Exception) {
                        val intent = Intent()
                        intent.action = Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION
                        context.startActivity(intent)
                    }
                } else if (item.sizeApp.toInt() == 2) {
                    try {
                        val intent2 = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
                        intent2.data = Uri.parse("package:${item.appPackage}")
                        context.startActivity(intent2)
                    } catch (re: RuntimeException) {
                        val intent2 = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
                        context.startActivity(intent2)
                    }

                } else if (item.sizeApp.toInt() == 3) {
                    val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
                    context.startActivity(intent)

                } else {
                    try {

                        val packageName = item.appPackage // Replace with your app's package name
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        intent.data = Uri.parse("package:$packageName")
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        context.startActivity(intent)

                    } catch (re: RuntimeException) {
                        Log.e("TAG", "onViewCreated: ", re)
                    }

                }

            }
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    fun setArrayList (arrayList: ArrayList<AppModel>) {
        this.arrayList = arrayList;
        notifyDataSetChanged()
    }


}
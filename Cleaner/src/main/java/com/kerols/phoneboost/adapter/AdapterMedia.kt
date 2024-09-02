package com.kerols.phoneboost.adapter

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.FileUriExposedException
import android.text.format.Formatter
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kerols.phoneboost.Model.FileItem
import com.kerols.phoneboost.R
import com.kerols.phoneboost.Utils.Constes.Companion.ARCHIVES
import com.kerols.phoneboost.Utils.Constes.Companion.AUDIO
import com.kerols.phoneboost.Utils.Constes.Companion.DOCUMENTS
import com.kerols.phoneboost.Utils.Constes.Companion.OTHERS
import java.io.File
import java.io.UnsupportedEncodingException
import java.net.URLEncoder
import java.util.*


class AdapterMedia(var arrayList: ArrayList<FileItem>, var context : Context?,
                   var mOnClickMedia : OnClickMedia, var mimeType : String
) : RecyclerView.Adapter<AdapterMedia.MediaViewModel>() {


    class MediaViewModel(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val textName: TextView = itemView.findViewById<TextView>(R.id.File_name)
        val textSize: TextView = itemView.findViewById<TextView>(R.id.File_size)
        val mImageFile: ImageView = itemView.findViewById<ImageView>(R.id.icon_File)
        val mCheckBox: CheckBox = itemView.findViewById<CheckBox>(R.id.checkboxFile)
        val  relativeParams = itemView.layoutParams as RecyclerView.LayoutParams

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaViewModel {
        return MediaViewModel(LayoutInflater.from(parent.context).inflate(R.layout.file_item,parent,false))
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onBindViewHolder(holder: MediaViewModel, position: Int) {
        val Item = arrayList[position]
        holder.textSize.text = Formatter.formatFileSize(context,Item.size)
        holder.textName.text = Item.Name
        holder.mCheckBox.isChecked = Item.isCheck
        if(mimeType == AUDIO){
            context?.let { Glide.with(it).load(R.drawable.music_1005_svgrepo_com).error(R.drawable.file_1_svgrepo_com).into(holder.mImageFile) }
        }else if(mimeType == OTHERS || mimeType == DOCUMENTS || mimeType== ARCHIVES)
        {
            context?.let { Glide.with(it).load(R.drawable.file_o_svgrepo_com).error(R.drawable.file_1_svgrepo_com).into(holder.mImageFile) }
        }
        else {
            context?.let { Glide.with(it).load(Item.Data).error(R.drawable.file_1_svgrepo_com).into(holder.mImageFile) }
        }

        if (position ==  arrayList.size - 1) {
            holder.relativeParams.setMargins(0,0,0,200)
        } else {
            holder.relativeParams.setMargins(0,0,0,0)
        }

        holder.mCheckBox.setOnClickListener {
            mOnClickMedia.onEventClick(Item,holder.mCheckBox.isChecked)
            Item.isCheck = holder.mCheckBox.isChecked
        }
        holder.mCheckBox.setOnClickListener {
            mOnClickMedia.onEventClick(Item,holder.mCheckBox.isChecked)
            Item.isCheck = holder.mCheckBox.isChecked
        }

        holder.itemView.setOnClickListener {
            if (mimeType == DOCUMENTS  ||
                mimeType == OTHERS ||
                mimeType == ARCHIVES  ){
                openFile2(Item.Data, Item.Name,getMimeType(Item.Name) ?: "*/*")
            }else {
                openFile(Item.Data, Item.Name,mimeType)
            }
        }

    }

    override fun getItemCount(): Int {
        return arrayList.size
    }



    fun setArray (arrayList: ArrayList<FileItem>) {
        this.arrayList = arrayList
        notifyDataSetChanged()
    }


    interface OnClickMedia {
        fun onEventClick( fileItem : FileItem?, check: Boolean)
    }


    @RequiresApi(Build.VERSION_CODES.N)
    private fun openFile(url: String, Name : String , mime : String) {
        try {
            val intent = Intent()
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            val dirUri: Uri = Uri.parse(url.toString())
            val uri: Uri = Uri.parse("file://" + dirUri.path)
            intent.action = Intent.ACTION_VIEW
            intent.setDataAndType(uri, "$mime/*")
            try {
                context?.startActivity(Intent.createChooser(intent, Name))
            } catch (a: ActivityNotFoundException) {
                Toast.makeText(
                    context,
                    "You don't have apps to run",
                    Toast.LENGTH_SHORT
                ).show()
            }
        } catch (fileUriExposedException: FileUriExposedException) {
            val apkURI = context?.let {
                FileProvider.getUriForFile(
                    it,
                    context?.packageName +".provider", File(url))
            }
            val install = Intent(Intent.ACTION_VIEW)
            install.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            install.setDataAndType(apkURI, "$mime/*")
            install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            context?.startActivity(Intent.createChooser(install, Name))
            Log.e("TAG", "Position: $apkURI")
        }
    }


    @RequiresApi(Build.VERSION_CODES.N)
    private fun openFile2(url: String, Name : String , mime : String) {
        try {
            val intent = Intent()
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            val dirUri: Uri = Uri.parse(url.toString())
            val uri: Uri = Uri.parse("file://" + dirUri.path)
            intent.action = Intent.ACTION_VIEW
            intent.setDataAndType(uri, mime)
            try {
                context?.startActivity(Intent.createChooser(intent, Name))
            } catch (a: ActivityNotFoundException) {
                Toast.makeText(
                    context,
                    "You don't have apps to run",
                    Toast.LENGTH_SHORT
                ).show()
            }
        } catch (fileUriExposedException: FileUriExposedException) {
            val apkURI = context?.let {
                FileProvider.getUriForFile(
                    it,
                    context?.packageName +".provider", File(url))
            }
            val install = Intent(Intent.ACTION_VIEW)
            install.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            install.setDataAndType(apkURI, mime)
            install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            context?.startActivity(Intent.createChooser(install, Name))
            Log.e("TAG", "Position: $apkURI")
        }
    }


    private fun getMimeType(url: String?): String? {
        var type: String? = null
        val extension = getExtension(url)
        type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
        return type
    }

    private fun getExtension(fileName: String?): String {
        val encoded: String? = try {
            URLEncoder.encode(fileName, "UTF-8").replace("+", "%20")
        } catch (e: UnsupportedEncodingException) {
            null
        }
        return MimeTypeMap.getFileExtensionFromUrl(encoded).lowercase(Locale.getDefault())
    }
}
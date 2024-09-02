package com.kerols.phoneboost.Fragments

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.Html
import android.util.Log
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.core.text.HtmlCompat
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.kerols.phoneboost.Activitys.MainActivity.Companion.runUi
import com.kerols.phoneboost.Model.FileItem
import com.kerols.phoneboost.R
import com.kerols.phoneboost.Utils.Constes
import com.kerols.phoneboost.Utils.Constes.Companion.ARCHIVES
import com.kerols.phoneboost.Utils.Constes.Companion.AUDIO
import com.kerols.phoneboost.Utils.Constes.Companion.DOCUMENTS
import com.kerols.phoneboost.Utils.Constes.Companion.IMAGES
import com.kerols.phoneboost.Utils.Constes.Companion.VIDEOS
import com.kerols.phoneboost.adapter.AdapterMedia
import com.kerols.phoneboost.databinding.FragmentMediaChooseBinding
import java.io.File
import java.util.*


class MediaChooseFragment : Fragment(), AdapterMedia.OnClickMedia, View.OnClickListener {

    private var arrayList : ArrayList<FileItem> = ArrayList()
    private lateinit var binding : FragmentMediaChooseBinding
    var  adapterMedia : AdapterMedia? = null
    private var mTotlaSize : Long = 0
    private var arrayData : ArrayList<String> = ArrayList()
    var str : String = "last"
    private var isRun : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMediaChooseBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args = arguments?.let { MediaChooseFragmentArgs.fromBundle(it) }
        mQueryMedia(args?.mimetype)
        binding.RemoveFiles.setOnClickListener(this)
        val menuHost = activity
        menuHost?.addMenuProvider(object : MenuProvider, com.kerols.phoneboost.Fragments.BottomDialogCustom.OnClickRadio {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                // Add menu items here
                menuInflater.inflate(R.menu.menufile, menu)
                val myActionMenuItem = menu.findItem(R.id.action_search)
                val searchView = myActionMenuItem.actionView as SearchView?
                searchView?.queryHint = getString(R.string.search);
                searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String): Boolean {
                        return false
                    }

                    override fun onQueryTextChange(s: String?): Boolean {
                        s?.let { mSearchfilter(it) }
                        return true
                    }
                })
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                if (menuItem.itemId == R.id.action_filter) {
                    val bottom = com.kerols.phoneboost.Fragments.BottomDialogCustom(this, str)
                    activity?.let { bottom.show(it.supportFragmentManager,bottom.tag) }
                }
                return true
            }

            override fun onEventClick(string: String , it : com.kerols.phoneboost.Fragments.BottomDialogCustom) {
                filter(string)
                str = string
                it.dismiss()

            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

        binding.checkboxall.setOnCheckedChangeListener { buttonView, isChecked ->

            if (isChecked){
                for (it in arrayList){
                    it.isCheck = isChecked
                    arrayData.add(it.Data)
                    mTotlaSize += it?.size ?: 0
                }
                binding.RemoveFiles.visibility = View.VISIBLE
                binding.RemoveFiles.text = "${getString(R.string.Remove)} (${arrayData.size})"
            }else {
                for (it in arrayList){
                    it.isCheck = isChecked
                }
                mTotlaSize = 0
                arrayData.clear()
                binding.RemoveFiles.visibility = View.GONE
            }
            adapterMedia?.setArray(arrayList)

        }

    }

    @SuppressLint("Recycle")
    fun mQueryMedia (mimetype2 : String?) {
        Thread {
                Thread.sleep(500)
                arrayList  = ArrayList()
                isRun = true
                val uri = MediaStore.Files.getContentUri("external")
                val sort = MediaStore.Files.FileColumns._ID + " DESC";
                val projection = arrayOf(
                    MediaStore.Files.FileColumns.MIME_TYPE,
                    MediaStore.Files.FileColumns.DATA,
                    MediaStore.Files.FileColumns.DISPLAY_NAME,
                    MediaStore.Files.FileColumns.SIZE,
                    MediaStore.Files.FileColumns.DATE_MODIFIED
                )

                val cursor = activity?.contentResolver?.query(uri , projection ,null ,null , sort)
                if (cursor?.moveToFirst() == true)
                {
                    try {
                     do {
                        val fullMimetype = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.MIME_TYPE))?.lowercase(Locale.getDefault())
                        val mTime =  cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATE_MODIFIED))
                        val mSize =  cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.SIZE))
                        val mName =  cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DISPLAY_NAME))
                        val mDATA =  cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA))
                        val mimetype = fullMimetype?.substringBefore("/")

                        when (mimetype2) {
                            IMAGES -> {
                                if (mimetype == "image") {
                                    arrayList.add(FileItem(mSize,mTime,mDATA,mName,false))
                                }
                            }
                            VIDEOS -> {
                                if (mimetype == "video") {
                                    arrayList.add(FileItem(mSize,mTime,mDATA,mName,false))
                                }
                            }
                            AUDIO -> {
                                if (mimetype == "audio" || Constes().extraAudioMimeTypes.contains(fullMimetype)) {
                                    arrayList.add(FileItem(mSize,mTime,mDATA,mName,false))
                                }
                            }
                            DOCUMENTS -> {
                                if (mimetype == "text" || Constes().extraDocumentMimeTypes.contains(fullMimetype)) {
                                    try {
                                        arrayList.add(FileItem(mSize,mTime,mDATA,mName,false))
                                    }catch (re : RuntimeException) {
                                        arrayList.add(FileItem(mSize,mTime,mDATA,File(mDATA).name,false))
                                    }


                                }
                            }
                            ARCHIVES -> {
                                if (Constes().archiveMimeTypes.contains(fullMimetype)) {
                                    arrayList.add(FileItem(mSize,mTime,mDATA,mName,false))
                                }
                            }

                        }

                    } while (cursor.moveToNext() && isRun)

                    }catch (re : RuntimeException) {
                        Log.e("TAG", "mQueryMedia: ", re )
                    }
                }

                runUi.post {
                   if (isRun) {
                       binding.animationView.visibility = View.GONE
                       if (arrayList.size > 0) {
                           adapterMedia = AdapterMedia(arrayList, activity, this, mimetype2 ?: "*");
                           binding.Files.adapter = adapterMedia
                           binding.viewLay.visibility = View.VISIBLE
                           binding.viewLayEnd.visibility = View.VISIBLE
                       }
                   }
                }

        }.start()

    }

    override fun onEventClick(fileItem : FileItem?, check: Boolean) {
        if (check) {
            fileItem?.Data?.let { arrayData.add(it) }
            mTotlaSize += fileItem?.size ?: 0
        } else {
            fileItem?.Data?.let { arrayData.remove(it) }
            mTotlaSize -= fileItem?.size ?: 0
        }
        if (arrayData.isEmpty()) {
            binding.RemoveFiles.visibility = View.GONE
        } else {
            binding.RemoveFiles.visibility = View.VISIBLE
        }
        binding.RemoveFiles.text = "${getString(R.string.Remove)} (${arrayData.size})"

    }

    override fun onClick(v: View?) {
        val yesText = "<font color='#2e6fff'>DELETE</font>"
        val cancelText = "<font color='#2e6fff'>CANCEL</font>"
        val DELETE = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(yesText, HtmlCompat.FROM_HTML_MODE_LEGACY)
        } else {
            Html.fromHtml(yesText)
        }
        val CANCEL = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(cancelText, HtmlCompat.FROM_HTML_MODE_LEGACY)
        } else {
            Html.fromHtml(cancelText)
        }

        activity?.let {
            MaterialAlertDialogBuilder(it)
                .setTitle(getString(R.string.Delete_Selected))
                .setMessage(getString(R.string.Delete_Selected_message))
                .setPositiveButton(
                    DELETE
                ) { dialogInterface, i ->
                    try {

                    var mDataArray: Array<String> = arrayOf()
                    mDataArray = arrayData.toArray(mDataArray)
                    findNavController().navigate(MediaChooseFragmentDirections.actionMediaChooseFragmentToMediaProcessFragment(mTotlaSize,mDataArray))

                    }catch (re : RuntimeException) {
                        Log.e("TAG", "onClick: ", re )
                    }

                }
                .setNegativeButton(
                    CANCEL
                ) { dialogInterface, i ->
                    dialogInterface.dismiss()
                }
                .show()
        }

    }


    fun filter (filterType : String) {

            when (filterType) {
                "last" -> {
                    arrayList.sortWith(Comparator { o1, o2 -> o2?.Time?.compareTo(o1?.Time!!)!! })
                }
                "small" -> {
                    arrayList.sortWith(Comparator { o1, o2 -> o1?.size?.compareTo(o2?.size!!)!! })

                }
                else -> {
                    arrayList.sortWith(Comparator { o1, o2 -> o2?.size?.compareTo(o1?.size!!)!! })
                }
            }
                   adapterMedia?.setArray(arrayList)

    }

    private fun mSearchfilter(text: String) {

            val filteredList: ArrayList<FileItem> = ArrayList<FileItem>()
            for (item in arrayList) {
                if (item.Name.lowercase().contains(text.lowercase())) {
                    filteredList.add(item)
                }
            }
                adapterMedia?.setArray(filteredList)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        isRun = false
    }
}
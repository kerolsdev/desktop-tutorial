package com.kerols.phoneboost.Fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kerols.phoneboost.R
import com.kerols.phoneboost.Utils.Constes
import com.kerols.phoneboost.Utils.Constes.Companion.ARCHIVES
import com.kerols.phoneboost.Utils.Constes.Companion.AUDIO
import com.kerols.phoneboost.Utils.Constes.Companion.DOCUMENTS
import com.kerols.phoneboost.Utils.Constes.Companion.IMAGES
import com.kerols.phoneboost.Utils.Constes.Companion.VIDEOS
import com.kerols.phoneboost.databinding.FragmentMediaHomeBinding
import java.io.File
import java.util.*
import kotlin.collections.HashMap
import android.text.format.Formatter
import android.util.Log
import androidx.navigation.fragment.findNavController
import com.kerols.phoneboost.Utils.Utils


class MediaHomeFragment : Fragment(), View.OnClickListener {

    private val utils : Utils? by lazy {
        context?.let { Utils(it) }
    }

    private lateinit var mediaHomeFragmentBinding : FragmentMediaHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mediaHomeFragmentBinding = FragmentMediaHomeBinding.inflate(inflater,container,false)
        return mediaHomeFragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Thread {

            val filesSize = getSizesByMimeType()
            val imagesSize = filesSize[IMAGES] ?: 0
            val videosSize = filesSize[VIDEOS] ?: 0
            val audioSize = filesSize[AUDIO] ?: 0
            val documentsSize = filesSize[DOCUMENTS] ?: 0
            val archivesSize = filesSize[ARCHIVES] ?: 0


            Handler(Looper.getMainLooper()).post {

                mediaHomeFragmentBinding.animationView.visibility = View.GONE
                mediaHomeFragmentBinding.sizeImages.text = Formatter.formatFileSize( requireActivity(),imagesSize)
                mediaHomeFragmentBinding.sizeAudio.text = Formatter.formatFileSize( requireActivity(),audioSize)
                mediaHomeFragmentBinding.sizeVideo.text = Formatter.formatFileSize( requireActivity(),videosSize)
                mediaHomeFragmentBinding.sizedocuments.text = Formatter.formatFileSize( requireActivity(),documentsSize)
                mediaHomeFragmentBinding.sizearchives.text = Formatter.formatFileSize( requireActivity(),archivesSize)

                mediaHomeFragmentBinding.MediaLayout.visibility = View.VISIBLE

                mediaHomeFragmentBinding.Video.setOnClickListener(this)
                mediaHomeFragmentBinding.Audio.setOnClickListener(this)
                mediaHomeFragmentBinding.Image.setOnClickListener(this)
                mediaHomeFragmentBinding.documents.setOnClickListener(this)
                mediaHomeFragmentBinding.archives.setOnClickListener(this)


                utils?.saveMedia(true)
            }



        }.start()



    }

    private fun getSizesByMimeType() : HashMap<String, Long> {
        val uri = MediaStore.Files.getContentUri("external")
        val projection = arrayOf(
            MediaStore.Files.FileColumns.SIZE,
            MediaStore.Files.FileColumns.MIME_TYPE,
            MediaStore.Files.FileColumns.DATA
        )

        var imagesSize = 0L
        var videosSize = 0L
        var audioSize = 0L
        var documentsSize = 0L
        var archivesSize = 0L
        var othersSize = 0L


           val cursor = activity?.contentResolver?.query(uri, projection,null,null,null)
           if (cursor?.moveToFirst() == true){
               do {
                   try {
                       val mimeType = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.MIME_TYPE))?.lowercase(
                           Locale.getDefault())
                       val size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.SIZE))
                       if (mimeType == null) {
                           if (size > 0 && size != 4096L) {
                               val path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA))
                               if (getIsPathDirectory(path)) {
                                   othersSize += size
                               }
                           }

                       }

                       when (mimeType?.substringBefore("/")) {
                           "image" -> imagesSize += size
                           "video" -> videosSize += size
                           "audio" -> audioSize += size
                           "text" -> documentsSize += size
                           else -> {
                               when {
                                   Constes().extraDocumentMimeTypes.contains(mimeType) -> documentsSize += size
                                   Constes().extraAudioMimeTypes.contains(mimeType) -> audioSize += size
                                   Constes().archiveMimeTypes.contains(mimeType) -> archivesSize += size
                                   else -> othersSize += size
                               }
                           }
                       }
                   } catch (e: Exception) {
                       Log.e("TAG", "getSizesByMimeType:" , e)
                   }

               }while (cursor.moveToNext())
           }


        val mimeTypeSizes = HashMap<String, Long>().apply {
            put(IMAGES, imagesSize)
            put(VIDEOS, videosSize)
            put(AUDIO, audioSize)
            put(DOCUMENTS, documentsSize)
            put(ARCHIVES, archivesSize)
        }

        return mimeTypeSizes
    }

    private fun getIsPathDirectory(path: String?): Boolean {
        return path?.let { File(it).exists() } == true
    }

    override fun onClick(v: View?) {
        if (v?.id == R.id.Video) {
            launchMimetypeActivity(VIDEOS)
        }else if (v?.id == R.id.Audio)
        {
            launchMimetypeActivity(AUDIO)
        }
        else if (v?.id == R.id.documents)
        {
            launchMimetypeActivity(DOCUMENTS)
        }

        else if (v?.id == R.id.archives)
        {
            launchMimetypeActivity(ARCHIVES)
        }
        else
        {
            launchMimetypeActivity(IMAGES)
        }
    }
    private fun launchMimetypeActivity(mimetype: String) {
        try {
            findNavController().navigate(MediaHomeFragmentDirections.actionMediaHomeFragmentToMediaChooseFragment(mimetype))
        }catch (re : RuntimeException) {
            Log.e("TAG", "launchMimetypeActivity: ",  re)
        }
    }

}
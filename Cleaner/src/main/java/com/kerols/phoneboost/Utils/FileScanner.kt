
package com.kerols.phoneboost.Utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.content.res.Resources
import android.media.MediaScannerConnection
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.kerols.phoneboost.Fragments.CleanerProcess.Companion.isRunningCleaner
import com.kerols.phoneboost.R

import java.io.File
import java.util.*
import kotlin.collections.ArrayList

class FileScanner(private val path: File, context: Context?) {
    private lateinit var context: Context
    private var res: Resources? = null
    private var filesRemoved = 0
    private var kilobytesTotal: Long = 0
    private var delete = false
    private var emptyDir = false
    private var autoWhite = true
    private var corpse = false
    private var cacheApps : Boolean = false
    private var thumbailes : Boolean = false
    private val listFiles: List<File>
        get() = getListFiles(path)
    private val mRunMain by lazy { Handler(Looper.getMainLooper()) }

    /**
     * Used to generate a list of all files on device
     * @param parentDirectory where to start searching from
     * @return List of all files on device (besides whitelisted ones)
     */
    private fun getListFiles(parentDirectory: File): List<File> {
        val inFiles = ArrayList<File>()
        val files = parentDirectory.listFiles()
        if (files != null) {
            for (file in files) {
                if (file != null) { // hopefully to fix crashes on a very limited number of devices.
                        if (file.isDirectory) { // folder
                            inFiles.add(file) // add folder itself
                            inFiles.addAll(getListFiles(file)) // add contents to returned list
                        } else { inFiles.add(file) } // add file
                    }
            }
        }
        return inFiles
    }





    /**
     * Runs as for each loop through the filter, and checks if the file matches any filters
     * @param file file to check
     * @return true if the file's extension is in the filter, false otherwise
     */
    fun filter(file: File?): Boolean {
        if (file != null) {
            try {
                // corpse checking - TODO: needs improved!
                when {
                    corpse &&
                            file.parentFile != null &&
                            file.parentFile.parentFile != null &&
                            file.parentFile.name == "data" &&
                            file.parentFile.parentFile.name == "Android" &&
                            file.name != ".nomedia" &&
                            !installedPackages.contains(file.name) -> return true
                }
                // empty folder
                if (file.isDirectory && isDirectoryEmpty(file) && emptyDir) return true

                // file
                val filterIterator = filters.iterator()
                while (filterIterator.hasNext()) {
                    val filter = filterIterator.next()
                    if (file.absolutePath.lowercase(Locale.getDefault()).matches(filter.lowercase(Locale.getDefault()).toRegex()))
                        return true
                }
            } catch (e: NullPointerException) {
                return false
            }
        }
        return false // not empty folder or file in filter
    }

    private val installedPackages: List<String>
        get() {
            val pm = context.packageManager
            val packages = pm.getInstalledApplications(PackageManager.GET_META_DATA)
            val packagesString: MutableList<String> = ArrayList()
            for (packageInfo in packages) {
                packagesString.add(packageInfo.packageName)
            }
            return packagesString
        }

    /**
     * lists the contents of the file to an array, if the array length is 0, then return true, else
     * false
     * @param directory directory to test
     * @return true if empty, false if containing a file(s)
     */
    private fun isDirectoryEmpty(directory: File): Boolean {
        return if (directory.list() != null && directory.list() != null) directory.list().isEmpty()
        else false
    }

    /**
     * Adds paths to the white list that are not to be cleaned. As well as adds extensions to filter.
     * 'generic', 'aggressive', and 'apk' should be assigned by calling preferences.getBoolean()
     */
    @SuppressLint("ResourceType")
    fun setUpFilters(generic: Boolean, aggressive: Boolean, apk: Boolean): FileScanner {
        val folders: MutableList<String> = ArrayList()
        val files: MutableList<String> = ArrayList()
        setResources(context.resources)
        if (generic) {
            folders.addAll(listOf(*res!!.getStringArray(R.array.generic_filter_folders)))
            files.addAll(listOf(*res!!.getStringArray(R.array.generic_filter_files)))
        }
        if (aggressive) {
            folders.addAll(listOf(*res!!.getStringArray(R.array.aggressive_filter_folders)))
            files.addAll(listOf(*res!!.getStringArray(R.array.aggressive_filter_files)))
        }

        // filters
        filters.clear()
        for (folder in folders) filters.add(getRegexForFolder(folder))
        for (file in files) filters.add(getRegexForFile(file))

        // apk
        if (apk) { filters.add(getRegexForFile(".apk")) }
        if (thumbailes){
            filters.add(getRegexForFolder(".thumbnails"))
            filters.add(getRegexForFolder(".Thumbnails"))
        }
        return this
    }

    @SuppressLint("SuspiciousIndentation")
    fun startScan(scanProgress : CircularProgressIndicator): Long {
        val foundFiles: ArrayList<File> = ArrayList<File>(listFiles)
        var mAppsCachedCleaners : ArrayList<ApplicationInfo> = ArrayList()

            if(getCacheApps()){

                mAppsCachedCleaners =  getListOfAppsInfo(context, ALL_APPS)
                scanProgress.max =  foundFiles.size + mAppsCachedCleaners.size

                } else {
                 scanProgress.max =  foundFiles.size

            }

            // scan & delete
            for (file in foundFiles) {
                if (isRunningCleaner) {
                    if (filter(file)) { // filter
                        kilobytesTotal += file.length()
                        if (delete) {
                            ++filesRemoved
                              file.delete()
                                MediaScannerConnection.scanFile(context, arrayOf(file.path),
                                    null) { path, uri ->
                                }
                        }
                    }
                    mRunMain.post {
                        scanProgress.progress = scanProgress.progress + 1
                    }
                } else break
            }

            if (getCacheApps()) {
                for (app in mAppsCachedCleaners) {
                    try {
                    if (isRunningCleaner) {
                        deleteCache(context, app.packageName)
                        mRunMain.post {  try { scanProgress.progress = scanProgress.progress + 1   } catch (re : RuntimeException) { } }
                    }else break

                    } catch (ex: RuntimeException) {
                        Log.e("TAG", "startScan: ",ex ) }
                }
            }


        return kilobytesTotal
    }

    private fun getRegexForFolder(folder: String): String {
        return ".*(\\\\|/)$folder(\\\\|/|$).*"
    }

    private fun getRegexForFile(file: String): String {
        return ".+" + file.replace(".", "\\.") + "$"
    }


    fun setResources(res: Resources?): FileScanner {
        this.res = res
        return this
    }

    fun setEmptyDir(emptyDir: Boolean): FileScanner {
        this.emptyDir = emptyDir
        return this
    }

    fun setDelete(delete: Boolean): FileScanner {
        this.delete = delete
        return this
    }

    fun setCorpse(corpse: Boolean): FileScanner {
        this.corpse = corpse
        return this
    }

    fun setContext(context: Context): FileScanner {
        this.context = context
        return this
    }

    fun setCacheApps (value : Boolean) : FileScanner {
        cacheApps = value
        return this
    }

    private fun getCacheApps () : Boolean  {
        return cacheApps
    }

    fun setThumbailesValue (value: Boolean) : FileScanner {
        thumbailes = value
        return this
    }

    companion object {
        // TODO remove local prefs objects, create setter for one instead
        private val filters = ArrayList<String>()
        private val protectedFileList =
            arrayOf(
                "backup",
                "copy",
                "copies",
                "important",
                "do_not_edit"
            ) // TODO: move to resources for translations

        const val ALL_APPS : Int = 0
        const val USER_APPS : Int = 1
        const val SYSTEM_APPS : Int = 2
    }

    private fun getListOfAppsInfo(activity: Context , isAll: Int): ArrayList <ApplicationInfo> {
        val appForReturnedList = ArrayList<ApplicationInfo>()
        val appsInfoList: MutableList<ApplicationInfo> = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            activity.packageManager.getInstalledApplications(PackageManager.GET_META_DATA)
        } else {
            val intent = Intent(Intent.ACTION_MAIN, null)
            intent.addCategory(Intent.CATEGORY_LAUNCHER)
            val arrayAppsNew = mutableListOf<ApplicationInfo>()
            val listAppsReturned = activity?.packageManager?.queryIntentActivities(intent, 0)
            listAppsReturned?.forEach {
                arrayAppsNew.add(it.activityInfo.applicationInfo)
            }
            arrayAppsNew
        }
        val appsInstalled: MutableList<ApplicationInfo> = mutableListOf()
        val appsSystem: MutableList<ApplicationInfo> = mutableListOf()
        (appsInfoList.indices).forEach { i ->
          if (appsInfoList[i].packageName != activity.packageName) {
            if (appsInfoList[i].flags and ApplicationInfo.FLAG_SYSTEM == 1) {
                appsSystem.add(appsInfoList[i])
            } else {
                appsInstalled.add(appsInfoList[i])
            }
           }
        }

        when (isAll) {
            ALL_APPS -> {
                appForReturnedList.addAll(appsInstalled)
                appForReturnedList.addAll(appsSystem)
            }
            USER_APPS -> appForReturnedList.addAll(appsInstalled)
            SYSTEM_APPS -> appForReturnedList.addAll(appsSystem)
            else -> appForReturnedList.addAll(appsInstalled)
        }

        return appForReturnedList
    }


    private fun deleteCache(context: Context, packagePathByPackageName: String) {
            val dir = File(getPackagePathByPackageName(context , packagePathByPackageName))
            deleteDir(dir)
    }

    private fun getPackagePathByPackageName(activity: Context?, packageName: String): String {
        val packageManager = activity?.packageManager
        var packagePath = packageName
        val packageInfo = packageManager?.getPackageInfo(packagePath, 0)
        var appCacheDir = ""
        packagePath = packageInfo?.applicationInfo?.dataDir ?:  " "
        val dir = File("$packagePath/cache")
        if (dir.exists()) {
            appCacheDir = dir.absolutePath
        }
        return appCacheDir
    }


    private fun deleteDir(dir: File?): Boolean {
        return if (dir != null && dir.isDirectory) {
            dir.deleteRecursively()
            val children: Array<String> = dir.list() as Array<String>
            for (i in children.indices) {
                val success = deleteDir(File(dir, children[i]))
                if (!success) {
                    return false
                }
            }
            dir.deleteRecursively()
        } else if (dir != null && dir.isFile) {
            dir.deleteRecursively()
        } else {
            false
        }
    }



}

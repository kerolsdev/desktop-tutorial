package com.kerolsmm.appmanager.activities

import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import com.kerolsmm.appmanager.adapters.AdapterNetwork
import com.kerolsmm.appmanager.databinding.ActivitySystemAppsUsageBinding
import com.kerolsmm.appmanager.dialogs.NetworkDialog
import com.kerolsmm.appmanager.functions.AppDataUsageModel
import com.kerolsmm.appmanager.mvvm.MvvmBottomApp


class SystemAppsUsageActivity : AppCompatActivity(), AdapterNetwork.ClickNetwork {

    private  var arraylist : ArrayList<AppDataUsageModel> = ArrayList()
    private lateinit var adapterNetwork: AdapterNetwork
    private lateinit var binding: ActivitySystemAppsUsageBinding
    private val mvvmBottomApp : MvvmBottomApp by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySystemAppsUsageBinding.inflate(layoutInflater)
        setContentView(binding.root)


        adapterNetwork = AdapterNetwork(this,this,ArrayList<AppDataUsageModel>())
        binding.listNetworks.adapter = adapterNetwork


        setSupportActionBar(binding.toolbarSetting)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        Thread(Runnable {
            if (intent != null) {
                arraylist = intent.getSerializableExtra("system") as ArrayList<AppDataUsageModel>
            }
            runOnUiThread(Runnable {
                adapterNetwork.setArray(arraylist)
            })
        }).start()





    }

    override fun onClick(model: AppDataUsageModel) {

            val networkDialog = NetworkDialog()
            networkDialog.show(supportFragmentManager,networkDialog.tag)
            mvvmBottomApp.setValueAppModelNetwork(model)

    }


    override fun onOptionsItemSelected(@NonNull item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
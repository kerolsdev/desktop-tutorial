package com.kerols.appmanager.activities

import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import com.kerols.appmanager.adapters.AdapterNetwork
import com.kerols.appmanager.databinding.ActivitySystemAppsUsageBinding
import com.kerols.appmanager.dialogs.NetworkDialog
import com.kerols.appmanager.functions.AppDataUsageModel


class SystemAppsUsageActivity : AppCompatActivity(), AdapterNetwork.ClickNetwork {

    private  var arraylist : ArrayList<AppDataUsageModel> = ArrayList()
    private lateinit var adapterNetwork: AdapterNetwork
    private lateinit var binding: ActivitySystemAppsUsageBinding

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

            val networkDialog = NetworkDialog(model)
            networkDialog.show(supportFragmentManager,networkDialog.tag)
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
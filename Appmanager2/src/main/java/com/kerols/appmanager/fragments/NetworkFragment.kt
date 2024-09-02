package com.kerols.appmanager.fragments

import android.Manifest
import android.app.usage.UsageEvents
import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.core.view.MenuProvider
import com.kerols.appmanager.R
import com.kerols.appmanager.activities.SystemAppsUsageActivity
import com.kerols.appmanager.adapters.AdapterNetwork
import com.kerols.appmanager.databinding.FragmentNetworkBinding
import com.kerols.appmanager.dialogs.BottomDialogFilter
import com.kerols.appmanager.dialogs.NetworkDialog
import com.kerols.appmanager.functions.AppDataUsageModel
import com.kerols.appmanager.functions.NetworkStatsHelper
import com.kerols.appmanager.functions.NetworkStatsHelper.formatData
import com.kerols.appmanager.functions.QueryAppNetwork
import com.kerols.appmanager.functions.Values
import com.kerols.appmanager.model.AppModel
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import java.util.Locale


public  class NetworkFragment : Fragment(), Observer<HashMap<String,ArrayList<AppDataUsageModel>>>,
    AdapterNetwork.ClickNetwork, BottomDialogFilter.ClickNetworkFilter {


    private val handler : Handler = Handler(Looper.getMainLooper())
    private lateinit var binding : FragmentNetworkBinding
    private lateinit var adapterNetwork: AdapterNetwork


    private var systemApps: ArrayList<AppDataUsageModel> = ArrayList()
    private var userApps: ArrayList<AppDataUsageModel> = ArrayList()

    var session : Int = Values.SESSION_TODAY
    var type : Int = Values.TYPE_WIFI

    private val queryAppNetwork = QueryAppNetwork()

    private var isComplete : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNetworkBinding.inflate(inflater,container,false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        adapterNetwork = AdapterNetwork(requireContext(),this,
            ArrayList<AppDataUsageModel>())
        binding.listNetworks.adapter = adapterNetwork


        binding.loadingApp.visibility = View.VISIBLE
        binding.ViewMain.visibility = View.GONE
        binding.Filter.visibility = View.GONE
        binding.viewEmpty.visibility = View.GONE
        binding.swipeRefresh.isEnabled = false

        queryAppNetwork.appNetWork(this,requireActivity(),session,type)


        binding.swipeRefresh.setOnRefreshListener {
            try {

            binding.swipeRefresh.isRefreshing = false;
            binding.swipeRefresh.isEnabled = false
            binding.loadingApp.visibility = View.VISIBLE
            binding.ViewMain.visibility = View.GONE
            binding.Filter.visibility = View.GONE
            binding.viewEmpty.visibility = View.GONE
            queryAppNetwork.appNetWork(this,requireActivity(),session, type)

            }catch (re : RuntimeException) {

            }
        }



        binding.Filter.setOnClickListener {
            if (isComplete) {
                val filterDialog = BottomDialogFilter(this,session, type)
                filterDialog.show(requireActivity().supportFragmentManager,filterDialog.tag)
            }
        }



        val menuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                // Add menu items here
                menuInflater.inflate(R.menu.search, menu)
                val myActionMenuItem = menu.findItem(R.id.action_search)
                val searchView = myActionMenuItem.actionView as SearchView?

                searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String): Boolean {
                        return false
                    }

                    override fun onQueryTextChange(s: String?): Boolean {
                        if (isComplete) { s?.let { filter(it) } }
                        return true
                    }
                })
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                // Handle the menu selection
                return true
            }
        }, viewLifecycleOwner, androidx.lifecycle.Lifecycle.State.RESUMED)


    }

    override fun onSubscribe(d: Disposable) {

    }

    override fun onError(e: Throwable) {

    }

    override fun onComplete() {
        isComplete = true
        binding.swipeRefresh.isEnabled = true
        binding.loadingApp.visibility = View.GONE
        binding.Filter.visibility = View.VISIBLE
        if (userApps.isEmpty()){
            binding.viewEmpty.visibility = View.VISIBLE
            binding.ViewMain.visibility = View.GONE
        }else {
            binding.ViewMain.visibility = View.VISIBLE
            binding.viewEmpty.visibility = View.GONE
        }

    }

    override fun onNext(t: HashMap<String,ArrayList<AppDataUsageModel>>) {
        systemApps =   t["system"] ?: ArrayList()
        userApps  =    t["user"]   ?: ArrayList()
        adapterNetwork.setArray(userApps)
    }

    override fun onClick(model: AppDataUsageModel) {
        if (model.packageName == getString(R.string.package_system)) {
            val intent = Intent(requireActivity(),SystemAppsUsageActivity::class.java)
            intent.putExtra("system",systemApps)
            requireActivity().startActivity(intent)
        } else {
            val networkDialog = NetworkDialog(model)
            networkDialog.show(requireActivity().supportFragmentManager,networkDialog.tag)

        }
    }

    override fun click(session: Int, type: Int) {
        this.session = session
        this.type = type
        binding.loadingApp.visibility = View.VISIBLE
        binding.ViewMain.visibility = View.GONE
        binding.Filter.visibility = View.GONE
        binding.viewEmpty.visibility = View.GONE
        binding.swipeRefresh.isEnabled = false
        queryAppNetwork.appNetWork(this,requireActivity(),session, type)

    }

    private fun filter(text: String) {
        val filteredList: ArrayList<AppDataUsageModel> = ArrayList<AppDataUsageModel>()
        for (item in userApps) {
            if (item.appName?.lowercase(Locale.ROOT)?.contains(text.lowercase(Locale.getDefault())) == true) {
                filteredList.add(item)
            }
        }
        adapterNetwork.setArray(filteredList)
    }

}
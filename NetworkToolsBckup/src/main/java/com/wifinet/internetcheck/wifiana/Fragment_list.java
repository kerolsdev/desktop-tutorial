package com.wifinet.internetcheck.wifiana;


import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.wifinet.internetcheck.BottomAna;
import com.wifinet.internetcheck.R;

import java.util.ArrayList;
import java.util.List;


@TargetApi(29)
public class Fragment_list
        extends Fragment
        implements AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener, ArrayAdapterWLAN.onClickAdapter {

    private RecyclerView lv;
    private ArrayList<ScanResult> scanResultList = new ArrayList<ScanResult>();
    private ArrayAdapterWLAN wlanAdapter;
    private Spinner sortingSpinner;
    private SortingHelper sortingHelper;
    private int currentSortingOption;

    private Wifi_ana mainActivity;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mainActivity = (Wifi_ana) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_ana, container, false);

        sortingHelper = new SortingHelper();

        sortingSpinner = (Spinner) view.findViewById(R.id.spinnerSorting);
        List<String> list = new ArrayList<String>();
        list.add(sortingHelper.getSortingOptionName(SortingHelper.SORTING_OPTION_LEVEL));
        list.add(sortingHelper.getSortingOptionName(SortingHelper.SORTING_OPTION_CHANNEL));
        list.add(sortingHelper.getSortingOptionName(SortingHelper.SORTING_OPTION_CHANNEL_WIDTH));
        list.add(sortingHelper.getSortingOptionName(SortingHelper.SORTING_OPTION_SSID));
       /* if (android.os.Build.VERSION.SDK_INT >= 30) {
            list.add(sortingHelper.getSortingOptionName(SortingHelper.SORTING_OPTION_WLAN_STANDARD));
        }*/

        ArrayAdapter<String> sortingAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, list);
        sortingSpinner.setAdapter(sortingAdapter);
        sortingSpinner.setOnItemSelectedListener(this);

        SharedPreferences sharedPrefs = getActivity().getPreferences(Context.MODE_PRIVATE);
        currentSortingOption = sharedPrefs.getInt(Util.PREF_SORTING_OPTION, SortingHelper.SORTING_OPTION_LEVEL);
        int spinnerPosition = sortingAdapter.getPosition(sortingHelper.getSortingOptionName(currentSortingOption));
        sortingSpinner.setSelection(spinnerPosition);

        lv = (RecyclerView) view.findViewById(R.id.list);

        scanResultList = new ArrayList<ScanResult>();

        wlanAdapter = new ArrayAdapterWLAN(getActivity(), scanResultList, this);
        lv.setAdapter(wlanAdapter);

        updateWLANList();

        getActivity().invalidateOptionsMenu();

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        SharedPreferences.Editor editor = getActivity().getPreferences(Context.MODE_PRIVATE).edit();
        editor.putInt(Util.PREF_SORTING_OPTION, currentSortingOption);
        editor.commit();
    }

    private void scanResultChanged() {
        updateWLANList();
    }

    private void updateWLANList() {
        liveDataApp mlive = new ViewModelProvider(getActivity()).get(liveDataApp.class);
        mlive.getScanResultMutableLiveData().observe(getViewLifecycleOwner(), new Observer<ArrayList<ScanResult>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChanged(ArrayList<ScanResult> scanResults) {
                scanResultList.clear();
                scanResultList.addAll(scanResults);
                SortingHelper.sort(scanResultList, currentSortingOption);
                wlanAdapter.notifyDataSetChanged();
            }
        });

    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
//    	ScanResult scanResult = (ScanResult) wlanAdapter.getItem(position);

//		DialogWLANListItemInfo dialogwlii = new DialogWLANListItemInfo(getActivity(), scanResult.SSID, scanResult.capabilities, Integer.toString(scanResult.frequency));
//		dialogwlii.show();
    }

    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
        String optionName = (String) sortingSpinner.getItemAtPosition(position);
        currentSortingOption = sortingHelper.getSortingOption(optionName);
        SortingHelper.sort(scanResultList, currentSortingOption);
        wlanAdapter.notifyDataSetChanged();
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }


    @Override
    public void itemViewClick(String mac , String ssid) {
        try {
            BottomAna bottomAna = new BottomAna(ssid,mac);
            bottomAna.show(getActivity().getSupportFragmentManager(),bottomAna.getTag());
        }catch (RuntimeException runtimeException) {

        }

    }
}
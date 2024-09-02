/*
 *  Copyright (C) 2020 Benjamin W. (bitbatzen@gmail.com)
 *
 *  This file is part of WLANScanner.
 *
 *  WLANScanner is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  WLANScanner is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with WLANScanner.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.wifinet.internetcheck.wifiana;

import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.wifinet.internetcheck.R;

import java.util.ArrayList;


public class FragmentDiagram6GHz
		extends Fragment
{

	LevelDiagram6GHz levelDiagram;


	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_wlan_diagram_6ghz, container, false);
		levelDiagram = (LevelDiagram6GHz) view.findViewById(R.id.levelDiagram6GHz);
		liveDataApp mlive = new ViewModelProvider(getActivity()).get(liveDataApp.class);
		mlive.getScanResultMutableLiveData().observe(getViewLifecycleOwner(), new Observer<ArrayList<ScanResult>>() {
			@Override
			public void onChanged(ArrayList<ScanResult> scanResults) {
				levelDiagram.updateDiagram(scanResults);
			}
		});


		return view;
	}

}
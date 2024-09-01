package com.kerolsmm.appmanager.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.kerolsmm.appmanager.R
import com.kerolsmm.appmanager.databinding.BottomDialogFilterBatteryBinding

class BottomDialogFilterBattery(var session: String, private var clickFilterBattery: ClickFilterBattery?) : BottomSheetDialogFragment() {

    constructor() : this("Daily",null)


    private lateinit var binding : BottomDialogFilterBatteryBinding



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BottomDialogFilterBatteryBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.sessionGroupBattery.setOnCheckedStateChangeListener { group, checkedIds ->

            for (it in checkedIds) {
                val chip = group.findViewById<Chip>(it)
                if (chip.id == R.id.session_daily) {
                    session = "Daily"
                } else if (chip.id == R.id.session_yearly) {
                    session = "Yearly"
                } else if (chip.id == R.id.session_monthly) {
                    session = "Monthly"
                } else if (chip.id == R.id.session_weekly) {
                    session = "Weekly"
                }

            }
        }



        when (session) {
            "Daily" -> binding.sessionGroupBattery.check(R.id.session_daily)
            "Weekly" -> binding.sessionGroupBattery.check(R.id.session_weekly)
            "Monthly" -> binding.sessionGroupBattery.check(R.id.session_monthly)
            "Yearly" -> binding.sessionGroupBattery.check(R.id.session_yearly)
        }


        binding.btnCancel.setOnClickListener {
            dismiss()
        }

        binding.btnOk.setOnClickListener {
            clickFilterBattery?.onClick(session)
            dismiss()
        }


    }



    interface ClickFilterBattery {
        fun onClick (session: String)
    }


}
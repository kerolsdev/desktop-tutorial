package com.kerols.phoneboost.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.kerols.phoneboost.R

class BottomDialogCustom(var mOnClickRadio : com.kerols.phoneboost.Fragments.BottomDialogCustom.OnClickRadio, var str : String)  : BottomSheetDialogFragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom, container, false)
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mRadios = view.findViewById<RadioGroup>(R.id.radioGroup)
        val big = view.findViewById<RadioButton>(R.id.radioBig)
        val small = view.findViewById<RadioButton>(R.id.radioSmall)
        val last = view.findViewById<RadioButton>(R.id.radioLast)

        if (str == "small"){
              small.isChecked = true
        }
        else if (str == "big") {
              big.isChecked = true
        }
        else {
             last.isChecked = true
        }

        mRadios.setOnCheckedChangeListener { group, checkedId ->
               if (checkedId == R.id.radioSmall) {
                     mOnClickRadio.onEventClick("small",this)
            } else if (checkedId == R.id.radioLast) {
                     mOnClickRadio.onEventClick("last",this)
            } else {
                     mOnClickRadio.onEventClick("big",this)
                }

        }
    }

    interface OnClickRadio {
        fun onEventClick(string: String , it  : com.kerols.phoneboost.Fragments.BottomDialogCustom)
    }

}
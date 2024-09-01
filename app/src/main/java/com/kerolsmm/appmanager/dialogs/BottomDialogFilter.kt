package com.kerolsmm.appmanager.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.kerolsmm.appmanager.R
import com.kerolsmm.appmanager.databinding.BottomDialogFilterBinding
import com.kerolsmm.appmanager.functions.Values
import com.kerolsmm.appmanager.functions.Values.SESSION_ALL_TIME
import com.kerolsmm.appmanager.functions.Values.SESSION_LAST_MONTH
import com.kerolsmm.appmanager.functions.Values.SESSION_THIS_MONTH
import com.kerolsmm.appmanager.functions.Values.SESSION_THIS_YEAR
import com.kerolsmm.appmanager.functions.Values.SESSION_TODAY
import com.kerolsmm.appmanager.functions.Values.SESSION_YESTERDAY
import com.kerolsmm.appmanager.functions.Values.TYPE_MOBILE_DATA


class BottomDialogFilter(private val click: ClickNetworkFilter?, var session: Int?, var type: Int?) : BottomSheetDialogFragment() {

    constructor() : this(null,null,null)
        // Initialize fragment if necessary


    private lateinit var binding  : BottomDialogFilterBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BottomDialogFilterBinding.inflate(inflater,container,false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)




        binding.sessionGroup.setOnCheckedStateChangeListener { group, checkedIds ->

            for (it in checkedIds) {

                val chip = group.findViewById<Chip>(it)


                if (chip.id == R.id.session_today) {
                    session = Values.SESSION_TODAY


                } else   if (chip.id == R.id.session_this_month) {
                    session = Values.SESSION_THIS_MONTH


                } else   if (chip.id == R.id.session_this_year) {
                    session = Values.SESSION_THIS_YEAR


                }else   if (chip.id == R.id.session_yesterday) {
                    session = Values.SESSION_YESTERDAY

                }else   if (chip.id == R.id.session_all_time) {
                    session = Values.SESSION_ALL_TIME


                }else   if (chip.id == R.id.session_last_month) {
                    session = Values.SESSION_LAST_MONTH
                }

            }

        }

            binding.typeGroup.setOnCheckedStateChangeListener{ group, checkedIds ->

                for (it in checkedIds) {

                    val chip = group.findViewById<Chip>(it)

                    if (chip.id == R.id.type_mobile) {
                        type = TYPE_MOBILE_DATA
                    } else   if (chip.id == R.id.type_wifi) {
                        type = Values.TYPE_WIFI
                    }

                }

            }


        when (session) {
            SESSION_TODAY -> binding.sessionGroup.check(R.id.session_today)
            SESSION_YESTERDAY -> binding.sessionGroup.check(R.id.session_yesterday)
            SESSION_THIS_MONTH -> binding.sessionGroup.check(R.id.session_this_month)
            SESSION_LAST_MONTH -> binding.sessionGroup.check(R.id.session_last_month)
            SESSION_THIS_YEAR -> binding.sessionGroup.check(R.id.session_this_year)
            SESSION_ALL_TIME -> binding.sessionGroup.check(R.id.session_all_time)
        }

        when (type) {
            Values.TYPE_MOBILE_DATA -> binding.typeGroup.check(R.id.type_mobile)
            Values.TYPE_WIFI -> binding.typeGroup.check(R.id.type_wifi)
        }

        binding.btnCancel.setOnClickListener {
            dismiss()
        }

        binding.btnOk.setOnClickListener {
            session?.let { it1 -> type?.let { it2 -> click?.click(it1, it2) } }
            dismiss()
        }



    }



    interface ClickNetworkFilter  {
        fun  click ( session: Int ,  type : Int)
    }


}
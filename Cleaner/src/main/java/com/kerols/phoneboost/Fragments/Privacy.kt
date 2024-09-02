package com.kerols.phoneboost.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kerols.phoneboost.R
import com.kerols.phoneboost.databinding.FragmentPrivacyBinding


class Privacy : Fragment() {


    lateinit var binding : FragmentPrivacyBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding  =  FragmentPrivacyBinding.inflate(inflater,container,false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.privacy.text = getString(R.string.privacy)




    }

}
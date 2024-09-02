package com.kerols.phoneboost.Fragments


import android.view.ViewGroup
import android.view.LayoutInflater
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.kerols.phoneboost.databinding.FragmentPackageFinishBinding

class Package_finish : Fragment() {

    private lateinit var binding: FragmentPackageFinishBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPackageFinishBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}
package com.kerols.phoneboost.Fragments

import android.os.Bundle
import android.text.format.Formatter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.kerols.phoneboost.databinding.FragmentCleanerFinishBinding
import kotlin.math.abs


class CleanerFinish : Fragment() {


    private lateinit var binding : FragmentCleanerFinishBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCleanerFinishBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
           super.onViewCreated(view, savedInstanceState)
           val args: CleanerFinishArgs? = arguments?.let { CleanerFinishArgs.fromBundle(it) }
           binding.freeram.text = args?.result?.let { Formatter.formatFileSize(context, abs(it)) }

    }
}
package com.example.tutor.statistic

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tutor.databinding.FragmentMainBinding
import com.example.tutor.databinding.FragmentStatisticBinding

class StatisticFragment : Fragment() {
    lateinit var binding: FragmentStatisticBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentStatisticBinding.inflate(inflater, container, false)
        return binding.root
    }

}
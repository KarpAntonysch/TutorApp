package com.example.tutor.statistic.lessons

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.tutor.databinding.FragmentLessonsStatisticBinding
import com.google.android.material.tabs.TabLayout
import java.util.concurrent.atomic.AtomicBoolean

class LessonsStatisticFragment : Fragment() {
    lateinit var binding: FragmentLessonsStatisticBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentLessonsStatisticBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }


}
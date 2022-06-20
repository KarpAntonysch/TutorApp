package com.example.tutor.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.tutor.R
import com.example.tutor.databinding.FragmentMainBinding
import java.text.SimpleDateFormat
import java.util.*


class MainFragment : Fragment() {
    lateinit var binding: FragmentMainBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    // передадим дату (Long) через  Bundle
        val bundle = Bundle()
        binding.btnAddToCalendar.setOnClickListener {
            val currentDate = binding.calendarView.date
            bundle.putLong("ArgForDate",currentDate)
            findNavController().navigate(R.id.action_mainFragment_to_addStudentToDaySchedule,bundle)
        }
    }
}
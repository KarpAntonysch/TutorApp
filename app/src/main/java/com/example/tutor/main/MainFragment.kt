package com.example.tutor.main

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import androidx.annotation.RequiresApi
import androidx.navigation.fragment.findNavController
import com.example.tutor.R
import com.example.tutor.databinding.FragmentMainBinding
import java.text.SimpleDateFormat
import java.time.LocalDate
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

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    // передадим дату (Long) через  Bundle
        val bundle = Bundle()

        var currentDate = binding.calendarView.date// текущая дата в Long
    //   выбранная пользователем дата  в Long

        binding.calendarView.setOnDateChangeListener { _, year, moth, dayOfMoth ->
            val currentCalendar: Calendar = Calendar.getInstance()
            currentCalendar.set(year, moth, dayOfMoth)
            currentDate = currentCalendar.timeInMillis
        }
        binding.btnAddToCalendar.setOnClickListener {
                bundle.putLong("ArgForDate",currentDate)
            findNavController().navigate(R.id.action_mainFragment_to_addStudentToDaySchedule,bundle)
        }
    }
}
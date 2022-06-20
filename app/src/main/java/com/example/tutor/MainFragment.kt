package com.example.tutor

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import androidx.navigation.fragment.findNavController
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

        val sdf = SimpleDateFormat("dd.M.yyyy")
        var currentDate = sdf.format(Date())
        binding.textView.text = currentDate

        binding.calendarView.setOnDateChangeListener { view_calendar, year, moth, dayOfMoth ->
            binding.textView.text = "$dayOfMoth.${moth + 1}.$year" }

        binding.btnAddToCalendar.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_addStudentToDaySchedule)
        }
    }
}
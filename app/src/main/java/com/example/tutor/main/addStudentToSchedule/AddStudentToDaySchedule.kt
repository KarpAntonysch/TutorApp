package com.example.tutor.main.addStudentToSchedule

import android.icu.text.SimpleDateFormat
import android.os.Binder
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import com.example.tutor.R
import com.example.tutor.databinding.FragmentAddStudentToDayScheduleBinding
import java.util.*


class AddStudentToDaySchedule : Fragment() {
    lateinit var binding: FragmentAddStudentToDayScheduleBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentAddStudentToDayScheduleBinding.inflate(inflater,container,false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Прием Bundle
        val currentDate = arguments?.getLong("ArgForDate")
        fun convertLongToTime(time: Long): String {
            val date = Date(time)
            val format = SimpleDateFormat("dd.MM.yyyy")
            return format.format(date)
        }

        binding.tvDate.text = convertLongToTime(currentDate!!)
    }

}
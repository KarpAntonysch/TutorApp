package com.example.tutor.main.addStudentToSchedule

import android.icu.text.SimpleDateFormat
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.tutor.bd.entities.ScheduleEntity
import com.example.tutor.databinding.FragmentAddStudentToDayScheduleBinding
import com.example.tutor.journal.studentJournal.DBlication
import java.util.*


class AddStudentToDaySchedule : Fragment() {
    lateinit var binding: FragmentAddStudentToDayScheduleBinding
    private val scheduleViewModel: AddStudentToScheduleViewModel by viewModels {
        AddStudentToScheduleViewModelFactory((requireActivity().application as DBlication).scheduleRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentAddStudentToDayScheduleBinding.inflate(inflater, container, false)
        return binding.root
    }


    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getCurrentDate()
    }


    //Прием даты с помощью Bundle
    @RequiresApi(Build.VERSION_CODES.N)
    fun getCurrentDate() {
        val currentDate = arguments?.getLong("ArgForDate")
        fun convertLongToTime(time: Long): String {
            val date = Date(time)
            val format = SimpleDateFormat("dd.MM.yyyy")
            return format.format(date)
        }
        binding.tvDate.text = convertLongToTime(currentDate!!)
    }

    // добавление объекта расписания в БД
    fun addScheduleToDB(scheduleEntity: ScheduleEntity){
        scheduleViewModel.insert(scheduleEntity)
    }

    fun getScheduleValues() : ScheduleEntity{
        val dateWithTime : Long
        val studentId : Int
        return ScheduleEntity(dateWithTime,studentId)
    }
}
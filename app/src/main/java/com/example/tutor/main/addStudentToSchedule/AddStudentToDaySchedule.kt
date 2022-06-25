package com.example.tutor.main.addStudentToSchedule

import android.annotation.SuppressLint
import android.icu.text.SimpleDateFormat
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TimePicker
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.tutor.bd.entities.ScheduleEntity
import com.example.tutor.bd.entities.studentForSchedule
import com.example.tutor.databinding.FragmentAddStudentToDayScheduleBinding
import com.example.tutor.journal.StudentJournalViewModel
import com.example.tutor.journal.StudentJournalViewModelFactory
import com.example.tutor.journal.studentJournal.DBapplication
import java.util.*
import java.util.Calendar.getInstance
import kotlin.collections.ArrayList


class AddStudentToDaySchedule : Fragment() {
    lateinit var binding: FragmentAddStudentToDayScheduleBinding
    private val scheduleViewModel: AddStudentToScheduleViewModel by viewModels {
        AddStudentToScheduleViewModelFactory((requireActivity().application as DBapplication).scheduleRepository)
    }
    private val studentJournalViewModel: StudentJournalViewModel by viewModels {
        StudentJournalViewModelFactory((requireActivity().application as DBapplication).studentRepository)
    }
    lateinit var  timePicker:TimePicker



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

        timePicker = binding.timePicker
        timePicker.setIs24HourView(true)

        getCurrentTime()
        getCurrentDate()
        //спинер
        val spinner = binding.spinnerForSchedule
        var infoList: ArrayList<studentForSchedule>

        // получение информации для таблицы schedule(id,firstname,secondname), реализация спинера
        studentJournalViewModel.getInfo().observe(viewLifecycleOwner,{
        infoList= ArrayList(it)
            // получение отдельных списков имен и фамилий
            val infoName = infoList.map { it.firstName }
            val infoSecondName = infoList.map { it.secondName }
            // комбинация этих списков и задание вида отображения в {}
            val spinnerList = infoName.zip(infoSecondName){
                    name1st, name2nd ->"$name1st $name2nd"}


            spinner.adapter = ArrayAdapter(requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                spinnerList)
        })
            spinner.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        p0: AdapterView<*>?,
                        p1: View?,
                        p2: Int,
                        p3: Long, ) {}

                    override fun onNothingSelected(p0: AdapterView<*>?) {
                    }

                }
    }
    //Прием даты с помощью Bundle
    @RequiresApi(Build.VERSION_CODES.N)
    fun getCurrentDate() {
        val currentDate = arguments?.getLong("ArgForDate")
        fun convertLongToTime(time: Long): String {
            val date = Date(time)
            val format = SimpleDateFormat("dd.MM.yyyy ")
            return format.format(date)
        }
        binding.tvDate.text = convertLongToTime(currentDate!!)
    }

    // Выбор времени с использованием TimePicker. Просто пример, нужно перерабатывать
    @SuppressLint("SetTextI18n", "NewApi")
    @RequiresApi(Build.VERSION_CODES.M)
    fun getCurrentTime():Long{
        var currentDate = arguments?.getLong("ArgForDate")
        timePicker.setOnTimeChangedListener { _, hour, minute ->
            // нужно вытащить время в long
            val cal:Calendar = getInstance()
            cal.set(Calendar.HOUR_OF_DAY,timePicker.hour)
            cal.set(Calendar.MINUTE,timePicker.minute)
            currentDate = cal.timeInMillis
            binding.textView2.text = currentDate.toString()
        }
        return currentDate!!
    }

    // добавление объекта расписания в БД (таблица2)
    fun addScheduleToDB(scheduleEntity: ScheduleEntity){
        scheduleViewModel.insert(scheduleEntity)
    }

    /*fun getScheduleValues() : ScheduleEntity{
        val currentDate = arguments?.getLong("ArgForDate")
        val currentTime = binding.edTime.toString().toLong()
        val dateWithTime : Long = currentTime + currentDate!!
        val studentId : Int
        return ScheduleEntity(dateWithTime,studentId)
    }*/
}
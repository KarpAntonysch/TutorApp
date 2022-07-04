package com.example.tutor.main.addStudentToSchedule

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.tutor.bd.entities.ScheduleEntity
import com.example.tutor.bd.entities.studentForSchedule
import com.example.tutor.convertLongToTime
import com.example.tutor.databinding.FragmentAddStudentToDayScheduleBinding
import com.example.tutor.journal.StudentJournalViewModel
import com.example.tutor.journal.StudentJournalViewModelFactory
import com.example.tutor.journal.studentJournal.DBapplication
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
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
    var studentID: Int = 0 // начальная инициализация. задаю как 0 т.к. мой id !=0
    var timeFromPicker: Long = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentAddStudentToDayScheduleBinding.inflate(inflater, container, false)
        return binding.root
    }


    @SuppressLint("NewApi")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.timePicker.setIs24HourView(true)

        binding.tvDate.text = getCurrentDate().convertLongToTime("dd.MM.yyyy")

        //спинер
        val spinner = binding.spinnerForSchedule
        var infoList: ArrayList<studentForSchedule>

        // получение информации для таблицы schedule(id,firstname,secondname), реализация спинера
        studentJournalViewModel.getInfo().observe(viewLifecycleOwner, {
            infoList = ArrayList(it)
            // получение отдельных списков имен и фамилий
            val infoName = infoList.map { it.firstName }
            val infoSecondName = infoList.map { it.secondName }
            // комбинация этих списков и задание вида отображения в {}
            val spinnerList =
                infoName.zip(infoSecondName) { name1st, name2nd -> "$name1st $name2nd" }
            //spinner.setSelection(0) для установки значения по умолчанию
            spinner.adapter = ArrayAdapter(requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                spinnerList)
            spinner.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        p0: AdapterView<*>?,
                        p1: View?,
                        position: Int,
                        p3: Long,
                    ) {
                        val item = spinner.getItemAtPosition(position)
                        studentID = infoList[position].id

                    }

                    override fun onNothingSelected(p0: AdapterView<*>?) {
                    }
                }
        })

        getCurrentTime()
        binding.btnAddSchedule.setOnClickListener {
            binding.textView3.text = studentID.toString()
            val jointDate = getCurrentDate().convertLongToTime("dd.MM.yyyy") +
                    timeFromPicker?.convertLongToTime(" H:m")
            //Перевожу строки в дату с помощью DateTimeFormatter
            val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy H:m")
            val formatDate = LocalDateTime.parse(jointDate, formatter)
            // приведение к необходимому виду
            val newFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy H:m")
            val formattedDate = formatDate.format(newFormatter)
            binding.textView2.text = formattedDate
        }
    }


    //Прием даты с помощью Bundle

    fun getCurrentDate(): Long {
        val currentDate = arguments?.getLong("ArgForDate")
        return currentDate!!
    }

    // Выбор времени с использованием TimePicker.
    @SuppressLint("SetTextI18n", "NewApi")
    fun getCurrentTime() {
        val cal: Calendar = getInstance()
        timeFromPicker=getCurrentDate()
        //binding.textView2.text = getCurrentDate().convertLongToTime(" H:m")
        // вывод выбранного значения времени
        binding.timePicker.setOnTimeChangedListener { _, hour, minute ->
            cal.set(Calendar.HOUR_OF_DAY, hour)
            cal.set(Calendar.MINUTE, minute)
            timeFromPicker = cal.timeInMillis
            binding.textView2.text = timeFromPicker?.convertLongToTime(" H:m")
        }




    }

    /* fun getScheduleValues() : ScheduleEntity {
         val dateWithTime: Long
         val studentId: Int = studentID
         return ScheduleEntity(dateWithTime, studentId)
     }*/
    // добавление объекта расписания в БД (таблица2)
    fun addScheduleToDB(scheduleEntity: ScheduleEntity) {
        scheduleViewModel.insert(scheduleEntity)
    }
}

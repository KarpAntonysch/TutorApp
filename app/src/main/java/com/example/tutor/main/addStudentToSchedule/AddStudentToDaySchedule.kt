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
import com.example.tutor.R
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


class AddStudentToDaySchedule : Fragment(){
    lateinit var binding: FragmentAddStudentToDayScheduleBinding
    private val scheduleViewModel: AddStudentToScheduleViewModel by viewModels {
        AddStudentToScheduleViewModelFactory((requireActivity().application as DBapplication).scheduleRepository)
    }
    private val studentJournalViewModel: StudentJournalViewModel by viewModels {
        StudentJournalViewModelFactory((requireActivity().application as DBapplication).studentRepository)
    }




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentAddStudentToDayScheduleBinding.inflate(inflater, container, false)
        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.timePicker.setIs24HourView(true)

        val dateForSchedule = getCurrentDate()// Long
        binding.tvDate.text = dateForSchedule.convertLongToTime("dd.MM.yyyy")

        getCurrentTime()


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
                        p3: Long, ) {
                        val item = spinner.getItemAtPosition(position)

                        binding.textView3.text = infoList[position].id.toString()
                    }

                    override fun onNothingSelected(p0: AdapterView<*>?) {
                    }

                }


        })



    }

    //Прием даты с помощью Bundle

    fun getCurrentDate():Long {
        val currentDate = arguments?.getLong("ArgForDate")
        return currentDate!!
    }

    // Выбор времени с использованием TimePicker.
    @SuppressLint("SetTextI18n", "NewApi")
    fun getCurrentTime() {

        var timeFromPicker: Long? = arguments?.getLong("ArgForDate")
        val cal: Calendar = getInstance()
        binding.textView2.text = timeFromPicker?.convertLongToTime(" H:m")
        // вывод выбранного значения времени
        binding.timePicker.setOnTimeChangedListener { _, hour, minute ->
            cal.set(Calendar.HOUR_OF_DAY, hour)
            cal.set(Calendar.MINUTE, minute)
            timeFromPicker = cal.timeInMillis
            binding.textView2.text = timeFromPicker?.convertLongToTime(" H:m")
        }

        // при нажатии на кнопку собираю значение. Дата из  getCurrentDate(), время из пикера
        binding.btnAddSchedule.setOnClickListener {
            val dateForSchedule = getCurrentDate()
            var jointDate = dateForSchedule.convertLongToTime("dd.MM.yyyy") +
                    timeFromPicker?.convertLongToTime(" H:m")
            //Перевожу строки в дату с помощью DateTimeFormatter
            val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy H:m")
            val formatDate = LocalDateTime.parse(jointDate,formatter)
            // приведение к необходимому виду
            val newFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy H:m")
            val formattedDate = formatDate.format(newFormatter)
            binding.textView2.text = formattedDate.toString()
        }
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

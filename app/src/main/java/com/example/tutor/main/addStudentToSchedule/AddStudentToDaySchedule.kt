package com.example.tutor.main.addStudentToSchedule

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.tutor.R
import com.example.tutor.bd.entities.ScheduleEntity
import com.example.tutor.convertLongToTime
import com.example.tutor.databinding.FragmentAddStudentToDayScheduleBinding
import com.example.tutor.dialogs.JointDialogInterface
import com.example.tutor.journal.StudentJournalViewModel
import com.example.tutor.journal.StudentJournalViewModelFactory
import com.example.tutor.journal.studentJournal.DBapplication
import java.util.*
import java.util.Calendar.getInstance


class AddStudentToDaySchedule : Fragment(),JointDialogInterface {

    lateinit var binding: FragmentAddStudentToDayScheduleBinding
    private val scheduleViewModel: AddStudentToScheduleViewModel by viewModels {
        AddStudentToScheduleViewModelFactory((requireActivity().application as DBapplication).scheduleRepository)
    }
    private val studentJournalViewModel: StudentJournalViewModel by viewModels {
        StudentJournalViewModelFactory((requireActivity().application as DBapplication).studentRepository)
    }
    private var timeFromPicker: Long = 0
    private val addStudentToDayScheduleClass = AddStudentToDayScheduleClass()// объект класса логики

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentAddStudentToDayScheduleBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        return binding.root
    }


    @SuppressLint("NewApi")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolBarSetting()
        binding.timePicker.setIs24HourView(true)
        binding.tvDate.text = getCurrentDate().convertLongToTime("dd.MM.yyyy")
        spinnerRealization()
        getCurrentTime()
        addingSchedule()
    }

    @SuppressLint("NewApi")
    private fun addingSchedule() {
        binding.btnAddSchedule.setOnClickListener {
            formattedCurrentDate()
            // Проверка на наличие студентов в спинере
            if (getScheduleValues().studentId == 0) {
                showInfoDialogFragment("warning")
            } else {
                addScheduleToDB(getScheduleValues())
                activity?.onBackPressed()/*"мягкое" закрытие фрагмента. Т.е. фрагмент просыпается из стека.
             Он не уничтожается из стека, не создается новый экземпляр этого фрагмента в стеке,
                в отличие от findNavController().navigate(R.id.action_addStudentToDaySchedule_to_mainFragment)
                здесь в стек добавляется новый экземпляр фрагмента, без уничтожения старого*/
            }
        }
    }

    private fun spinnerRealization() {
        // получение информации для таблицы schedule(id,firstname,secondName), реализация спинера
        studentJournalViewModel.getInfo().observe(viewLifecycleOwner, {
            //спинер
            val spinner = binding.spinnerForSchedule
            val newList = scheduleViewModel.getNewList(it)
            spinner.adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                newList
            )
            spinner.setSelection(scheduleViewModel.searchID(newList)) //для установки значения по умолчанию
            spinner.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        p0: AdapterView<*>?,
                        p1: View?,
                        position: Int,
                        p3: Long,
                    ) {
                        scheduleViewModel.studentID = newList[position].id
                    }
                    override fun onNothingSelected(p0: AdapterView<*>?) {
                    }
                }
        })
    }

    private fun toolBarSetting() {
        val toolbar = binding.addScheduleToolbar
        (activity as AppCompatActivity).supportActionBar?.hide()
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.title = "Добавление занятия"
    }

    // меню ToolBar
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.pages_tool_bar, menu)
    }

    // слушатель айтемов меню ToolBar
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> activity?.onBackPressed()
            R.id.info -> {
                showInfoDialogFragment("schedule")
            }
        }
        return true
    }

    //Прием даты с помощью Bundle
    private fun getCurrentDate(): Long {
        val currentDate = arguments?.getLong("ArgForDate")
        return currentDate!!
    }

    // Выбор времени с использованием TimePicker.
    @SuppressLint("SetTextI18n", "NewApi")
    fun getCurrentTime() {
        val cal: Calendar = getInstance()
        timeFromPicker = getCurrentDate() // текущее время без использования спинера
        // вывод выбранного значения времени
        binding.timePicker.setOnTimeChangedListener { _, hour, minute ->
            cal.set(Calendar.HOUR_OF_DAY, hour)
            cal.set(Calendar.MINUTE, minute)
            timeFromPicker = cal.timeInMillis
        }
    }

    // сбор общего времени из отдельной даты и отдельного времени. Приведение данных в нужный формат
    @RequiresApi(Build.VERSION_CODES.O)
    fun formattedCurrentDate(): Long {
        return addStudentToDayScheduleClass.formattedCurrentDate(getCurrentDate(), timeFromPicker)
    }

    // Заполнение объекта ScheduleEntity временем и id
    @SuppressLint("NewApi")
    fun getScheduleValues(): ScheduleEntity {
        return addStudentToDayScheduleClass.getScheduleValues(formattedCurrentDate(), scheduleViewModel.studentID)
    }

    // добавление объекта расписания в БД (scheduleTable)
    private fun addScheduleToDB(scheduleEntity: ScheduleEntity) {
        scheduleViewModel.insert(scheduleEntity)
    }

    // Функция вызова диалогового окна из InfoDialogFragment. Для подсказки в toolBar и ошибки пустого спинера
    private fun showInfoDialogFragment(target: String) {
        if (target == "schedule") {
            showJoinDialog(R.string.hint,false,R.string.good,
                R.string.empty,childFragmentManager,R.string.addStudentToSchedule,true)
        }
        if (target == "warning") {
            showJoinDialog(R.string.hint,false,R.string.good,
                R.string.empty,childFragmentManager,R.string.emptyStudent,true)
        }

    }
}

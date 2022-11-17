package com.example.tutor.main.addStudentToSchedule

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
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
import com.example.tutor.dialogs.*
import com.example.tutor.fireBase.FireBaseRepository
import com.example.tutor.journal.studentJournal.DBapplication
import com.example.tutor.journal.studentJournal.pager.activeStudents.StudentJournalViewModel
import com.example.tutor.journal.studentJournal.pager.activeStudents.StudentJournalViewModelFactory
import java.util.*
import java.util.Calendar.getInstance


class AddStudentToDaySchedule : Fragment(), JointDialogInterface,NotificationListener,PeriodListener{

    lateinit var binding: FragmentAddStudentToDayScheduleBinding
    private val scheduleViewModel: AddStudentToScheduleViewModel by viewModels {
        AddStudentToScheduleViewModelFactory((requireActivity().application as DBapplication).scheduleRepository,
            requireActivity().application)
    }
    private val studentJournalViewModel: StudentJournalViewModel by viewModels {
        StudentJournalViewModelFactory((requireActivity().application as DBapplication).studentRepository,
            FireBaseRepository()
        )
    }
    private var timeFromPicker: Long = 0
    private val addStudentToDayScheduleClass = AddStudentToDayScheduleClass()// объект класса логики
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentAddStudentToDayScheduleBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        createChannel(getString(R.string.push_notification_channel_id),
            getString(R.string.push_notification_channel_name))
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolBarSetting()
        binding.timePicker.setIs24HourView(true)
        binding.tvDate.text = getCurrentDate().convertLongToTime("dd.MM.yyyy")
        binding.notificationValue.text = requireContext().getText(R.string.tenMinutes)
        binding.periodValue.text = requireContext().getText(R.string.day1)
        spinnerRealization()
        getCurrentTime()
        addingSchedule()
        showNotificationSettings()
        showPeriodSettings()

    }

    private fun showPeriodSettings(){
        binding.periodSettings.setOnClickListener{
            PeriodBottomFragment(this).show(childFragmentManager,"tag1")
        }
    }

    private fun showNotificationSettings(){
        binding.notificationSettings.setOnClickListener{
            NotificationBottomFragment(this).show(childFragmentManager,"tag")
        }
    }
    private fun addingSchedule() {
        binding.btnAddSchedule.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                formattedCurrentDate()
                addScheduleToDB(getScheduleValues())
                //TODO
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
            val spinnerCheck: Int? = scheduleViewModel.studentID/*проверка выбранного ученика
            при изменении конфигурации, если ученик был удален, то список в спинере по порядку, если
            не удален, то выбор запоминается и переживает конфигурацию*/
            val spinner = binding.spinnerForSchedule
            val newList = scheduleViewModel.getNewList(it)
            spinner.adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                newList
            )
            if (spinnerCheck !== null) {
                spinner.setSelection(scheduleViewModel.searchID(newList))
            }
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
                showJoinDialog(R.string.hint, false, R.string.good,
                    R.string.empty, childFragmentManager, R.string.addStudentToSchedule, true)
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
        val delay = scheduleViewModel.notificationDelay.value
        return addStudentToDayScheduleClass.getScheduleValues(formattedCurrentDate(),
            scheduleViewModel.studentID!!, delay!!)
    }

    // добавление объекта расписания в БД (scheduleTable)+создание оповещения
    private fun addScheduleToDB(scheduleEntity: ScheduleEntity) {
        scheduleViewModel.insertWithPeriod(scheduleEntity)
    }

    private fun createChannel(channelId: String, channelName: String) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                channelId,
                channelName,

                NotificationManager.IMPORTANCE_HIGH
            )
                .apply {
                    setShowBadge(false)
                }

            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.description = getString(R.string.push_message)

            val notificationManager = requireActivity().getSystemService(
                NotificationManager::class.java
            )
            notificationManager.createNotificationChannel(notificationChannel)

        }
    }
// коллбэк для notificationBottomSheet
    override fun notification1() {
       binding.notificationValue.text = requireContext().getText(R.string.tenMinutes)
        scheduleViewModel.notificationCondition.value="ten"
    }

    override fun notification2() {
        binding.notificationValue.text = requireContext().getText(R.string.fifteensMinutes)
        scheduleViewModel.notificationCondition.value="fifteen"
        scheduleViewModel.notificationDelay.value = 900000
    }

    override fun notification3() {
        binding.notificationValue.text = requireContext().getText(R.string.thirteensMinutes)
        scheduleViewModel.notificationCondition.value="thirty"
        scheduleViewModel.notificationDelay.value = 1800000
    }

    override fun notification4() {
        binding.notificationValue.text = requireContext().getText(R.string.cancelNotification)
        scheduleViewModel.notificationCondition.value="cancel"
        scheduleViewModel.notificationDelay.value = 0
    }
    // коллбэк для periodBottomSheet
    override fun period0() {
        binding.periodValue.text = requireContext().getText(R.string.day1)
        scheduleViewModel.periodCondition.value = "day"
    }

    override fun period1() {
        binding.periodValue.text = requireContext().getText(R.string.week)
        scheduleViewModel.periodCondition.value = "week"
    }

    override fun period2() {
        binding.periodValue.text = requireContext().getText(R.string.month)
        scheduleViewModel.periodCondition.value = "month"
    }

    override fun period3() {
        binding.periodValue.text = requireContext().getText(R.string.halfYear)
        scheduleViewModel.periodCondition.value = "halfYear"
    }

    override fun period4() {
        binding.periodValue.text = requireContext().getText(R.string.year)
        scheduleViewModel.periodCondition.value = "year"
    }


}

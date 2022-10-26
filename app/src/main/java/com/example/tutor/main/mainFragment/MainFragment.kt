package com.example.tutor.main.mainFragment

import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.tutor.R
import com.example.tutor.adapters.MainFragmentAdapter
import com.example.tutor.bd.entities.ScheduleEntity
import com.example.tutor.convertLongToTime
import com.example.tutor.databinding.FragmentMainBinding
import com.example.tutor.dialogs.JointDialogFragment
import com.example.tutor.dialogs.JointDialogInterface
import com.example.tutor.fireBase.FireBaseRepository
import com.example.tutor.fireBase.FireBaseViewModel
import com.example.tutor.journal.studentJournal.pager.activeStudents.StudentJournalViewModel
import com.example.tutor.journal.studentJournal.pager.activeStudents.StudentJournalViewModelFactory
import com.example.tutor.journal.studentJournal.DBapplication
import com.google.firebase.auth.FirebaseAuth
import java.util.*
import android.content.Intent
import android.net.Uri


class MainFragment : Fragment(), MainFragmentAdapter.Listener,
    JointDialogInterface {
    lateinit var binding: FragmentMainBinding
    private val mainFragmentViewModel: MainFragmentViewModel by viewModels {
        MainFragmentViewModelFactory((requireActivity().application as DBapplication).scheduleRepository)
    }
    private val studentJournalViewModel: StudentJournalViewModel by viewModels {
        StudentJournalViewModelFactory((requireActivity().application as DBapplication).studentRepository,
            FireBaseRepository()
        )
    }

    lateinit var recyclerView: RecyclerView
    private var adapter = MainFragmentAdapter(this)
    private var selectedDate: Long? = null
    private val fireBaseViewModel = FireBaseViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        saveSelectedDate()
        return binding.root

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.v("tess", "onViewCreated")
        super.onViewCreated(view, savedInstanceState)
        toolBarSetting()
        // передадим дату (Long) через  Bundle
        val bundleForSchedule = Bundle()
        val bundleForStudent = Bundle()
        var currentDate = binding.calendarView.date
        dateDisplaying(currentDate)
        realizationOfRV2(currentDate.convertLongToTime("dd-MM-yyyy"))
        binding.calendarView.setOnDateChangeListener { _, year, moth, dayOfMoth ->
            // возвращает календарь с установленными по умолчанию часовым поясом и языком
            val currentCalendar: Calendar = Calendar.getInstance()
            // устанавливаем выбранную дату
            currentCalendar.set(year, moth, dayOfMoth)
            // возвращает дату и время в Long
            currentDate = currentCalendar.timeInMillis
            selectedDate = currentDate
            realizationOfRV2(currentDate.convertLongToTime("dd-MM-yyyy"))
            dateDisplaying(currentDate)
        }

        studentJournalViewModel.allActiveStudents.observe(viewLifecycleOwner, {
            if (it.isNullOrEmpty()) {
                binding.btnAddToCalendar.setOnClickListener {
                    //проверка на нулевой или пустой список в Журнале (==пустой список в спинере)
                    showJoinDialog(
                        R.string.warning, false, R.string.toJournal, R.string.empty,
                        childFragmentManager, R.string.warningText, true
                    )
                    setupAddBtnDialogFragmentListener(bundleForStudent)
                }
            } else {
                binding.btnAddToCalendar.setOnClickListener {
                    bundleForSchedule.putLong("ArgForDate", currentDate)
                    findNavController().navigate(
                        R.id.action_mainFragment_to_addStudentToDaySchedule,
                        bundleForSchedule
                    )
                }
            }
        })
        hideFAB()
    }

    private fun dateDisplaying(currentDate: Long) {
        binding.tvSelectedDate.text = currentDate.convertLongToTime("dd MMMM yyyy")
    }

    private fun toolBarSetting() {

        // Скрытие toolbar activity и создание своего
        (activity as AppCompatActivity).supportActionBar?.hide()
        val toolbar = binding.fragmentToolbar
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as AppCompatActivity).supportActionBar?.title =
            FirebaseAuth.getInstance().currentUser?.displayName
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_exit)
    }

    // меню ToolBar
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.pages_tool_bar, menu)
    }

    // слушатель айтемов меню ToolBar
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                showJoinDialog(
                    R.string.exitQuestion, true, R.string.yes, R.string.no,
                    childFragmentManager, R.string.mainFragmentDialog, false
                )
                setupJoinDialogFragmentListener()
            }
            R.id.info -> {
                showJoinDialog(
                    R.string.hint, false, R.string.good, R.string.empty,
                    childFragmentManager, R.string.mainFragmentDialog, true
                )
            }
        }
        return true
    }

    private fun exitFromFbAccount() {
        fireBaseViewModel.signOut()//выход из аккаунта
        findNavController().navigate(R.id.action_mainFragment_to_entranceActivity)//переход на экран авторизации
    }

    private fun realizationOfRV2(currentDate: String) {
        recyclerView = binding.recyclerviewSchedule
        recyclerView.adapter = adapter
        mainFragmentViewModel.getScheduleOfDay(currentDate)
            .observe(viewLifecycleOwner) { scheduleList ->// добавлена сортировка по времени
                scheduleList.let { adapter.submitList(it.sortedBy {order -> order.scheduleEntity.dateWithTime }) }
            }
    }

    private fun saveSelectedDate() {
        if (selectedDate !== null) {
            binding.calendarView.date = selectedDate!!
        }
    }

    // функция для удаления объекта в расписании на день через dialog, переопределенная функция из
    // интерфейса
    override fun onClickToDeleteSchedule(scheduleEntity: ScheduleEntity) {
        showJoinDialog(
            R.string.deleteQuestion, true, R.string.yes, R.string.no,
            childFragmentManager, R.string.empty, false
        )
        setupDialogFragmentListener(scheduleEntity)
    }

    override fun onClickToCall(phoneNumber: String) {
        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phoneNumber"))
            startActivity(intent)
    }

    private fun setupAddBtnDialogFragmentListener(bundleForStudent: Bundle) {
        childFragmentManager.setFragmentResultListener(
            JointDialogFragment.REQUEST_KEY,
            this,
            FragmentResultListener { _, result ->
                when (result.getInt(JointDialogFragment.KEY_RESPONSE)) {
                    DialogInterface.BUTTON_POSITIVE ->{
                        bundleForStudent.putInt("ArgForStudent",1)
                        findNavController().navigate(
                        R.id.action_mainFragment_to_addStudentToJournalFragment,bundleForStudent
                    )
                   }
                }
            })
    }
    // инициализация кнопки выхода из аккаунта
    private fun setupJoinDialogFragmentListener() {
        childFragmentManager.setFragmentResultListener(
            JointDialogFragment.REQUEST_KEY,
            this,
            FragmentResultListener { _, result ->
                when (result.getInt(JointDialogFragment.KEY_RESPONSE)) {
                    DialogInterface.BUTTON_POSITIVE -> exitFromFbAccount()
                }
            })
    }

    // Функция инициализации кнопки удаления в диалоговом окне
    private fun setupDialogFragmentListener(scheduleEntity: ScheduleEntity) {
        childFragmentManager.setFragmentResultListener(
            JointDialogFragment.REQUEST_KEY,
            this,
            FragmentResultListener { _, result ->
                when (result.getInt(JointDialogFragment.KEY_RESPONSE)) {
                    DialogInterface.BUTTON_POSITIVE -> mainFragmentViewModel
                        .deleteSchedule(scheduleEntity)
                }
            })
    }

    private fun hideFAB() {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                // вниз
                if (dy > 0 && binding.btnAddToCalendar.visibility === View.VISIBLE) {
                    binding.btnAddToCalendar.hide()
                    // вверх и статика
                } else if (dy <= 0 && binding.btnAddToCalendar.visibility !== View.VISIBLE) {
                    binding.btnAddToCalendar.show()
                }
            }
        })
    }
}

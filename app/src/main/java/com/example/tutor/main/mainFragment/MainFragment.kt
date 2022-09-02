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
import com.example.tutor.InfoDialogFragment
import com.example.tutor.R
import com.example.tutor.adapters.MainFragmentAdapter
import com.example.tutor.bd.entities.ScheduleEntity
import com.example.tutor.convertLongToTime
import com.example.tutor.databinding.FragmentMainBinding
import com.example.tutor.journal.studentJournal.DBapplication
import java.util.*


class MainFragment : Fragment(), MainFragmentAdapter.Listener {
    lateinit var binding: FragmentMainBinding
    private val mainFragmentViewModel: MainFragmentViewModel by viewModels {
        MainFragmentViewModelFactory((requireActivity().application as DBapplication).scheduleRepository)
    }
    lateinit var recyclerView: RecyclerView
    private var adapter = MainFragmentAdapter(this)
    private var selectedDate:Long? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        if (selectedDate !== null){
            binding.calendarView.date = selectedDate!!
            realizationOfRV2(selectedDate!!.convertLongToTime("dd-MM-yyyy"))
        }
        return binding.root

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.v("tess","onViewCreated")
        super.onViewCreated(view, savedInstanceState)
        toolBarSetting()
        // передадим дату (Long) через  Bundle
        val bundle = Bundle()
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
            selectedDate= currentDate
            realizationOfRV2(currentDate.convertLongToTime("dd-MM-yyyy"))
            dateDisplaying(currentDate)
        }
        binding.btnAddToCalendar.setOnClickListener {
            bundle.putLong("ArgForDate", currentDate)
            findNavController().navigate(R.id.action_mainFragment_to_addStudentToDaySchedule,
                bundle)
        }
        hideFAB()
    }

    private fun dateDisplaying(currentDate : Long){
        binding.tvSelectedDate.text = currentDate.convertLongToTime("dd MMMM yyyy")
    }
    private fun  toolBarSetting(){
        // Скрытие toolbar activity и создание своего
        (activity as AppCompatActivity).supportActionBar?.hide()
        val toolbar = binding.fragmentToolbar
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as AppCompatActivity).supportActionBar?.title = "Расписание"
    }
    // меню ToolBar
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.pages_tool_bar, menu)
    }

    // слушатель айтемов меню ToolBar
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.info -> {
                showInfoDialogFragment()
            }
        }
        return true
    }

    private fun realizationOfRV2(currentDate: String) {
        recyclerView = binding.recyclerviewSchedule
        recyclerView.adapter = adapter
        mainFragmentViewModel.getScheduleOfDay(currentDate)
            .observe(viewLifecycleOwner) { scheduleList ->// добавлена сортировка по времени
                scheduleList.let { adapter.submitList(it.sortedBy { it.scheduleEntity.dateWithTime }) }
            }
    }

    // функция для удаления объекта в расписании на день через dialog, переопределенная функция из
    // интерфейса
    override fun onClickToDeleteSchedule(scheduleEntity: ScheduleEntity) {
        showDialogFragment()
        setupDialogFragmentListener(scheduleEntity)
    }

    // Функция вызова диалогового окна из ScheduleDialogFragment
    private fun showDialogFragment() {
        val dialogFragment = ScheduleDialogFragment()
        dialogFragment.show(childFragmentManager, ScheduleDialogFragment.TAG)
    }

    // Функция инициализации кнопок в диалоговом окне из JournalDialogFragment
    private fun setupDialogFragmentListener(scheduleEntity: ScheduleEntity) {
        childFragmentManager.setFragmentResultListener(ScheduleDialogFragment.REQUEST_KEY,
            this,
            FragmentResultListener { _, result ->
                val that = result.getInt(ScheduleDialogFragment.KEY_RESPONSE)
                when (that) {
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

    // Функция вызова диалогового окна из InfoDialogFragment
    private fun showInfoDialogFragment() {
        val dialogFragment = InfoDialogFragment("Подсказка",R.string.mainFragmentDialog)
        dialogFragment.show(childFragmentManager, InfoDialogFragment.TAG)
    }
}

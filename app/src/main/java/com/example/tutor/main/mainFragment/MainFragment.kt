package com.example.tutor.main.mainFragment

import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.tutor.R
import com.example.tutor.adapters.MainFragmentAdapter
import com.example.tutor.bd.entities.ScheduleEntity
import com.example.tutor.bd.entities.ScheduleWithStudent
import com.example.tutor.bd.entities.StudentEntity
import com.example.tutor.convertLongToTime
import com.example.tutor.databinding.FragmentMainBinding
import com.example.tutor.journal.studentJournal.DBapplication
import com.example.tutor.journal.studentJournal.JourmalDialogFragment
import java.util.*


class MainFragment : Fragment(), MainFragmentAdapter.Listener {
    lateinit var binding: FragmentMainBinding
    private val mainFragmentViewModel: MainFragmentViewModel by viewModels {
        MainFragmentViewModelFactory((requireActivity().application as DBapplication).scheduleRepository)
    }
    lateinit var recyclerView: RecyclerView
    private var adapter = MainFragmentAdapter(this)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // передадим дату (Long) через  Bundle
        val bundle = Bundle()
        var currentDate = binding.calendarView.date
         realizationOfRV2(currentDate.convertLongToTime("dd-MM-yyyy"))
        binding.calendarView.setOnDateChangeListener { _, year, moth, dayOfMoth ->
            // возвращает календарь с установленными по умолчанию часовым поясом и языком
            val currentCalendar: Calendar = Calendar.getInstance()
            // устанавливаем выбранную дату
            currentCalendar.set(year, moth, dayOfMoth)
            // возвращает дату и время в Long
            currentDate = currentCalendar.timeInMillis
            realizationOfRV2(currentDate.convertLongToTime("dd-MM-yyyy"))
        }
        binding.btnAddToCalendar.setOnClickListener {
            bundle.putLong("ArgForDate", currentDate)
            findNavController().navigate(R.id.action_mainFragment_to_addStudentToDaySchedule,
                bundle)
        }
    }

    private fun realizationOfRV2(currentDate: String) {
        recyclerView = binding.recyclerviewSchedule
        recyclerView.adapter = adapter
        mainFragmentViewModel.getScheduleOfDay(currentDate).observe(viewLifecycleOwner){
           scheduleList ->// добавлена сортировка по времени
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
    fun showDialogFragment() {
        val dialogFragment = ScheduleDialogFragment()
        dialogFragment?.show(childFragmentManager, ScheduleDialogFragment.TAG)
    }

    // Функция инициализации кнопок в диалоговом окне из JournalDialogFragment
    fun setupDialogFragmentListener(scheduleEntity: ScheduleEntity) {
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

}
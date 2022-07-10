package com.example.tutor.main.mainFragment

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.tutor.R
import com.example.tutor.adapters.MainFragmentAdapter
import com.example.tutor.convertLongToTime
import com.example.tutor.databinding.FragmentMainBinding
import com.example.tutor.journal.studentJournal.DBapplication
import java.util.*


class MainFragment : Fragment() {
    lateinit var binding: FragmentMainBinding
    private val mainFragmentViewModel: MainFragmentViewModel by viewModels {
        MainFragmentViewModelFactory((requireActivity().application as DBapplication).scheduleRepository)
    }
    lateinit var recyclerView: RecyclerView
    private var adapter = MainFragmentAdapter()

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
           scheduleList ->
            scheduleList.let { adapter.submitList(it) }
        }
    }
}
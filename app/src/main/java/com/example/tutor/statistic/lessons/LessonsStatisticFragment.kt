package com.example.tutor.statistic.lessons

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.tutor.databinding.FragmentLessonsStatisticBinding
import com.example.tutor.journal.studentJournal.DBapplication
import com.example.tutor.statistic.AmountStatisticFragmentViewModel
import com.example.tutor.statistic.StatisticFragmentViewModelFactory
import com.google.android.material.tabs.TabLayout
import java.time.chrono.ChronoPeriod
import java.util.concurrent.atomic.AtomicBoolean

class LessonsStatisticFragment : Fragment() {
    lateinit var binding: FragmentLessonsStatisticBinding
    private val lessonsStatisticFragmentViewModel: LessonsStatisticFragmentViewModel by viewModels {
        LessonsStatisticFragmentViewModelFactory((requireActivity().application as DBapplication).scheduleRepository)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentLessonsStatisticBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val tabLayout = binding.lessonsTab
        getWeekLessons()

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab) {
                if (tab.position ==0){
                    getWeekLessons()
                }else if(tab.position ==1){
                    getMonthLessons()
                }else if(tab.position ==2){
                    getPeriodLessons("-5 months")
                }else if(tab.position ==3){
                    getPeriodLessons("-11 months")
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}

            override fun onTabReselected(tab: TabLayout.Tab) {}

        })
    }

    fun getWeekLessons(){
        lessonsStatisticFragmentViewModel.totalWeekLessons.observe(viewLifecycleOwner){
            binding.tvLessons.text = "Занятий за неделю : $it"
        }
    }
    fun getMonthLessons(){
        lessonsStatisticFragmentViewModel.totalMonthLessons.observe(viewLifecycleOwner){
            binding.tvLessons.text = "Занятий за месяц : $it"
        }
    }
    fun getPeriodLessons(period: String){
        lessonsStatisticFragmentViewModel.totalPeriodLessons(period).observe(viewLifecycleOwner){
            if (period == "-5 months") {
                binding.tvLessons.text = "Занятий за 6 месяцев : $it"}
           else{binding.tvLessons.text = "Занятий за год : $it"}
        }
        }
}
package com.example.tutor.statistic.lessons

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.tutor.convertLongToTime
import com.example.tutor.databinding.FragmentLessonsStatisticBinding
import com.example.tutor.journal.studentJournal.DBapplication
import com.example.tutor.statistic.DaysOfWeek
import com.example.tutor.statistic.Months
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartModel
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartType
import com.github.aachartmodel.aainfographics.aachartcreator.AASeriesElement
import com.google.android.material.tabs.TabLayout
import java.util.*

class LessonsStatisticFragment : Fragment() {
    lateinit var binding: FragmentLessonsStatisticBinding
    private val lessonsStatisticFragmentViewModel: LessonsStatisticFragmentViewModel by viewModels {
        LessonsStatisticFragmentViewModelFactory((requireActivity().application as DBapplication).scheduleRepository)
    }
    val months = mutableMapOf(
        Months.JANUARY to 0,
        Months.FEBRUARY to 0,
        Months.MARCH to 0,
        Months.APRIL to 0,
        Months.MAY to 0,
        Months.JUNE to 0,
        Months.JULY to 0,
        Months.AUGUST to 0,
        Months.SEPTEMBER to 0,
        Months.OCTOBER to 0,
        Months.NOVEMBER to 0,
        Months.DECEMBER to 0,
    )
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
        weekChart()
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab) {
                if (tab.position ==0){
                    getWeekLessons()
                    weekChart()
                }else if(tab.position ==1){
                    getMonthLessons()
                    monthChart()
                }else if(tab.position ==2){
                    getPeriodLessons("-5 months")
                    sixMonthChart()
                }else if(tab.position ==3){
                    getPeriodLessons("-11 months")
                    yearChart()
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}

            override fun onTabReselected(tab: TabLayout.Tab) {}

        })
    }

    fun chart(dates: Array<String>, lessons: Array<Int>): AAChartModel {
        val aaChartModel: AAChartModel = AAChartModel()
            .chartType(AAChartType.Column)
            .backgroundColor("#FFFFFFFF")
            .yAxisTitle("Количество занятий")
            .dataLabelsEnabled(true)
            .categories(dates)
            .series(arrayOf(
                AASeriesElement()
                    .color("#FF6200EE")
                    .name("Количество")
                    .data(lessons as Array<Any>)
            )
            )
        return aaChartModel
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

    fun weekChart() {
        val mapOfWeek = lessonsStatisticFragmentViewModel.getMapOfWeekLessons()
        var daysOfWeek = mutableMapOf(
            DaysOfWeek.MONDAY to 0,
            DaysOfWeek.TUESDAY to 0,
            DaysOfWeek.WEDNESDAY to 0,
            DaysOfWeek.THURSDAY to 0,
            DaysOfWeek.FRIDAY to 0,
            DaysOfWeek.SATURDAY to 0,
            DaysOfWeek.SUNDAY to 0,
        )
        mapOfWeek.keys.forEach { key ->
            daysOfWeek.put(daysOfWeek.keys.find { it.number == key }!!, mapOfWeek[key]!!)
        }
        val d = daysOfWeek.keys.toMutableList().map { it.rusName }.toTypedArray()
        val l = daysOfWeek.values.toTypedArray()
        binding.lessonsChart.aa_drawChartWithChartModel(chart(d, l))
    }
    fun monthChart(){
        val mapOfMonth = lessonsStatisticFragmentViewModel.getMapOfMonthLessons()
        val d = mapOfMonth.keys.toTypedArray()
        val l = mapOfMonth.values.toTypedArray()
        binding.lessonsChart.aa_drawChartWithChartModel(chart(d,l))
    }
    fun sixMonthChart(){
        val mapOf6Month = lessonsStatisticFragmentViewModel.getMapOfPeriodLessons("-5 months")
        val calendar: Calendar = Calendar.getInstance()
        val cal = calendar.timeInMillis.convertLongToTime("MM").toInt()
        val filterMonths =
            months.filterKeys { (cal - 5L) <= it.number && it.number <= cal }.toMutableMap()
        mapOf6Month.keys.forEach { key ->
            filterMonths.put(filterMonths.keys.find { it.number == key }!!, mapOf6Month[key]!!)
            val d = filterMonths.keys.toMutableList().map { it.rusName }.toTypedArray()
            val l = filterMonths.values.toTypedArray()
            binding.lessonsChart.aa_drawChartWithChartModel(chart(d, l))
        }
    }
    fun yearChart(){
        val mapOfYear = lessonsStatisticFragmentViewModel.getMapOfPeriodLessons("-11 months")
        mapOfYear.keys.forEach { key ->
            months.put(months.keys.find { it.number == key }!!, mapOfYear[key]!!)
        }
        val d = months.keys.toMutableList().map { it.rusName }.toTypedArray()
        val p = months.values.toTypedArray()
        binding.lessonsChart.aa_drawChartWithChartModel(chart(d, p))
    }
}
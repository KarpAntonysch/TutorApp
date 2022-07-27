package com.example.tutor.statistic

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.tutor.databinding.FragmentStatisticBinding
import com.example.tutor.journal.studentJournal.DBapplication
import com.github.aachartmodel.aainfographics.aachartcreator.*
import java.util.*

class StatisticFragment : Fragment() {
    lateinit var binding: FragmentStatisticBinding
    private val statisticFragmentViewModel: StatisticFragmentViewModel by viewModels {
        StatisticFragmentViewModelFactory((requireActivity().application as DBapplication).scheduleRepository)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentStatisticBinding.inflate(inflater, container, false)
        return binding.root
    }

    //MAIN
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val aaChartView = binding.aaChartView
        aaChartView.aa_drawChartWithChartModel(weekChart())
        getTotalWeekAmount()
        getTotalWeekLessons()

    }
    // MAIN!

    // функция для отображения недельного графика из библиотеки AAChartModel
    fun weekChart():AAChartModel{

        val aaChartModel : AAChartModel = AAChartModel()
            .chartType(AAChartType.Column)
            //.title("Деньги")
            //.subtitle("неделя")
            .backgroundColor("#FFFFFFFF")
            .yAxisTitle("Занятия/Рубль")
            .dataLabelsEnabled(true)
            .categories(arrayOf(
                "ПН","ВТ","СР","ЧТ","ПТ","Сб","ВС"
            ))
            .series(arrayOf(
                AASeriesElement()
                    .name("Количесвто занятий")
                    .data(arrayOf(222, 432, 300, 454, 590, 530, 510))

                , AASeriesElement()
                    .name("Заработано")
                    .data(arrayOf(220, 282, 201, 234, 290, 430, 410))

            )
            )
        return aaChartModel
    }

    // получение суммы за неделю
    fun  getTotalWeekAmount(){
        statisticFragmentViewModel.totalWeekAmount.observe(viewLifecycleOwner){
            binding.tvAmount.text = "Заработано: ${it}₽"
        }
    }
    // получение колличества занятий за неделю
    fun getTotalWeekLessons(){
        statisticFragmentViewModel.totalLessons.observe(viewLifecycleOwner){
            binding.tvLessons.text = "Проведено занятий: ${it}"
        }
    }
}
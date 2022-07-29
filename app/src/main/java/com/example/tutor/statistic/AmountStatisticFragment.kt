package com.example.tutor.statistic

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.tutor.databinding.FragmentAmountStatisticBinding
import com.example.tutor.journal.studentJournal.DBapplication
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartModel
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartType
import com.github.aachartmodel.aainfographics.aachartcreator.AASeriesElement
import java.time.chrono.ChronoPeriod

class AmountStatisticFragment : Fragment() {
    lateinit var binding: FragmentAmountStatisticBinding
    private val statisticFragmentViewModel: StatisticFragmentViewModel by viewModels {
        StatisticFragmentViewModelFactory((requireActivity().application as DBapplication).scheduleRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentAmountStatisticBinding.inflate(inflater, container, false)
        return binding.root
    }

    //MAIN
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val aaChartView = binding.aaChartView
               // получение суммы по дням
        fun getAmountByDaysOfWeek(){
            statisticFragmentViewModel.amountByDaysOfWeek.observe(viewLifecycleOwner){
                val d = it!!.map { it.date!! }.toTypedArray()
                val p = it!!.map { it.price!!}.toTypedArray()
                aaChartView.aa_drawChartWithChartModel(weekChart(d,p))
            }
        }
      getPeriodWeekAmount("week")
        getAmountByDaysOfWeek()

        binding.btnWeek.setOnClickListener {
            getPeriodWeekAmount("week")
            getAmountByDaysOfWeek()
        }
        binding.btnMonth.setOnClickListener {
            getPeriodWeekAmount("-1 month")
        }
        binding.btn6Month.setOnClickListener {
            getPeriodWeekAmount("-6 month")
        }
        binding.btnYear.setOnClickListener {
            getPeriodWeekAmount("-1 year")
        }
    }
    // MAIN!

    // функция для отображения недельного графика из библиотеки AAChartModel
    fun weekChart(dates:Array<String>,prices:Array<Int>):AAChartModel{
        val aaChartModel : AAChartModel = AAChartModel()
            .chartType(AAChartType.Column)
            //.title("Доход за неделю")
            //.subtitle("неделя")
            .backgroundColor("#FFFFFFFF")
            .yAxisTitle("Рубль")
            .dataLabelsEnabled(true)
            .categories(dates)
            .series(arrayOf(
                AASeriesElement()
                    .name("Доход")
                    .data(prices as Array<Any>)
            )
            )
        return aaChartModel
    }

    // получение итоговой суммы по периодам
    fun  getPeriodWeekAmount(  period: String){
        if (period == "week"){
            statisticFragmentViewModel.totalWeekAmount .observe(viewLifecycleOwner){
                binding.tvAmount.text = "Доход за неделю : ${it}₽"
        }
        }
        if (period == "-1 month"){
            statisticFragmentViewModel.totalPeriodAmount(period) .observe(viewLifecycleOwner){
               binding.tvAmount.text = "Доход за месяц : ${it}₽"
            }
        }
        if(period == "-6 month"){
            statisticFragmentViewModel.totalPeriodAmount(period) .observe(viewLifecycleOwner){
                binding.tvAmount.text = "Доход за 6 месяцев : ${it}₽"
            }
        }
        if(period == "-1 year"){
            statisticFragmentViewModel.totalPeriodAmount(period) .observe(viewLifecycleOwner){
                binding.tvAmount.text = "Доход за год : ${it}₽"
            }
        }
    }

}
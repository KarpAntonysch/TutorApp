package com.example.tutor.statistic

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.tutor.convertLongToTime
import com.example.tutor.databinding.FragmentAmountStatisticBinding
import com.example.tutor.journal.studentJournal.DBapplication
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartModel
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartType
import com.github.aachartmodel.aainfographics.aachartcreator.AASeriesElement
import java.util.*

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

        // получение суммы по дням
        fun getAmountByDaysOfWeek() {
            statisticFragmentViewModel.amountByDaysOfWeek.observe(viewLifecycleOwner) {
                val d = it!!.map { it.date!! }.toTypedArray()
                val p = it!!.map { it.price!! }.toTypedArray()
                binding.aaChartView.aa_drawChartWithChartModel(chart(d, p))
            }
        }
        // недельный график при переходе на фрагмент
        getWeekAmount()
        getAmountByDaysOfWeek()

        binding.btnWeek.setOnClickListener {
            getWeekAmount()
            getAmountByDaysOfWeek()
        }
        binding.btnMonth.setOnClickListener {
            statisticFragmentViewModel.totalMonthAmount.observe(viewLifecycleOwner) {
                binding.tvAmount.text = "Доход за месяц : ${it}₽"
            }
        }
        binding.btn6Month.setOnClickListener {
            statisticFragmentViewModel.totalPeriodAmount("-5 months").observe(viewLifecycleOwner) {
                binding.tvAmount.text = "Доход за 6 месяцев : ${it}₽"
                sixMomthChart()
            }
        }
        binding.btnYear.setOnClickListener {
            statisticFragmentViewModel.totalPeriodAmount("-11 months").observe(viewLifecycleOwner) {
                binding.tvAmount.text = "Доход за год : ${it}₽"
                yearChart()
            }


        }
    }
    // MAIN!

    // функция для отображения  графика из библиотеки AAChartModel
    fun chart(dates: Array<String>, prices: Array<Int>): AAChartModel {
        val aaChartModel: AAChartModel = AAChartModel()
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

    // сумма за неделю
    fun getWeekAmount() {
        statisticFragmentViewModel.totalWeekAmount.observe(viewLifecycleOwner) {
            binding.tvAmount.text = "Доход за неделю : ${it}₽"
        }
    }

    fun sixMomthChart() {
        // Map для месяцов графика с ключами-месяцами и пустыми значениями-доходами
        val twelveMonthsForSix = mutableMapOf(
            1L to 0,
            2L to 0,
            3L to 0,
            4L to 0,
            5L to 0,
            6L to 0,
            7L to 0,
            8L to 0,
            9L to 0,
            10L to 0,
            11L to 0,
            12L to 0
        )
        val mapOfPrice = statisticFragmentViewModel.getMapOfPrice("-5 month")
        val calendar: Calendar = Calendar.getInstance()
        val cal = calendar.timeInMillis.convertLongToTime("MM").toLong()
        val sixMonthFilter: MutableMap<Long, Int> =
            twelveMonthsForSix.filterKeys { (cal - 5L) <= it && it <= cal } as MutableMap<Long, Int>
        sixMonthFilter.putAll(mapOfPrice)
        val d = sixMonthFilter.keys.toMutableList().map { it.toString() }.toTypedArray()
        val p = sixMonthFilter.values.toTypedArray()
        binding.aaChartView.aa_drawChartWithChartModel(chart(d, p))
    }

    fun yearChart() {
        // Map для месяцов графика с ключами-месяцами и пустыми значениями-доходами
        val twelveMonthsForSix = mutableMapOf(
            1L to 0,
            2L to 0,
            3L to 0,
            4L to 0,
            5L to 0,
            6L to 0,
            7L to 0,
            8L to 0,
            9L to 0,
            10L to 0,
            11L to 0,
            12L to 0
        )
        // получили MAP с ключами-месяцами и значениями-доходами из БД
        val mapOfPrice = statisticFragmentViewModel.getMapOfPrice("-11 month")
        // обновили twelveMonths Map - значениями mapOfPrice
        twelveMonthsForSix.putAll(mapOfPrice)
        val d = twelveMonthsForSix.keys.toMutableList().map { it.toString() }.toTypedArray()
        val p = twelveMonthsForSix.values.toTypedArray()
        binding.aaChartView.aa_drawChartWithChartModel(chart(d, p))
    }
}


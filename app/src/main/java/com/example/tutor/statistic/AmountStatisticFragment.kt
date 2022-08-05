package com.example.tutor.statistic

import android.os.Bundle
import android.util.Log
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
import kotlinx.coroutines.flow.forEach
import kotlinx.coroutines.flow.map
import java.util.*

class AmountStatisticFragment : Fragment() {
    lateinit var binding: FragmentAmountStatisticBinding
    private val statisticFragmentViewModel: StatisticFragmentViewModel by viewModels {
        StatisticFragmentViewModelFactory((requireActivity().application as DBapplication).scheduleRepository)
    }
    //Map из enam класса, где константы класса - ключи. значения - нули, для перезаписи их значениями сумм из БД
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
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentAmountStatisticBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // недельный график при переходе на фрагмент
        getWeekAmount()
        weekChart()

        binding.btnWeek.setOnClickListener {
            getWeekAmount()
            weekChart()
        }
        binding.btnMonth.setOnClickListener {
            statisticFragmentViewModel.totalMonthAmount.observe(viewLifecycleOwner) {
                binding.tvAmount.text = "Доход за месяц : ${it}₽"
            }
            monthChart()
        }
        binding.btn6Month.setOnClickListener {
            statisticFragmentViewModel.totalPeriodAmount("-5 months").observe(viewLifecycleOwner) {
                binding.tvAmount.text = "Доход за 6 месяцев : ${it}₽"
            }
            sixMomthChart()
        }
        binding.btnYear.setOnClickListener {
            statisticFragmentViewModel.totalPeriodAmount("-11 months").observe(viewLifecycleOwner) {
                binding.tvAmount.text = "Доход за год : ${it}₽"
            }
            yearChart()
        }
    }


    // функция для отображения  графика из библиотеки AAChartModel
    fun chart(dates: Array<String>, prices: Array<Int>): AAChartModel {
        val aaChartModel: AAChartModel = AAChartModel()
            .chartType(AAChartType.Column)
            //.title("Доход за неделю")
            //.subtitle("неделя")
            .backgroundColor("#FFFFFFFF")
            .yAxisTitle("Доход, ₽")
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
    fun weekChart(){
        val mapOfWeek = statisticFragmentViewModel.getMapOfWeek()
        var daysOfWeek = mutableMapOf(
            DaysOfWeek.MONDAY to 0,
            DaysOfWeek.TUESDAY to 0,
            DaysOfWeek.WEDNESDAY to 0,
            DaysOfWeek.THURSDAY to 0,
            DaysOfWeek.FRIDAY to 0,
            DaysOfWeek.SATURDAY to 0,
            DaysOfWeek.SUNDAY to 0,
        )
        mapOfWeek.keys.forEach{ key ->
        daysOfWeek.put(daysOfWeek.keys.find { it.number == key }!!, mapOfWeek[key]!!)
        }
        val d = daysOfWeek.keys.toMutableList().map {it.rusName }.toTypedArray()
        val p = daysOfWeek.values.toTypedArray()
        binding.aaChartView.aa_drawChartWithChartModel(chart(d,p))
    }

    fun monthChart(){
        val mapOfMonth = statisticFragmentViewModel.getMapOfMonth()
        Log.v("e","${mapOfMonth.keys}")
        val d = mapOfMonth.keys.toTypedArray()
        val p = mapOfMonth.values.toTypedArray()
        binding.aaChartView.aa_drawChartWithChartModel(chart(d,p))
    }

    fun sixMomthChart() {

        val mapOfPrice = statisticFragmentViewModel.getMapOfYear("-5 months")
        // создаем календарь,для определения текущего месяца и последующей фильтрации 6 месяцев от текущего
        val calendar: Calendar = Calendar.getInstance()
        val cal = calendar.timeInMillis.convertLongToTime("MM").toInt()
        // фильтрация map`а по ключам до полугода
        val filterMonths = months.filterKeys { (cal - 5L) <= it.number && it.number <=cal }.toMutableMap()
        // перезапись значений для одинаковых ключей
        mapOfPrice.keys.forEach{ key ->
            filterMonths.put(filterMonths.keys.find { it.number == key}!!,mapOfPrice[key]!!)
        }
        val d = filterMonths.keys.toMutableList().map { it.rusName }.toTypedArray()
        val p = filterMonths.values.toTypedArray()
        binding.aaChartView.aa_drawChartWithChartModel(chart(d, p))

    }


    fun yearChart() {

        // получили MAP с ключами-месяцами и значениями-доходами из БД
        val mapOfPrice = statisticFragmentViewModel.getMapOfYear("-11 months")
        //  для каждого ключа в mapOfPrice: к map month добавляем значение по ключу mapOfMonth !=0 ( mapOfPrice[key]!!)
        // при условии, что номер ключа month(т.е. номер месяца  enam класса) совпадает с ключом mapOfPrice. При добавлении
        // значения обновляются автоматически для равных ключей. если ключи не равны, то значение остается 0
        mapOfPrice.keys.forEach { key ->
            months.put(months.keys.find { it.number == key }!!, mapOfPrice[key]!!)
        }
        val d = months.keys.toMutableList().map { it.rusName }.toTypedArray()
        val p = months.values.toTypedArray()
        binding.aaChartView.aa_drawChartWithChartModel(chart(d, p))
    }

}


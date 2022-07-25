package com.example.tutor.statistic

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tutor.R
import com.example.tutor.convertLongToTime
import com.example.tutor.databinding.FragmentMainBinding
import com.example.tutor.databinding.FragmentStatisticBinding
import com.github.aachartmodel.aainfographics.aachartcreator.*
import java.util.*

class StatisticFragment : Fragment() {
    lateinit var binding: FragmentStatisticBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentStatisticBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val aaChartView = binding.aaChartView
        aaChartView.aa_drawChartWithChartModel(weekChart())
        val currentDate =getCurrentTime()
        binding.textView.text = currentDate.convertLongToTime("dd.MM.yyyy")
    }

    fun getCurrentTime(): Long {
        val cal: Calendar = Calendar.getInstance()
        return cal.timeInMillis
    }
    // функция для отображения недельного графика из библиотеки AAChartModel
    fun weekChart():AAChartModel{
        val aaChartModel : AAChartModel = AAChartModel()
            .chartType(AAChartType.Column)
            //.title("Деньги")
            //.subtitle("неделя")
            .backgroundColor("#FFFFFFFF")
            .yAxisTitle("Рубль")
            .dataLabelsEnabled(true)
            .categories(arrayOf(
                "qq","ww","ee","rr","xx","aa","ss"
            ))
            .series(arrayOf(
                AASeriesElement()
                    .name("Количесвто занятий")
                    .data(arrayOf(2000, 432, 3000, 454, 590, 530, 510))
                    .step("right")//
                , AASeriesElement()
                    .name("Заработано")
                    .data(arrayOf(220, 282, 201, 234, 290, 430, 410))
                    .step("center")//设置折线样式为直方折线,折线连接点位置居中
            )
            )
        return aaChartModel
    }
}
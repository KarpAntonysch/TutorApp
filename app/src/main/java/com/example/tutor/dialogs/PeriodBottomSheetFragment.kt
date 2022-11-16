package com.example.tutor.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.example.tutor.R
import com.example.tutor.databinding.NotificationBottomSheetLayoutBinding
import com.example.tutor.databinding.PeriodBottomSheetFragmentBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

private const val COLLAPSED_HEIGHT = 500

class PeriodBottomFragment(private val periodListener: PeriodListener) : BottomSheetDialogFragment() {

    lateinit var binding: PeriodBottomSheetFragmentBinding

    override fun getTheme() = R.style.AppBottomSheetDialogTheme

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding =
            PeriodBottomSheetFragmentBinding.bind(inflater.inflate(R.layout.period_bottom_sheet_fragment, container))
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        // плотность пикселей на дюйм для красивого масштабирования
        val density = requireContext().resources.displayMetrics.density

        dialog?.let {
            // Находим сам bottomSheet и достаём из него Behaviour
            val bottomSheet =
                it.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout
            val behavior = BottomSheetBehavior.from(bottomSheet)

            // Выставляем высоту для состояния collapsed и выставляем состояние collapsed
            behavior.peekHeight = (COLLAPSED_HEIGHT * density).toInt()
            behavior.state = BottomSheetBehavior.STATE_COLLAPSED

        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding){
            periodDay.setOnClickListener {
                periodListener.period0()
                dialog?.dismiss()
            }
            periodWeek.setOnClickListener {
                periodListener.period1()
                dialog?.dismiss()
            }
            monthPeriod.setOnClickListener {
                periodListener.period2()
                dialog?.dismiss()
            }
            halfYearPeriod.setOnClickListener {
                periodListener.period3()
                dialog?.dismiss()
            }
            yearPeriod.setOnClickListener {
                periodListener.period4()
                dialog?.dismiss()
            }
        }
    }
}
interface PeriodListener{
    fun period0()
    fun period1()
    fun period2()
    fun period3()
    fun period4()
}



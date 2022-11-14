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

class PeriodBottomFragment : BottomSheetDialogFragment() {

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
        val item11 = binding.periodItem1
        val item12 = binding.periodItem2
        val item13 = binding.periodItem3
        val item14 = binding.periodItem4

        item11.setOnClickListener {
            dialog?.dismiss()
        }
        item12.setOnClickListener {
            dialog?.dismiss()
        }
        item13.setOnClickListener {
            dialog?.dismiss()
        }
        item14.setOnClickListener {
            dialog?.dismiss()
        }

    }
}



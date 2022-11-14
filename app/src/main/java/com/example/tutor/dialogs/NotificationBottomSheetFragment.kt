package com.example.tutor.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.example.tutor.R
import com.example.tutor.databinding.NotificationBottomSheetLayoutBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

private const val COLLAPSED_HEIGHT = 500

class NotificationBottomFragment(private val bottomSheetListener: BottomSheetListener) : BottomSheetDialogFragment() {
    lateinit var binding: NotificationBottomSheetLayoutBinding

    override fun getTheme() = R.style.AppBottomSheetDialogTheme

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding =
            NotificationBottomSheetLayoutBinding.bind(inflater.inflate(R.layout.notification_bottom_sheet_layout, container))
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
            tenMinutes.setOnClickListener {
                bottomSheetListener.ac1()
                dialog?.dismiss()
            }
            fifteensMinutes.setOnClickListener {
                bottomSheetListener.ac2()
                dialog?.dismiss()
            }
            thirteensMinutes.setOnClickListener {
                bottomSheetListener.ac3()
                dialog?.dismiss()
            }
            cancelNotifications.setOnClickListener {
                bottomSheetListener.ac4()
                dialog?.dismiss()
            }
        }

    }
}

interface BottomSheetListener{
    fun ac1()
    fun ac2()
    fun ac3()
    fun ac4()
}



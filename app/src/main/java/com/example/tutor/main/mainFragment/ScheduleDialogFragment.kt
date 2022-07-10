package com.example.tutor.main.mainFragment

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment

class ScheduleDialogFragment : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val listener = DialogInterface.OnClickListener {_, that ->
            parentFragmentManager.setFragmentResult(REQUEST_KEY, bundleOf(KEY_RESPONSE to that))
        }
        return AlertDialog.Builder(requireContext())
            .setCancelable(false)
            .setTitle("Вы действительно хотите удалить ?")
            .setPositiveButton("да", listener)
            .setNegativeButton("нет", listener)
            .create()
    }

    companion object {
        @JvmStatic
        val TAG = ScheduleDialogFragment::class.java.simpleName
        @JvmStatic
        val REQUEST_KEY = "$TAG:defaultRequestKey1"
        @JvmStatic
        val KEY_RESPONSE = "RESPONSE1"
    }
}
package com.example.tutor.journal.studentJournal

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import com.example.tutor.R

class JourmalDialogFragment : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val listener = DialogInterface.OnClickListener { _, which ->
            parentFragmentManager.setFragmentResult(REQUEST_KEY, bundleOf(KEY_RESPONSE to which))
        }
        return AlertDialog.Builder(requireContext())
            .setCancelable(true)
             .setTitle("Вы действительно хотите удалить ?")
            //.setMessage("Вы действительно хотите удалить ?")
            .setPositiveButton("да", listener)
            .setNegativeButton("нет", listener)
            .create()
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)

    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)

    }

    companion object {
        @JvmStatic val TAG = JourmalDialogFragment::class.java.simpleName
        @JvmStatic val REQUEST_KEY = "$TAG:defaultRequestKey"
        @JvmStatic val KEY_RESPONSE = "RESPONSE"
    }
}
package com.example.tutor.journal.studentJournal

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment

class JournalDialogFragment : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val listener = DialogInterface.OnClickListener { _, which ->
            parentFragmentManager.setFragmentResult(REQUEST_KEY, bundleOf(KEY_RESPONSE to which))
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
        val TAG = JournalDialogFragment::class.java.simpleName
        @JvmStatic
        val REQUEST_KEY = "$TAG:defaultRequestKey"
        @JvmStatic
        val KEY_RESPONSE = "RESPONSE"
    }
}
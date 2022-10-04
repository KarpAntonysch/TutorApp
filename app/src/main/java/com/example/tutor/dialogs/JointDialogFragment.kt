package com.example.tutor.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.widget.Button
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment

class JointDialogFragment(val title:Int, private val showButton: Boolean,
                          private val posText:Int, private val negText:Int,
                          private val description: Int, private val setMessage:Boolean) : DialogFragment() {
    lateinit var alertDialog:AlertDialog
    override fun onStart() {
        super.onStart()
        val d = dialog as AlertDialog?
        if (d != null) {
            val negativeButton = d.getButton(Dialog.BUTTON_NEGATIVE) as Button
            negativeButton.isEnabled = showButton
        }
    }
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val listener = DialogInterface.OnClickListener { _, which ->
            parentFragmentManager.setFragmentResult(REQUEST_KEY, bundleOf(KEY_RESPONSE to which))
        }


        if (setMessage){
            alertDialog = AlertDialog.Builder(requireContext())
                .setCancelable(false)
                .setTitle(title)
                .setMessage(description)
                .setPositiveButton(posText, listener)
                .setNegativeButton(negText, listener)
                .create()
           } else{
            alertDialog = AlertDialog.Builder(requireContext())
                .setCancelable(false)
                .setTitle(title)
                .setPositiveButton(posText, listener)
                .setNegativeButton(negText, listener)
                .create()
           }
        return alertDialog
    }

    companion object {
        @JvmStatic
        val TAG = JointDialogFragment::class.java.simpleName
        @JvmStatic
        val REQUEST_KEY = "$TAG:defaultRequestKey"
        @JvmStatic
        val KEY_RESPONSE = "RESPONSE"
    }
}
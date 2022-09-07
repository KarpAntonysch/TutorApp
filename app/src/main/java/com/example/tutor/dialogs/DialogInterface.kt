package com.example.tutor.dialogs

import androidx.fragment.app.FragmentManager

interface DialogInterface {
    fun showDialogFragment(manager:FragmentManager,hint:Int){
        val dialogFragment = InfoDialogFragment("Подсказка",hint)
        dialogFragment.show(manager, InfoDialogFragment.TAG)
    }
}
package com.example.tutor.dialogs

import androidx.fragment.app.FragmentManager

interface JointDialogInterface {
    fun showJoinDialog(title:Int, showButton:Boolean, posText:Int, negText:Int,
                       manager: FragmentManager, description:Int, setMessage:Boolean){
        val dialogFragment = JointDialogFragment(title,showButton,posText,negText,description,setMessage)
        dialogFragment.show(manager, JointDialogFragment.TAG)
    }
}
package com.example.tutor.journal

import android.os.Binder
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tutor.R
import com.example.tutor.databinding.FragmentAddStudentToJournalBinding

class AddStudentToJournal : Fragment() {
lateinit var binding: FragmentAddStudentToJournalBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentAddStudentToJournalBinding.inflate(inflater,container,false)
        return binding.root
    }

}
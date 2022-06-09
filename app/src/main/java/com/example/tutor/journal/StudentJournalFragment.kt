package com.example.tutor.journal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.tutor.R
import com.example.tutor.databinding.FragmentStudentJournalBinding

class StudentJournalFragment : Fragment() {
    lateinit var binding: FragmentStudentJournalBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentStudentJournalBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.addStudent.setOnClickListener{
            findNavController().navigate(R.id.action_studentJournalFragment_to_addStudentToJournal)
        }

    }

}
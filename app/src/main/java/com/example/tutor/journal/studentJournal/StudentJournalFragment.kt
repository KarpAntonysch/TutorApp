package com.example.tutor.journal.studentJournal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.tutor.R
import com.example.tutor.adapters.StudentJournalAdapter
import com.example.tutor.databinding.FragmentStudentJournalBinding

class StudentJournalFragment : Fragment() {
    lateinit var binding: FragmentStudentJournalBinding
    lateinit var recyclerView:RecyclerView
    lateinit var adapter :StudentJournalAdapter



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
        binding.btnAddStudent.setOnClickListener {
            findNavController().navigate(R.id.action_studentJournalFragment_to_addStudentToJournal)
        }

    }

}
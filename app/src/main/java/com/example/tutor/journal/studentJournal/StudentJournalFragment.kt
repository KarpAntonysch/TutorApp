package com.example.tutor.journal.studentJournal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.tutor.R
import com.example.tutor.adapters.StudentJournalAdapter
import com.example.tutor.bd.StudentEntity
import com.example.tutor.databinding.FragmentStudentJournalBinding
import com.example.tutor.journal.StudentJournalViewModel
import com.example.tutor.journal.StudentJournalViewModelFactory



class StudentJournalFragment : Fragment(),StudentJournalAdapter.Listener {
    lateinit var binding: FragmentStudentJournalBinding
    lateinit var recyclerView: RecyclerView
    private var adapter=StudentJournalAdapter(this)
    private val studentJournalViewModel: StudentJournalViewModel by viewModels {
        StudentJournalViewModelFactory((requireActivity().application as StudentApplication).repository)
    }
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
        realizationOfRV()


    }
    private fun realizationOfRV(){
        recyclerView=binding.allStudentsRV
        recyclerView.adapter=adapter
        studentJournalViewModel.allStudents.observe(viewLifecycleOwner)
            {studentList ->
                studentList.let { adapter.submitList(it) }
            }
    }
    
    override fun onClick(studentEntity: StudentEntity) {
        studentJournalViewModel.deleteStudent(studentEntity)
    }
}
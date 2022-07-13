package com.example.tutor.journal.studentJournal.pager.inactiveStudents

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tutor.R
import com.example.tutor.adapters.InactiveJournalAdapter
import com.example.tutor.bd.entities.StudentEntity
import com.example.tutor.databinding.FragmentInactiveStudentsBinding


class InactiveStudentsFragment : Fragment(),InactiveJournalAdapter.Listener {
    lateinit var binding: FragmentInactiveStudentsBinding
    lateinit var recyclerView : RecyclerView
    val adapter = InactiveJournalAdapter(this)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
         binding = FragmentInactiveStudentsBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

    private fun realizationOfInactiveRV(){
        recyclerView = binding.allInactiveStudentsRV
        recyclerView.adapter = adapter

    }
    override fun returnStudent(studentEntity: StudentEntity) {
        TODO("Not yet implemented")
    }

}
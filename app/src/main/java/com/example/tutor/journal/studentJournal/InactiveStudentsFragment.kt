package com.example.tutor.journal.studentJournal

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tutor.R
import com.example.tutor.databinding.FragmentInactiveStudentsBinding


class InactiveStudentsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val binding = FragmentInactiveStudentsBinding.inflate(inflater,container,false)
        return binding.root
    }


}
package com.example.tutor.journal.studentJournal.pager

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import com.example.tutor.R
import com.example.tutor.adapters.JournalVPAdapter
import com.example.tutor.bd.entities.StudentEntity
import com.example.tutor.databinding.FragmentJornalPagerBinding
import com.example.tutor.journal.StudentJournalViewModel
import com.example.tutor.journal.StudentJournalViewModelFactory
import com.example.tutor.journal.studentJournal.DBapplication
import com.example.tutor.journal.studentJournal.pager.activeStudents.JournalActionModeCallback
import com.google.android.material.tabs.TabLayoutMediator

class JornalPagerFragment : Fragment(), JournalActionModeCallback.ActionModeListener {
    lateinit var binding: FragmentJornalPagerBinding
    lateinit var actionMode: JournalActionModeCallback
    private val studentJournalViewModel: StudentJournalViewModel by activityViewModels {
        StudentJournalViewModelFactory((requireActivity().application as DBapplication).studentRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentJornalPagerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setPagerAndTab()
        Log.v("aaa",
            "1${studentJournalViewModel.actionModeVM.value},${studentJournalViewModel.student.value} onViewCreated JornalPagerFragment")
        studentJournalViewModel.actionModeVM.observe(viewLifecycleOwner) { acshn ->
            if (acshn == true) {
                Log.v("aaa",
                    "1${studentJournalViewModel.actionModeVM.value},1${studentJournalViewModel.student.value}")
                actionMode =
                    JournalActionModeCallback(studentJournalViewModel.student)
                actionMode.startActionMode(view!!, R.menu.journal_app_menu, this)
            }

        }
    }

    fun setPagerAndTab() {
        binding.journalViewPager.adapter = JournalVPAdapter(this)// подключл адаптер пейджера
        // подключил TabLayout
        TabLayoutMediator(binding.journalTabLayout, binding.journalViewPager) { tab, position ->
            when (position) {
                0 -> {
                    tab.setText("Журнал")
                }
                else -> tab.setText("Выпускники")
            }
        }.attach()
    }

    override fun clickToMenuDelete(studentEntity: MutableLiveData<StudentEntity>?) {
        TODO("Not yet implemented")
    }
}

package com.example.tutor.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.tutor.journal.studentJournal.pager.inactiveStudents.InactiveStudentsFragment
import com.example.tutor.journal.studentJournal.pager.activeStudents.StudentJournalFragment

class JournalVPAdapter(fragment: Fragment) : FragmentStateAdapter(fragment){
    // устанавливаю количество табов
    override fun getItemCount(): Int = 2
    // определяю фрагменты для табов
    override fun createFragment(position: Int): Fragment {
    return when(position){
        0 -> StudentJournalFragment()
        else -> InactiveStudentsFragment()
    }
    }
}
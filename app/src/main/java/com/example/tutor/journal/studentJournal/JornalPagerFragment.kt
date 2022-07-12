package com.example.tutor.journal.studentJournal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.tutor.adapters.JournalVPAdapter
import com.example.tutor.databinding.FragmentJornalPagerBinding
import com.google.android.material.tabs.TabLayoutMediator

class JornalPagerFragment : Fragment() {
    lateinit var binding: FragmentJornalPagerBinding
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
}

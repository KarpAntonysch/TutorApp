package com.example.tutor.journal.studentJournal.pager

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.tutor.R
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
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val toolbar = binding.journalToolbar
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as AppCompatActivity).supportActionBar?.title = "Журнал"
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.pages_tool_bar, menu)
    }

    // слушатель айтемов меню ToolBar
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.info -> {
                var toast = Toast.makeText(requireContext(), "журнал", Toast.LENGTH_SHORT).show()
            }
        }
        return true
    }
}

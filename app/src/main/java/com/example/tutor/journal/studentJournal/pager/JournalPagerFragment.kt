package com.example.tutor.journal.studentJournal.pager

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.tutor.R
import com.example.tutor.adapters.JournalVPAdapter
import com.example.tutor.databinding.FragmentJournalPagerBinding
import com.example.tutor.dialogs.DialogInterface
import com.google.android.material.tabs.TabLayoutMediator

class JournalPagerFragment : Fragment(),DialogInterface {
    lateinit var binding: FragmentJournalPagerBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentJournalPagerBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolBarSetting()
        setPagerAndTab()
    }
    private fun toolBarSetting(){
        val toolbar = binding.journalToolbar
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as AppCompatActivity).supportActionBar?.title = "Журнал"
    }
    private fun setPagerAndTab() {
        binding.journalViewPager.adapter = JournalVPAdapter(this)// подключл адаптер пейджера
        // подключил TabLayout
        TabLayoutMediator(binding.journalTabLayout, binding.journalViewPager) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = "Журнал"
                }
                else -> tab.text = "Выпускники"
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
                showDialogFragment(childFragmentManager,R.string.journalFragmentDialog)
            }
        }
        return true
    }
}

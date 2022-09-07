package com.example.tutor.journal.studentJournal.pager.activeStudents.watchingTheStudent

import WatchingStudentAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.example.tutor.dialogs.DialogInterface
import com.example.tutor.R
import com.example.tutor.bd.entities.StudentEntity
import com.example.tutor.databinding.FragmentWatchingStudentBinding
import com.example.tutor.journal.studentJournal.DBapplication


class WatchingStudentFragment : Fragment(), DialogInterface {
   lateinit var binding: FragmentWatchingStudentBinding
   lateinit var recyclerView: RecyclerView
   private val adapter = WatchingStudentAdapter()
   private val watchingStudentViewModel:WatchingStudentViewModel by viewModels {
       WatchingStudentViewModelFactory((requireActivity().application as DBapplication).scheduleRepository)
   }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWatchingStudentBinding.inflate(layoutInflater,container,false)
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbarSetting()
        fillTheLayout()
        realizationOfRecyclerView()
    }

    private fun realizationOfRecyclerView(){
        recyclerView = binding.watchRV
        recyclerView.adapter = adapter
        watchingStudentViewModel.getStudentLessons(getStudentEntity()!!.id).observe(viewLifecycleOwner){
            listOfLessons ->
                listOfLessons.let { adapter.submitList(it.sortedBy {it}) }
        }
    }
    private fun fillTheLayout() = with(binding){
        watchTvName.text = getStudentEntity()?.firstName
        watchTvSndName.text = getStudentEntity()?.secondName
        watchTvClass.text = getStudentEntity()?.schoolClass.toString()
        watchTvPrice.text = "${getStudentEntity()?.price}₽"
    }
    private fun getStudentEntity() : StudentEntity?{
        return arguments?.getParcelable("ArgForWatching: com.example.tutor.bd.entities.StudentEntity")
    }
    private fun toolbarSetting(){
        val toolbar = binding.watchToolBar
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.title = "Просмотр"
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> activity?.onBackPressed()
            R.id.info -> {
                showDialogFragment(childFragmentManager,R.string.watchStudent)//Метод из DialogInterface
            }
        }
        return true
    }
}
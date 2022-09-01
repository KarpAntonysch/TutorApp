package com.example.tutor.journal.studentJournal.pager.activeStudents.updatingTheStudent

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import com.example.tutor.InfoDialogFragment
import com.example.tutor.R
import com.example.tutor.bd.entities.StudentEntity
import com.example.tutor.databinding.FragmentEditStudentBinding
import com.example.tutor.journal.studentJournal.DBapplication
import com.example.tutor.journal.studentJournal.pager.activeStudents.EditStudentViewModel
import com.example.tutor.journal.studentJournal.pager.activeStudents.EditStudentViewModelFactory
import com.example.tutor.toEditable



class EditStudentFragment : Fragment() {
    lateinit var binding: FragmentEditStudentBinding
    private val editStudentViewModel: EditStudentViewModel by viewModels {
        EditStudentViewModelFactory((requireActivity().application as DBapplication).studentRepository)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
     binding = FragmentEditStudentBinding.inflate(inflater,container,false)
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        toolBarSetting()
        fillTheLayout()
        binding.btnEditDB.setOnClickListener {
            updateStudent()
            Log.v("update","${updateStudent()}")
            activity?.onBackPressed()
        }
    }


    private fun toolBarSetting(){
        val toolbar = binding.editToolBar
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)//стреклка назад
        (activity as AppCompatActivity).supportActionBar?.title = "Редактирование"
    }
    // меню ToolBar
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.pages_tool_bar, menu)
    }
    // кнопки меню  toolbar
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> activity?.onBackPressed()
            R.id.info -> {
                showDialogFragment()
            }
        }
        return true
    }
    private fun getStudentEntity(): StudentEntity? {
        return arguments?.getParcelable("ArgForStudentEditing: com.example.tutor.bd.entities.StudentEntity")
    }
    private fun fillTheLayout(){
        binding.editFirstName.text = getStudentEntity()?.firstName?.toEditable()
        binding.editSecondName.text = getStudentEntity()?.secondName?.toEditable()
        binding.editClass.text = getStudentEntity()?.schoolClass?.toEditable()
        binding.editPrice.text = getStudentEntity()?.price?.toEditable()
    }
    private fun updateStudent(){
        val studentID = getStudentEntity()?.id
        val firstName = binding.editFirstName.text.toString()
        val secondName = binding.editSecondName.text.toString()
        val schoolClass = binding.editClass.text.toString().toInt()
        val price = binding.editPrice.text.toString().toInt()
        editStudentViewModel.updateStudent(studentID!!,firstName,secondName,schoolClass,price)
    }
    private fun showDialogFragment(){
        val dialogFragment = InfoDialogFragment("Подсказка",R.string.editStudentDialog)
        dialogFragment.show(childFragmentManager,InfoDialogFragment.TAG)
    }
}
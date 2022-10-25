package com.example.tutor.journal.studentJournal.pager.activeStudents.updatingTheStudent

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.tutor.R
import com.example.tutor.bd.entities.StudentEntity
import com.example.tutor.databinding.FragmentEditStudentBinding
import com.example.tutor.dialogs.JointDialogInterface
import com.example.tutor.fireBase.FireBaseRepository
import com.example.tutor.journal.studentJournal.DBapplication
import com.example.tutor.toEditable


class EditStudentFragment : Fragment(),JointDialogInterface {
    lateinit var binding: FragmentEditStudentBinding
    private val editStudentViewModel: EditStudentViewModel by viewModels {
        EditStudentViewModelFactory((requireActivity().application as DBapplication).studentRepository,
            FireBaseRepository())
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
                showJoinDialog(R.string.hint,false,R.string.good,R.string.empty,
                    childFragmentManager,R.string.editStudentDialog,true)
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
        binding.edPhoneNumber.text = getStudentEntity()?.phoneNumber?.toEditable()
    }
    private fun updateStudent(){
        val student = getStudentEntity()
        val firstName = binding.editFirstName.text.toString()
        val secondName = binding.editSecondName.text.toString()
        val schoolClass = binding.editClass.text.toString().toInt()
        val price = binding.editPrice.text.toString().toInt()
        val phoneNumber = binding.edPhoneNumber.text.toString()
        editStudentViewModel.updateStudent(student!!,firstName,secondName,schoolClass,price,phoneNumber)
    }

}
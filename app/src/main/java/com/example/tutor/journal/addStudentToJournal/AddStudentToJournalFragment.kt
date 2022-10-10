package com.example.tutor.journal.addStudentToJournal

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.tutor.R
import com.example.tutor.bd.entities.StudentEntity
import com.example.tutor.databinding.FragmentAddStudentToJournalBinding
import com.example.tutor.dialogs.JointDialogInterface
import com.example.tutor.fireBase.FireBaseRepository
import com.example.tutor.journal.studentJournal.DBapplication

class AddStudentToJournalFragment : Fragment(),JointDialogInterface {
lateinit var binding: FragmentAddStudentToJournalBinding
    private val studentViewModel: AddStudentToJournalViewModel by viewModels {
        AddStudentToJournalViewModelFactory((requireActivity().application as DBapplication).studentRepository,
            FireBaseRepository()
        )
    }
    private var firstStudentCheck:Int = 0 /*Проверка для добавления первого ученика, если переход
    совершен с mainFragment то при добавлении возврат туда же, если с StudentJournalFragment , то на него*/
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding= FragmentAddStudentToJournalBinding.inflate(inflater,container,false)
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        toolbarSetting()
        firstStudentCheck = firstStudentChecking()
        Log.v("qsq","${firstStudentChecking()}")
        binding.btnAddToDB.setOnClickListener{
            if (!empty()){
                val studentEntity = getStudentValues()
                addStudentEntityToDB(studentEntity)
                    Toast.makeText(requireContext(), "Добавлено", Toast.LENGTH_SHORT).show()
                    if (firstStudentCheck == 0){
                        activity?.onBackPressed()
                    }else{
                        findNavController().navigate(R.id.action_addStudentToJournalFragment_to_mainFragment)
                    }


                firstStudentCheck = 0
            }
        }
    }
    private fun firstStudentChecking():Int{
        val studentCheck = arguments?.getInt("ArgForStudent")
        return studentCheck!!
    }
    private fun toolbarSetting(){
        val toolbar = binding.addStudentToolbar
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)//стреклка назад
        (activity as AppCompatActivity).supportActionBar?.title = "Добавление ученика"
    }

    private fun addStudentEntityToDB(studentEntity: StudentEntity){
       studentViewModel.insert(studentEntity)
    }

    private fun getStudentValues():StudentEntity{
        val firstName:String = binding.edFirstName.text.toString()
        val secondName:String = binding.edSecondName.text.toString()
        val schoolClass:Int = binding.edClass.text.toString().toInt()
        val price:Int = binding.edPrice.text.toString().toInt()
        return StudentEntity(firstName, secondName, price, schoolClass)
    }
    // Проверка на заполнение полей
    private fun empty():Boolean{
        binding.apply {
            val warning = R.string.emptyWarning
            if (edFirstName.text.isNullOrEmpty())edFirstName.error= getString(warning)
            if (edSecondName.text.isNullOrEmpty())edSecondName.error=getString(warning)
            if (edClass.text.isNullOrEmpty())edClass.error=getString(warning)
            if (edPrice.text.isNullOrEmpty())edPrice.error=getString(warning)
            return edFirstName.text.isNullOrEmpty() || edSecondName.text.isNullOrEmpty() ||
                    edClass.text.isNullOrEmpty() || edPrice.text.isNullOrEmpty()
        }
    }
    // меню ToolBar
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.pages_tool_bar, menu)
    }

    // слушатель айтемов меню ToolBar
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> activity?.onBackPressed()
            R.id.info -> {
               // showDialogFragment(childFragmentManager,R.string.addStudentToJournal)
                showJoinDialog(R.string.hint,false,R.string.good,R.string.empty,
                childFragmentManager,R.string.addStudentToJournal,true)
            }
        }
        return true
    }
}
package com.example.tutor.journal.addStudentToJournal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.tutor.bd.entities.StudentEntity
import com.example.tutor.databinding.FragmentAddStudentToJournalBinding
import com.example.tutor.journal.studentJournal.DBlication

class AddStudentToJournalFragment : Fragment() {
lateinit var binding: FragmentAddStudentToJournalBinding
    private val studentViewModel: AddStudentToJournalViewModel by viewModels {
        AddStudentToJournalViewModelFactory((requireActivity().application as DBlication).studentRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentAddStudentToJournalBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.btnAddToDB.setOnClickListener{
            if (!empty()){
                val studentEntity = getStudentValues()
                addStudentEntityToDB(studentEntity)
                activity?.onBackPressed()
            }
        }
    }

    fun addStudentEntityToDB(studentEntity: StudentEntity){
        studentViewModel.insert(studentEntity)
    }

    fun getStudentValues():StudentEntity{
        val firstName:String = binding.edFirstName.text.toString()
        val secondName:String = binding.edSecondName.text.toString()
        val schoolClass:Int = binding.edClass.text.toString().toInt()
        val price:Int = binding.edPrice.text.toString().toInt()
        return StudentEntity(firstName, secondName, price, schoolClass)
    }
    // Проверка на заполнение полей
    private fun empty():Boolean{
        binding.apply {
            if (edFirstName.text.isNullOrEmpty())edFirstName.error="Заполните поле"
            if (edSecondName.text.isNullOrEmpty())edSecondName.error="Заполните поле"
            if (edClass.text.isNullOrEmpty())edClass.error="Заполните поле"
            if (edPrice.text.isNullOrEmpty())edPrice.error="Заполните поле"
            return edFirstName.text.isNullOrEmpty() || edSecondName.text.isNullOrEmpty() ||
                    edClass.text.isNullOrEmpty() || edPrice.text.isNullOrEmpty()
        }
    }
}
package com.example.tutor.journal.addStudentToJournal

import android.os.Binder
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import com.example.tutor.R
import com.example.tutor.bd.StudentEntity
import com.example.tutor.databinding.FragmentAddStudentToJournalBinding
import com.example.tutor.journal.studentJournal.StudentApplication

class AddStudentToJournalFragment : Fragment() {
lateinit var binding: FragmentAddStudentToJournalBinding
    private val studentViewModel: AddStudentToJournalViewModel by viewModels {
        AddStudentToJournalViewModelFactory((requireActivity().application as StudentApplication).repository)
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
            val studentEntity = getStudentValues()
            addStudentEntityToDB(studentEntity)
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

}
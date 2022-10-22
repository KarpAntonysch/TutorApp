package com.example.tutor.journal.studentJournal.pager.inactiveStudents

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentResultListener
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.example.tutor.R
import com.example.tutor.adapters.InactiveJournalAdapter
import com.example.tutor.bd.entities.StudentEntity
import com.example.tutor.databinding.FragmentInactiveStudentsBinding
import com.example.tutor.dialogs.JointDialogFragment
import com.example.tutor.dialogs.JointDialogInterface
import com.example.tutor.fireBase.FireBaseRepository
import com.example.tutor.journal.studentJournal.DBapplication


class InactiveStudentsFragment : Fragment(), InactiveJournalAdapter.Listener,JointDialogInterface {
    lateinit var binding: FragmentInactiveStudentsBinding
    lateinit var recyclerView: RecyclerView
    private val adapter = InactiveJournalAdapter(this)
    private val inactiveViewModel: InactiveStudentsViewModel by viewModels {
        InactiveStudentsViewModelFactory((requireActivity().application as DBapplication).studentRepository
            , FireBaseRepository())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentInactiveStudentsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        realizationOfInactiveRV()
    }

    private fun realizationOfInactiveRV() {
        recyclerView = binding.allInactiveStudentsRV
        recyclerView.adapter = adapter
        inactiveViewModel.allInactiveStudents.observe(viewLifecycleOwner) {
                listOfInactiveStudents ->
            listOfInactiveStudents.let { adapter.submitList(it) }
        }

    }

    override fun returnStudent(studentEntity: StudentEntity) {
        inactiveViewModel.returnStudentToActive(studentEntity.id)
        inactiveViewModel.returnStudentToActiveFB(studentEntity)
    }

    override fun deleteStudent(studentEntity: StudentEntity) {
        showJoinDialog(R.string.delete,true,R.string.yes,R.string.no,
            childFragmentManager,R.string.foreverDelete,true)
        setupDialogFragmentListener(studentEntity)

    }
    private fun setupDialogFragmentListener(studentEntity: StudentEntity) {
        childFragmentManager.setFragmentResultListener(JointDialogFragment.REQUEST_KEY,
            this,
            FragmentResultListener { _, result ->
                when (result.getInt(JointDialogFragment.KEY_RESPONSE)) {
                    DialogInterface.BUTTON_POSITIVE -> {
                        inactiveViewModel.deleteStudent(studentEntity)
                        Toast.makeText(requireContext(), "удалено", Toast.LENGTH_SHORT).show()
                    }
                }
            })
    }

}
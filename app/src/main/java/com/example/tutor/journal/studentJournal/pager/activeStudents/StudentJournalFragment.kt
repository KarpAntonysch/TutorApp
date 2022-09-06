package com.example.tutor.journal.studentJournal.pager.activeStudents

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.tutor.R
import com.example.tutor.adapters.StudentJournalAdapter
import com.example.tutor.bd.entities.StudentEntity
import com.example.tutor.databinding.FragmentStudentJournalBinding
import com.example.tutor.journal.StudentJournalViewModel
import com.example.tutor.journal.StudentJournalViewModelFactory
import com.example.tutor.journal.studentJournal.DBapplication
import com.example.tutor.journal.studentJournal.JourmalDialogFragment


class StudentJournalFragment() : Fragment(), StudentJournalAdapter.Listener,
    JournalActionModeCallback.ActionModeListener {
    lateinit var binding: FragmentStudentJournalBinding
    lateinit var recyclerView: RecyclerView
    private var adapter = StudentJournalAdapter(this)
    private val studentJournalViewModel: StudentJournalViewModel by viewModels {
        StudentJournalViewModelFactory((requireActivity().application as DBapplication).studentRepository)
    }
    lateinit var actionMode: JournalActionModeCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)//сообщает системе, что фрагемент хочет получать обратные вызовы, связанные с меню
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentStudentJournalBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnAddStudent.setOnClickListener {
            findNavController().navigate(R.id.action_jornalPagerFragment_to_addStudentToJournalFragment)
        }
        realizationOfRV()
        hideFAB()
    }

    private fun realizationOfRV() {
        recyclerView = binding.allStudentsRV
        recyclerView.adapter = adapter
        studentJournalViewModel.allStudents.observe(viewLifecycleOwner)
        { studentList ->
            studentList.let { adapter.submitList(it) }
        }
    }

    // переопределенная функция из интерфейса Listener для открытия actionMode.Callback1
    override fun onClickToChangeStudentActive(
        studentEntity: StudentEntity,
    ) {
        actionMode =
            JournalActionModeCallback(studentEntity)
        actionMode.startActionMode(view!!, R.menu.journal_app_menu, this)// для "запуска" actionMode
        // передаем в параметры функции вью, меню(наполняем бар) и реализацию интерфейса.
        // Которая реализвована как раз в фрагменте(ниже)
    }

    // Реализация метода интерфейса ActionModeListener для удаления ученика
    override fun clickToMenuDelete(studentEntity: StudentEntity) {
        showDialogFragment()
        setupDialogFragmentListener(studentEntity)
    }
    // Реализация метода интерфейса ActionModeListener для редактирования ученика
    override fun clickToMenuEdit(studentEntity: StudentEntity) {
        val bundle =Bundle()
        bundle.putParcelable("ArgForStudentEditing: com.example.tutor.bd.entities.StudentEntity",studentEntity)
        findNavController().navigate(R.id.action_jornalPagerFragment_to_editStudentFragment,bundle)
    }

    override fun clickToMenuWatch(studentEntity: StudentEntity) {
        val bundle = Bundle()
        bundle.putParcelable("ArgForWatching: com.example.tutor.bd.entities.StudentEntity",studentEntity)
        findNavController().navigate(R.id.action_jornalPagerFragment_to_watchingStudentFragment,bundle)
    }

    // Функция вызова диалогового окна из JournalDialogFragment
    fun showDialogFragment() {
        val dialogFragment = JourmalDialogFragment()
        dialogFragment.show(childFragmentManager, JourmalDialogFragment.TAG)
    }

    // Функция инициализации кнопок в диалоговом окне из JournalDialogFragment
    fun setupDialogFragmentListener(studentEntity: StudentEntity) {
        childFragmentManager.setFragmentResultListener(JourmalDialogFragment.REQUEST_KEY,
            this,
            FragmentResultListener { _, result ->
                val which = result.getInt(JourmalDialogFragment.KEY_RESPONSE)
                when (which) {
                    DialogInterface.BUTTON_POSITIVE -> {
                        studentJournalViewModel.changeStudentActive(
                            studentEntity.id)
                        actionMode.hideActionMode()
                        Toast.makeText(requireContext(), "удалено", Toast.LENGTH_SHORT).show()
                    }
                }
            })
    }


    // функция для динамического исчезания FAB при прокрутке списка
    fun hideFAB() {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                // вниз
                if (dy > 0 && binding.btnAddStudent.visibility === View.VISIBLE) {
                    binding.btnAddStudent.hide()
                    // вверх и статика
                } else if (dy <= 0 && binding.btnAddStudent.visibility !== View.VISIBLE) {
                    binding.btnAddStudent.show()
                }
            }
        })
    }
}

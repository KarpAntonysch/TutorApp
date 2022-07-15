package com.example.tutor.journal.studentJournal.pager.activeStudents

import android.content.DialogInterface
import android.os.Bundle
import android.view.*
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


class StudentJournalFragment : Fragment(), StudentJournalAdapter.Listener {
    lateinit var binding: FragmentStudentJournalBinding
    lateinit var recyclerView: RecyclerView
    private var adapter = StudentJournalAdapter(this)
    private val studentJournalViewModel: StudentJournalViewModel by viewModels {
        StudentJournalViewModelFactory((requireActivity().application as DBapplication).studentRepository)
    }
    var visibilityEdit = true
    var visibilityDelete = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)//сообщает системе, что фрагемент хочет получать обратные вызовы, связанные с меню
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
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
    // создание appbar
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
       inflater.inflate(R.menu.journal_app_menu,menu)

    }
    // этот метод вызывается перед каждым отображением меню.
    // Здесь прописывается логика скрытия и отобпражения элементов
    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
       val updateMenuDelete = menu.findItem(R.id.menuDelete)
       val updateMenuEdit = menu.findItem(R.id.menuEdit)
        updateMenuEdit.isVisible = visibilityDelete
        updateMenuDelete.isVisible = visibilityEdit
    }
    // обработка кнопок меню appbar
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
        R.id.menuDelete ->{
            Toast.makeText(requireContext(),"удалено",Toast.LENGTH_SHORT).show()
        }
            R.id.menuEdit ->{
                Toast.makeText(requireContext(), "изменено", Toast.LENGTH_SHORT).show()
            }
        }
        return true
    }
    // функция для обновдения appbar при нажатии на пользователя
    override fun updateOptionMenu(studentEntity: StudentEntity) {
        visibilityEdit = !visibilityEdit
        visibilityDelete = !visibilityDelete
        requireActivity().invalidateOptionsMenu()
    }
    // функция из интерфейса для открытие dialog и подтверждение удаления
    override fun onClickToChangeStudentActive(studentEntity: StudentEntity) {
        showDialogFragment()
        setupDialogFragmentListener(studentEntity)
    }

    // Функция вызова диалогового окна из JournalDialogFragment
    fun showDialogFragment() {
        val dialogFragment = JourmalDialogFragment()
        dialogFragment?.show(childFragmentManager, JourmalDialogFragment.TAG)
    }

    // Функция инициализации кнопок в диалоговом окне из JournalDialogFragment
    fun setupDialogFragmentListener(studentEntity: StudentEntity) {
        childFragmentManager.setFragmentResultListener(JourmalDialogFragment.REQUEST_KEY,
            this,
            FragmentResultListener { _, result ->
                val which = result.getInt(JourmalDialogFragment.KEY_RESPONSE)
                when (which) {
                    DialogInterface.BUTTON_POSITIVE -> studentJournalViewModel.changeStudentActive(
                        studentEntity.id)
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

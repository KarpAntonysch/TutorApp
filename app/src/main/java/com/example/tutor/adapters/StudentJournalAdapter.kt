package com.example.tutor.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.tutor.R
import com.example.tutor.bd.entities.StudentEntity
import com.example.tutor.databinding.StudentJournalItemBinding
import com.example.tutor.journal.studentJournal.pager.activeStudents.JournalActionModeCallback

class StudentJournalAdapter(val listener: Listener) :
    ListAdapter<StudentEntity, StudentJournalAdapter.StudentViewHolder>(StudentComparator()) {

    class StudentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = StudentJournalItemBinding.bind(view)
        //Добавляем параметр position, для реализиции номерации
        fun bind(studentEntity: StudentEntity, position: Int, listener: Listener) = with(binding) {
            tvNumber.text = (position + 1).toString()
            tvName.text = studentEntity.firstName
            tvSecondName.text = studentEntity.secondName
            tvSchoolClass.text = studentEntity.schoolClass.toString()
            // Реализация долгого нажатия на объект для удаления его из РВ с использованием интерфейса для обращения к
            //StudentJournalFragment  и DialogFragment
            itemView.setOnLongClickListener{
                listener.onClickToChangeStudentActive(studentEntity)
                return@setOnLongClickListener true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.student_journal_item, parent, false)
        return StudentViewHolder(view)
    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        val currentStudent = getItem(position)
        holder.bind(currentStudent, position, listener)
    }

    class StudentComparator : DiffUtil.ItemCallback<StudentEntity>() {
        override fun areItemsTheSame(oldItem: StudentEntity, newItem: StudentEntity): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: StudentEntity, newItem: StudentEntity): Boolean {
            return oldItem == newItem
        }
    }


    // интерфейс с методом для инициализации лонгклика по объекту RV. Callback1
    interface Listener  {// раширен интрефейсом
        //метод переопределяется в StudentJournalFragment для открытия actionMode
        fun onClickToChangeStudentActive(studentEntity: StudentEntity)
       // fun updateOptionMenu(studentEntity: StudentEntity)
    }

}

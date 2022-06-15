package com.example.tutor.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.tutor.R
import com.example.tutor.bd.StudentEntity
import com.example.tutor.databinding.StudentJournalItemBinding

class StudentJournalAdapter(val listener:Listener) :
    ListAdapter<StudentEntity, StudentJournalAdapter.StudentViewHolder>(StudentComparator()) {

    class StudentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = StudentJournalItemBinding.bind(view)
        fun bind(studentEntity: StudentEntity,position: Int,listener:Listener)  {
            binding.tvID.text= (position + 1).toString()
            binding.tvName.text = studentEntity.firstName
            binding.tvSecondName.text = studentEntity.secondName
            binding.tvSchoolClass.text = studentEntity.schoolClass.toString()
            binding.btnDelete.setOnClickListener{
                listener.onClick(studentEntity)
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
        holder.bind(currentStudent,position,listener)
    }

    class StudentComparator : DiffUtil.ItemCallback<StudentEntity>() {
        override fun areItemsTheSame(oldItem: StudentEntity, newItem: StudentEntity): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: StudentEntity, newItem: StudentEntity): Boolean {
            return oldItem == newItem
        }
    }

    // интерфейс для обработки нажатий в RV
    interface Listener{
        fun onClick(studentEntity: StudentEntity)
    }
}

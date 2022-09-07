package com.example.tutor.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.tutor.R
import com.example.tutor.bd.entities.StudentEntity
import com.example.tutor.databinding.InactiveStudentItemBinding

class InactiveJournalAdapter(val listener:Listener) :
    ListAdapter<StudentEntity, InactiveJournalAdapter.InactiveStudentViewHolder>(InactiveComparator()) {

    class InactiveStudentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = InactiveStudentItemBinding.bind(view)
        fun bind(studentEntity: StudentEntity, position: Int,listener: Listener) = with(binding) {
            tvInactiveNumber.text = (position + 1).toString()
            tvInactiveName.text = studentEntity.firstName
            tvInactiveSecondName.text = studentEntity.secondName
            tvInactiveSchoolClass.text = studentEntity.schoolClass.toString()
            btnReturn.setOnClickListener{
                listener.returnStudent(studentEntity)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InactiveStudentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.inactive_student_item, parent, false)
        return InactiveStudentViewHolder(view)
    }

    override fun onBindViewHolder(holder: InactiveStudentViewHolder, position: Int) {
        val currentStudent = getItem(position)
        holder.bind(currentStudent, position,listener)
    }

    class InactiveComparator : DiffUtil.ItemCallback<StudentEntity>() {
        override fun areItemsTheSame(oldItem: StudentEntity, newItem: StudentEntity): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: StudentEntity, newItem: StudentEntity): Boolean {
            return oldItem == newItem
        }
    }
    interface Listener{
        fun returnStudent(studentEntity: StudentEntity)
    }
}
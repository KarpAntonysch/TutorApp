package com.example.tutor.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

import com.example.tutor.R
import com.example.tutor.bd.StudentEntity
import com.example.tutor.databinding.StudentJournalItemBinding

class StudentJournalAdapter : RecyclerView.Adapter<StudentJournalAdapter.StudentViewHolder>() {

    var journalList= emptyList<StudentEntity>()

    class StudentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = StudentJournalItemBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.student_journal_item, parent, false)
        return StudentViewHolder(view)
    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        holder.binding.tvID.text=journalList[position].id++.toString()
        holder.binding.tvName.text=journalList[position].firstName
        holder.binding.tvSecondName.text=journalList[position].secondName
        holder.binding.tvSchoolClass.text=journalList[position].schoolClass.toString()
    }

    override fun getItemCount(): Int {
        return journalList.size
    }
    @SuppressLint("NotifyDataSetChanged")
    fun setList(list: List<StudentEntity>){
        journalList=list
        notifyDataSetChanged()
    }

}

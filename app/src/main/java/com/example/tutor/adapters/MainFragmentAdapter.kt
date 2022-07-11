package com.example.tutor.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.tutor.R
import com.example.tutor.adapters.MainFragmentAdapter.MainFragmentViewHolder
import com.example.tutor.bd.entities.ScheduleEntity
import com.example.tutor.bd.entities.ScheduleWithStudent
import com.example.tutor.convertLongToTime
import com.example.tutor.databinding.RecyclerViewSheduleItemBinding

class MainFragmentAdapter(val listener : Listener):
        ListAdapter<ScheduleWithStudent, MainFragmentViewHolder> (MainFragmentComparator()){

    class MainFragmentViewHolder(view: View): RecyclerView.ViewHolder(view){
        val binding = RecyclerViewSheduleItemBinding.bind(view)
        fun bind(scheduleWithStudent: ScheduleWithStudent,position: Int, listener: Listener) = with(binding){
            tvNumder.text = (position+1).toString()
            tvNameRV2.text = scheduleWithStudent.ListOfStudentEntity.firstName
            tvSNameRV2.text = scheduleWithStudent.ListOfStudentEntity.secondName
            tvTimeRV2.text  = scheduleWithStudent.scheduleEntity.dateWithTime.convertLongToTime("HH:mm")
            btnDeleteRV2.setOnClickListener{
                listener.onClickToDeleteSchedule(scheduleWithStudent.scheduleEntity)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainFragmentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_view_shedule_item, parent, false)
        return MainFragmentViewHolder(view)
    }

    override fun onBindViewHolder(holder: MainFragmentViewHolder, position: Int) {
        val currentStudent = getItem(position)
        holder.bind(currentStudent, position,listener)
    }

    class MainFragmentComparator : DiffUtil.ItemCallback<ScheduleWithStudent>() {
        override fun areItemsTheSame(oldItem: ScheduleWithStudent, newItem: ScheduleWithStudent): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: ScheduleWithStudent, newItem: ScheduleWithStudent): Boolean {
            return oldItem == newItem
        }
    }
    interface Listener{
        fun onClickToDeleteSchedule(scheduleEntity: ScheduleEntity)
    }
}

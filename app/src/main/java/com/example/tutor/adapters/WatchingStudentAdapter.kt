package com.example.tutor.adapters
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.tutor.R
import com.example.tutor.convertLongToTime
import com.example.tutor.databinding.WatchingStudentItemBinding


class  WatchingStudentAdapter :
    ListAdapter<Long, WatchingStudentAdapter.WatchingStudentViewHolder>(WatchingComparator()) {
    class WatchingStudentViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val binding = WatchingStudentItemBinding.bind(view)
        fun bind(date: Long,position: Int) = with(binding){
            watchLessonsNumber.text = (position+1).toString()
            watchDate.text=date.convertLongToTime("dd.MM.yyyy")
            watchTime.text=date.convertLongToTime("HH:mm")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WatchingStudentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.watching_student_item,parent,false)
        return WatchingStudentViewHolder(view)
    }

    override fun onBindViewHolder(holder: WatchingStudentViewHolder, position: Int) {
        val lesson = getItem(position)
        holder.bind(lesson,position)
    }
    class WatchingComparator : DiffUtil.ItemCallback<Long>() {
        override fun areItemsTheSame(oldItem: Long, newItem: Long): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Long, newItem: Long): Boolean {
            return oldItem == newItem
        }
    }
}
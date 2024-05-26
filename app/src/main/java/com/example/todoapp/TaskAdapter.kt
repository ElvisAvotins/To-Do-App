package com.example.todoapp

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.databinding.ItemTaskBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TaskAdapter(private var tasks: MutableList<Task>, private val onItemClick: (Task) -> Unit) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    class TaskViewHolder(val binding: ItemTaskBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]
        holder.binding.textViewTitle.text = task.title
        holder.binding.checkBox.isChecked = task.completed

        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())

        if (task.completed) {
            holder.binding.textViewTitle.paintFlags = holder.binding.textViewTitle.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            task.completedDate?.let {
                val completedDateString = dateFormat.format(it)
                holder.binding.textViewDate.text = holder.binding.root.context.getString(R.string.completed_date, completedDateString)
            }
        } else {
            holder.binding.textViewTitle.paintFlags = holder.binding.textViewTitle.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            task.deadline?.let {
                val deadlineString = dateFormat.format(it)
                holder.binding.textViewDate.text = holder.binding.root.context.getString(R.string.deadline, deadlineString)
            } ?: run {
                holder.binding.textViewDate.text = holder.binding.root.context.getString(R.string.no_deadline)
            }
        }

        holder.binding.root.setOnClickListener {
            onItemClick(task)
        }

        holder.binding.checkBox.setOnCheckedChangeListener { _, isChecked ->
            task.completed = isChecked
            task.completedDate = if (isChecked) Date() else null
            notifyDataSetChanged()
        }
    }

    override fun getItemCount() = tasks.size

    fun updateTasks(newTasks: List<Task>) {
        tasks = newTasks.toMutableList()
        notifyDataSetChanged()
    }
}

package com.android.kotlinmvvmtodolist.ui.task

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android.kotlinmvvmtodolist.R
import com.android.kotlinmvvmtodolist.database.TaskEntry
import com.android.kotlinmvvmtodolist.databinding.RowLayoutBinding
import com.android.kotlinmvvmtodolist.viewmodel.TaskViewModel
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class TaskAdapter(val clickListener: TaskClickListener,val doneClickListener: DoneClickListener) :
    ListAdapter<TaskEntry, TaskAdapter.ViewHolder>(TaskDiffCallback) {


    companion object TaskDiffCallback : DiffUtil.ItemCallback<TaskEntry>() {
        override fun areItemsTheSame(oldItem: TaskEntry, newItem: TaskEntry) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: TaskEntry, newItem: TaskEntry) = oldItem == newItem
    }

    class ViewHolder(val binding: RowLayoutBinding) : RecyclerView.ViewHolder(binding.root) {

        var done : ImageView = binding.checked


        fun bind(taskEntry: TaskEntry, clickListener: TaskClickListener,doneClickListener: DoneClickListener) {

            binding.taskEntry = taskEntry
            binding.clickListener = clickListener
            binding.doneClickListener = doneClickListener
            binding.executePendingBindings()

        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            RowLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val current = getItem(position)
        holder.bind(current, clickListener, doneClickListener )
    }
}

class TaskClickListener(val clickListener: (taskEntry: TaskEntry) -> Unit) {
    fun onClick(taskEntry: TaskEntry) = clickListener(taskEntry)
}
class DoneClickListener(val clickListener: (taskEntry: TaskEntry) -> Unit) {
    fun onClick(taskEntry: TaskEntry) {
        taskEntry.done=!taskEntry.done
        taskEntry.changed_at =  dateNow()
        clickListener(taskEntry)
    }
    private fun convertDateToLong(date: String): Long {
        val l = LocalDate.parse(date, DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm:ss"))

        val unix = l.atStartOfDay(ZoneId.systemDefault()).toInstant().epochSecond
        return unix
    }

    private fun dateNow(): Long {
        return convertDateToLong(
            LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm:ss"))
        )
    }
}


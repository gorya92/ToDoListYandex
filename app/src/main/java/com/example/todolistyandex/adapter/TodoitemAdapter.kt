package com.example.yandextask.adapter

import android.app.Activity
import android.content.Context
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.getColor
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.todolistyandex.R
import com.example.todolistyandex.model.TodoItemRepository
import com.example.yandextask.model.TodoItem
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class TodoitemAdapter() : RecyclerView.Adapter<TodoitemAdapter.ViewHolder>() {

     var recyclerList : ArrayList<TodoItem> = arrayListOf()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView
        val checked: ImageView
        val importance: ImageView
        val dataTv: TextView


        init {
            title = itemView.findViewById(R.id.title)
            checked = itemView.findViewById(R.id.checked)
            importance = itemView.findViewById(R.id.important)
            dataTv = itemView.findViewById(R.id.dateTV)

        }

        fun bind(i: Int) {
            setAllProperties(recyclerList,i,itemView.context)
        }

        /** Установка всех ресурсов по соответствующим параметрам **/
        fun setAllProperties(todoItemsRepository: ArrayList<TodoItem>, i: Int,activity: Context) {
            title.text = todoItemsRepository[i].title
            title.setTextColor(getColor(activity, R.color.black))
            dataTv.setText(todoItemsRepository[i].deadline)
            if (todoItemsRepository[i].done) {
                checked.setImageResource(R.drawable.ic_checked)
                title.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG)
                title.setTextColor(getColor(activity, R.color.label_Tertiary))
            } else {
                checked.setImageResource(R.drawable.ic_unchecked)
                title.paintFlags = title.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                title.setTextColor(getColor(activity, R.color.black))
            }
            if (todoItemsRepository[i].importance == "important") {
                importance.setImageResource(R.drawable.ic_high)

            }
            if (todoItemsRepository[i].importance == "low")
                importance.setImageResource(R.drawable.ic_low)
            if (todoItemsRepository[i].importance == "basic") {
                importance.setImageResource(R.drawable.rectangle)

            }
            val sdf = SimpleDateFormat("dd/MM/yyyy")
            /** Проверка на "дедлайн" **/
            if (todoItemsRepository[i].deadline != "") {
                val firstDate: Date = sdf.parse(todoItemsRepository[i].deadline)
                val secondDate: Date = sdf.parse(todoItemsRepository[i].created_at)
                if (todoItemsRepository[i].done == false) {

                    val cmp = firstDate.compareTo(secondDate)
                    when {
                        cmp > 0 -> {
                            checked.setImageResource(R.drawable.ic_unchecked)
                        }
                        cmp < 0 -> {
                            checked.setImageResource(R.drawable.ic_uncheckedred)
                        }
                        else -> {
                        }
                    }
                }
            }

        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.todo_item, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)

        holder.itemView.setOnClickListener { view ->
            var bundle =Bundle()
            bundle.putInt("id",TodoItemRepository.todoList.indexOf(recyclerList[position]))
            view.findNavController().navigate(R.id.newItemFragment,bundle)
        }
    }

    override fun getItemCount(): Int {
      //  return todoItemsRepositoryvisible.size
        return recyclerList.size
    }

   fun submitRepository(todoItemsRepository: ArrayList<TodoItem>) {
        val old = recyclerList
        val diffResult: DiffUtil.DiffResult = DiffUtil.calculateDiff(
            todoItemDiffCallBack(
                old,
                todoItemsRepository
            )
        )
        recyclerList = todoItemsRepository
        diffResult.dispatchUpdatesTo(this)
    }

    class todoItemDiffCallBack(
        var old: ArrayList<TodoItem>,
        var new: ArrayList<TodoItem>
    ) : DiffUtil.Callback() {
        override fun getOldListSize(): Int {
            return old.size
        }

        override fun getNewListSize(): Int {
            return new.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            var oldProduct: TodoItem = old[oldItemPosition];
            var newProduct: TodoItem = new[newItemPosition];
            return oldProduct.id == newProduct.id

        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return old[oldItemPosition] == new[newItemPosition]
        }
    }


}



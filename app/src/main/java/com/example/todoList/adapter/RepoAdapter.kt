package com.example.todoList.adapter

import android.content.Context
import android.graphics.Paint
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import com.example.todolistyandex.model.Todo
import com.example.todoList.R
import com.example.todoList.model.Lists
import java.text.SimpleDateFormat

class RepoAdapter( ) : RecyclerView.Adapter<RepoAdapter.RepoViewHolder>() {



    var visibility = false
    var arr : ArrayList<Todo> = arrayListOf()

  inner  class RepoViewHolder(view: View) : RecyclerView.ViewHolder(view) {


        var title: TextView
        var checked: ImageView
        var importance: ImageView
        var dataTv: TextView
        init {
            title = itemView.findViewById(R.id.title)
            checked = itemView.findViewById(R.id.checked)
            importance = itemView.findViewById(R.id.important)
            dataTv = itemView.findViewById(R.id.dateTV)


        }


        fun bind(i: Int, todoItemsRepository: java.util.ArrayList<Todo>) {
            setAllProperties(todoItemsRepository,i,itemView.context)
            checked.setOnClickListener {
                if (visibility) {
                    Lists.todo[i].done = !Lists.todo[i].done
                   notifyDataSetChanged()
                }
                else{
                    Lists.todoVisibility[i].done = !Lists.todoVisibility[i].done
                  notifyDataSetChanged()
                }

            }
        }
        fun setAllProperties(todoItemsRepository: java.util.ArrayList<Todo>, i: Int, activity: Context) {
            title.text = todoItemsRepository[i].text
            title.setTextColor(ContextCompat.getColor(activity, R.color.black))
            //dataTv.text = todoItemsRepository[i].deadline
            if (todoItemsRepository[i].done) {
                checked.setImageResource(R.drawable.ic_checked)
                title.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                title.setTextColor(ContextCompat.getColor(activity, R.color.label_Tertiary))
            } else {
                checked.setImageResource(R.drawable.ic_unchecked)
                title.paintFlags = title.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                title.setTextColor(ContextCompat.getColor(activity, R.color.black))
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
            /*  if (todoItemsRepository[i].deadline != "") {
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
              }*/

        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.todo_item, parent, false)
        return RepoViewHolder(view)
    }

    override fun onBindViewHolder(holder: RepoViewHolder, i: Int) {


        holder.itemView.setOnClickListener { view ->
            var bundle = Bundle()
            bundle.putInt("id", Lists.todo.indexOf(arr[i]))
            view.findNavController().navigate(R.id.newItemFragment,bundle)
        }
        holder.bind(i,arr)
    }

    override fun getItemCount(): Int = arr.size

}

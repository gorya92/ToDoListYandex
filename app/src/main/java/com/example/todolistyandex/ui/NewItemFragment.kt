package com.example.todolistyandex.ui

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat.getColor
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.todolistyandex.R
import com.example.todolistyandex.databinding.FragmentNewItemBinding
import com.example.todolistyandex.databinding.FragmentToDoListBinding
import com.example.todolistyandex.model.TodoItemRepository
import com.example.todolistyandex.presenters.NewListPresenters
import com.example.todolistyandex.presenters.TodoListPresenter
import com.example.todolistyandex.views.NewItemView
import com.example.yandextask.model.TodoItem
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

class NewItemFragment : MvpAppCompatFragment(),NewItemView {

    private lateinit var viewBinding: FragmentNewItemBinding

    @InjectPresenter()
    lateinit var newListPresenters: NewListPresenters

    var cal: Calendar = Calendar.getInstance() // Календарь

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.setTheme(R.style.Theme_date);
        return inflater.inflate(R.layout.fragment_new_item, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        var startList = TodoItemRepository.todoList
        viewBinding = FragmentNewItemBinding.bind(view)

        val bundle = Bundle()
        bundle.putBoolean("start", false)

        changeGarbageColor()
        spinnerInitialize()

        if (requireArguments().getInt("id")!=-1)
            setIfThatChange()

        viewBinding.closeBtn.setOnClickListener{
            TodoItemRepository.todoList=startList
            findNavController().navigate(R.id.toDoListFragment,bundle)}


        val dateSetListener =
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                newListPresenters.calendarGet()
            }

        /** Свитч предназначен для изменения параметра даты  **/
       viewBinding.dateSwitch.setOnCheckedChangeListener { compoundButton, b ->
            if (viewBinding.dateSwitch.isChecked) {
                    val dpd = DatePickerDialog(
                        requireActivity(),
                        dateSetListener,
                        cal.get(Calendar.YEAR),
                        cal.get(Calendar.MONTH),
                        cal.get(Calendar.DAY_OF_MONTH)
                    )
                    var date: Date = Date()

                    dpd.datePicker.minDate = cal.time.time

                    dpd.show()
            } else
              viewBinding.date.text = ""
        }



        viewBinding.save.setOnClickListener {
            if (viewBinding.dealtext.text.toString() != ""){
                if (arguments?.getBoolean("new")!!)
                TodoItemRepository.todoList.add( addItem())

                else{
                    TodoItemRepository.todoList[requireArguments().getInt("id")]=changeItem(requireArguments().getInt("id"))
                }
                findNavController().navigate(R.id.toDoListFragment,bundle)
            }
            }

        viewBinding.deletetext.setOnClickListener {
            if (requireArguments().getInt("id")!=-1) {
                TodoItemRepository.todoList.removeAt(requireArguments().getInt("id"))
                findNavController().navigate(R.id.toDoListFragment, bundle)
            }
        }




        super.onViewCreated(view, savedInstanceState)
    }



    fun spinnerInitialize(){
        /** Инициализация спиннера **/
        ArrayAdapter.createFromResource(
            requireActivity(),
            R.array.importance_list,

            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            viewBinding.spinner.adapter = adapter
        }

    }


    fun setIfThatChange(){
        val text = TodoItemRepository.todoList[requireArguments().getInt("id")]
        viewBinding.dealtext.setText(text.title)
        viewBinding.date.text= text.deadline

        if (text.importance == "important")
            viewBinding.spinner.setSelection(2)
        if (text.importance == "basic")
           viewBinding.spinner.setSelection(0)
        if (text.importance == "low")
          viewBinding.spinner.setSelection(1)
    }


    override fun changeGarbageColor() {
        if (viewBinding.dealtext.text.toString() != "" ) {
           viewBinding.deletetext.setTextColor(getColor(requireActivity(), R.color.Red))
           viewBinding.deleteImg.setImageResource(R.drawable.ic_baseline_deletered_24)
        }

        viewBinding.dealtext.addTextChangedListener {

            if (viewBinding.dealtext.text.toString() != "" && !arguments?.getBoolean("new")!!) {
               viewBinding.deletetext.setTextColor(getColor(requireActivity(), R.color.Red))
              viewBinding.deleteImg.setImageResource(R.drawable.ic_baseline_deletered_24)
            } else {
              viewBinding.deletetext.setTextColor(getColor(requireActivity(), R.color.Label_Disable))
              viewBinding.deleteImg.setImageResource(R.drawable.ic_baseline_delete_24)
            }
        }

        if (viewBinding.dealtext.lineCount == 4) {
           viewBinding.textcard.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        }


    }

    override fun setData(let: String?) {
        viewBinding.date.text =let
    }
fun changeItem(id:Int): TodoItem {
    var important = "basic"
    if (viewBinding.spinner.selectedItemPosition == 1)
        important = "low"
    if (viewBinding.spinner.selectedItemPosition == 2)
        important = "important"

    return   TodoItem(
        id = id.toString(),
        viewBinding.dealtext.text.toString() ,
        importance = important,
        done = false,
        deadline = viewBinding.date.text as String,
        created_at = LocalDateTime.now()
            .format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
    )
}
     fun addItem(): TodoItem {
        var important = "basic"
        if (viewBinding.spinner.selectedItemPosition == 1)
            important = "low"
        if (viewBinding.spinner.selectedItemPosition == 2)
            important = "important"

                return   TodoItem(
                    id = (TodoItemRepository.todoList + 1).toString(),
                    viewBinding.dealtext.text.toString() ,
                    importance = important,
                    done = false,
                    deadline = viewBinding.date.text as String,
                    created_at = LocalDateTime.now()
                        .format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                )

    }


}
package com.example.todolistyandex.fragments

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
import com.example.todolistyandex.model.TodoItemsRepository
import com.example.yandextask.model.TodoItem
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class NewItemFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        return inflater.inflate(R.layout.fragment_new_item, container, false)
    }


    var new = true
    var change = false
    var cal: Calendar = Calendar.getInstance() // Календарь
    var normalDate: String = ""


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.setTheme(R.style.Theme_date);

        var card: CardView = view.findViewById(R.id.textcard)
        var title: EditText = view.findViewById(R.id.dealtext)
        var deleteText: TextView = view.findViewById(R.id.deletetext)
        var deleteImg: ImageView = view.findViewById(R.id.deleteImg)
        var closeBtn: ImageButton = view.findViewById(R.id.closeBtn)

        if (title.text.toString() != "") {
            deleteText.setTextColor(getColor(requireActivity(), R.color.Red))
            deleteImg.setImageResource(R.drawable.ic_baseline_deletered_24)
        }

        if (title.lineCount == 4) {
            card.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        }


        /** Инициализация спиннера **/
        val spinner: Spinner = view.findViewById(R.id.spinner)
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            requireActivity(),
            R.array.importance_list,

            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter

        }

        var switch: Switch = view.findViewById(R.id.dateSwitch)


        var date: TextView = view.findViewById(R.id.date)

        val dateSetListener =
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateDateInView(date)
            }

        /** Свитч предназначен для изменения параметра даты  **/
        switch.setOnCheckedChangeListener { compoundButton, b ->
            if (switch.isChecked) {
                if (new == true) {
                    val dpd = DatePickerDialog(
                        requireActivity(),
                        dateSetListener,
                        // set DatePickerDialog to point to today's date when it loads up
                        cal.get(Calendar.YEAR),
                        cal.get(Calendar.MONTH),
                        cal.get(Calendar.DAY_OF_MONTH)
                    )
                    var date: Date = Date()

                    dpd.datePicker.minDate = cal.time.time


                    dpd.show()


                }
            } else
                date.text = ""
        }

        /** Получение данных с предыдущего фрагмента   **/
        var arr = arguments?.getParcelable<TodoItemsRepository>("MyArgChange")
        var id = arguments?.getInt("id")
        var newItem = arguments?.getBoolean("new")

        val dateTime = LocalDateTime.now()
            .format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))

        var text: TodoItem = TodoItem(
            id = "0",
            "",
            "",
            done = false,
            created_at = dateTime
        )
        if (newItem == false) {

            text = arr!![id!!]

        }


        /** Заолнение ui если мы редактируем дело **/
        if (newItem == false) {
            Log.d("news", text.toString())
            normalDate = text.deadline
            change = true
            new = false
            Log.d("PARCELABLE", text.title)
            title.setText(text.title)
            if (text.deadline != "") {
                date.text = (text.deadline)
                switch.isChecked = true
                new = true
            }
            Log.d("PARCELABLE", text.importance)
            if (text.importance == "important")
                spinner.setSelection(2)
            if (text.importance == "basic")
                spinner.setSelection(0)
            if (text.importance == "low")
                spinner.setSelection(1)

        }
        /** Кнопка сохранение нового или измененного дела  **/
        var saveBtn: TextView = view.findViewById(R.id.save)
        saveBtn.setOnClickListener {
            if (title.text.toString() != "") {
                val bundle = Bundle()
                if (text != null) {
                    text.title = title.text.toString()
                    Log.d("Replace", id.toString() + " " + text.toString())

                    text.changed_at = LocalDateTime.now()

                        .format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                    if (switch.isChecked)
                        text.deadline = normalDate
                    else
                        text.deadline = ""
                    if (spinner.selectedItemPosition == 0)
                        text.importance = "basic"
                    if (spinner.selectedItemPosition == 1)
                        text.importance = "low"
                    if (spinner.selectedItemPosition == 2)
                        text.importance = "important"

                }
                if (newItem == false) {
                    Log.d("Replace", id.toString() + " " + text.toString())

                    if (id != null && text != null) {
                        Log.d("Replace", "NOT NEW")
                        arr?.set(id, text)
                        bundle.putParcelable("Change", arr)
                        view.findNavController().navigate(R.id.toDoListFragment, bundle)
                    }
                } else {
                    if (arr != null && text != null) {
                        Log.d("Replace", id.toString() + " " + "NEW")
                        Log.d("Replace", arr.toString())
                        text.id = (arr.size + 1).toString()

                        Log.d("Replace", "this array" + arr[0].toString())

                        arr.add(0, text)

                        bundle.putParcelable("Change", arr)
                        Log.d("Replace", arr.toString())
                    }
                    view.findNavController().navigate(R.id.toDoListFragment, bundle)
                }





                Log.d("PARCELABLE", text.toString())

            }
        }
        /** Кнопка закрытия  **/
        closeBtn.setOnClickListener {
            //replaceFragment(ToDoListFragment())
            var bundle = Bundle()
            bundle.putParcelable("Change", arr)
            findNavController().navigate(R.id.toDoListFragment, bundle)
        }
        /** Кнопка уцдаления не работает если запускаем наш фрагмента
         * с FABа добавления **/
        deleteText.setOnClickListener {
            if (title.text.toString() != "") {
                if (newItem == false) {

                    var bundle = Bundle()
                    if (arr != null) {
                        arr.removeAt(id!!)
                    }
                    bundle.putParcelable("Change", arr)
                    findNavController().navigate(R.id.toDoListFragment, bundle)
                }
            }
        }
        deleteImg.setOnClickListener {
            if (title.text.toString() != "") {
                if (newItem == false) {
                    var bundle = Bundle()
                    if (arr != null) {
                        arr.removeAt(id!!)
                    }
                    bundle.putParcelable("Change", arr)
                    findNavController().navigate(R.id.toDoListFragment, bundle)
                }
            }
        }
        if (newItem == false) {
            deleteText.setTextColor(getColor(requireActivity(), R.color.Red))
            deleteImg.setImageResource(R.drawable.ic_baseline_deletered_24)
        }
        /** Листенер для изменение TextView и ImageView "удалить" **/
        title.addTextChangedListener {
            if (title.text.toString() != "" && newItem == false) {
                deleteText.setTextColor(getColor(requireActivity(), R.color.Red))
                deleteImg.setImageResource(R.drawable.ic_baseline_deletered_24)
            } else {
                deleteText.setTextColor(getColor(requireActivity(), R.color.Label_Disable))
                deleteImg.setImageResource(R.drawable.ic_baseline_delete_24)
            }

            if (title.lineCount == 4) {
                card.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            }

        }
    }


    private fun updateDateInView(date: TextView) {
        val myFormat = "dd" // mention the format you need
        val myFormat2 = "yyyy"
        val normalFormat = "dd/MM/yyyy"
        val normalsdf = SimpleDateFormat(normalFormat, Locale.FRANCE)
        val sdf = SimpleDateFormat(myFormat, Locale.FRANCE)
        val sdf2 = SimpleDateFormat(myFormat2, Locale.FRANCE)
        var PeriodDate = cal.time
        normalDate = PeriodDate?.let { normalsdf.format(it) }.toString()
        val month: String = cal.getDisplayName(
            Calendar.MONTH,
            Calendar.LONG_FORMAT, Locale("ru")
        )

        Log.d("DATE", normalDate)
        date.text = PeriodDate?.let { sdf.format(it) + " " + month + " " + sdf2.format(it) }
    }

    companion object {


    }
}
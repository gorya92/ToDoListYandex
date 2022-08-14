package com.example.todoList.view.fragment


import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import com.example.todoList.MainApplication
import com.example.todoList.R
import com.example.todoList.model.Lists
import com.example.todoList.presenter.RepoPresenter
import kotlinx.android.synthetic.main.fragment_repo.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class RepoFragment : Fragment(), RepoPresenter.View {


    @Inject
    lateinit var presenter: RepoPresenter

    var cal: Calendar = Calendar.getInstance() // Календарь


    override fun onAttach(context: Context) {
        super.onAttach(context)

        (activity?.application as MainApplication).component.inject(this)
        presenter.injectView(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_repo, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        changeGarbageColor()
        spinnerInitialize()
        setDate()
        close()
        delete()
        save()
        ifChange()

    }

    fun ifChange(){
        if (requireArguments().getInt("id") != -1)
            presenter.setIfThatChange(requireArguments().getInt("id"))
    }

    fun delete() {
        deletetext.setOnClickListener {
            if (requireArguments().getInt("id") != -1) {
                presenter.delete(Lists.todo[requireArguments().getInt("id")].id)
            }
        }
    }
    override fun goHome() = findNavController().navigate(R.id.toDoListFragment)


    fun starterDeleteText() {

        if (dealtext.text.toString() != "") {
            deletetext.setTextColor(
                ContextCompat.getColor(
                    requireActivity(),
                    R.color.Red
                )
            )
            deleteImg.setImageResource(R.drawable.ic_baseline_deletered_24)
        }
    }

    override fun changeGarbageColor() {
        starterDeleteText()
        dealtext.addTextChangedListener {
            if (dealtext.text.toString() != "" && !arguments?.getBoolean("new")!!) {
                deletetext.setTextColor(
                    ContextCompat.getColor(
                        requireActivity(),
                        R.color.Red
                    )
                )
                deleteImg.setImageResource(R.drawable.ic_baseline_deletered_24)
            } else {
                deletetext.setTextColor(
                    ContextCompat.getColor(
                        requireActivity(),
                        R.color.Label_Disable
                    )
                )
                deleteImg.setImageResource(R.drawable.ic_baseline_delete_24)
            }
        }
        if (dealtext.lineCount == 4) {
            textcard.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        }
    }

    override fun spinnerInitialize() {
        ArrayAdapter.createFromResource(
            requireActivity(),
            R.array.importance_list,

            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }

    }

    override fun close() {
        closeBtn.setOnClickListener {
            findNavController().navigate(R.id.toDoListFragment)
        }
    }

    override fun setIfThatChange(text: String, deadline: String, importance: String) {
        dealtext.setText(text)
        date.text = deadline

        if (importance == getString(R.string.important))
            spinner.setSelection(2)
        if (importance == getString(R.string.basic))
            spinner.setSelection(0)
        if (importance == getString(R.string.low))
            spinner.setSelection(1)
    }
    fun save(){
        save.setOnClickListener {
            if (dealtext.text.toString()!="") {
                if (requireArguments().getBoolean("new"))
                    newItem()
                else changeItem()
            }
        }
    }

    fun spinnerSelectedItemId() : String{
        var important = ""
        var spinnerID = spinner.selectedItemId.toString()
        if (spinnerID == "0")
            important = getString(R.string.basic)
        if (spinnerID == "1")
            important = getString(R.string.low)
        if (spinnerID == "2")
            important = getString(R.string.important)
        return important
    }

    override fun changeItem() {
        val text : String = dealtext.text.toString()
        val deadline : String = date.text.toString()
        val important = spinnerSelectedItemId()
        val id =  Lists.todo[requireArguments().getInt("id")].id

        presenter.changeItem(text,deadline,important,id)
    }

    override fun newItem() {
        val text : String = dealtext.text.toString()
        val deadline : String = date.text.toString()
        val important = spinnerSelectedItemId()

        presenter.newItem(text,deadline,important)
    }

    override fun loading(){}

    override fun dismissLoading(){}

    override fun setDate() {

        val dateSetListener =
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateDateInView(date)
            }

        dateSwitch.setOnCheckedChangeListener { compoundButton, b ->
            if (dateSwitch.isChecked) {
                val dpd = DatePickerDialog(
                    requireActivity(),
                    dateSetListener,
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)
                )
                dpd.datePicker.minDate = cal.time.time
                dpd.show()
            } else
                date.text = ""
        }
    }
    private fun updateDateInView(date: TextView) {
        val myFormat = "dd/MM/yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.FRANCE)
        var PeriodDate = cal.time

        date.text = PeriodDate?.let { sdf.format(it) }
    }

    override fun onStop() {
        super.onStop()
        presenter.onStop()
    }


}

package com.android.kotlinmvvmtodolist.ui.add

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.android.kotlinmvvmtodolist.R
import com.android.kotlinmvvmtodolist.database.TaskEntry
import com.android.kotlinmvvmtodolist.databinding.FragmentAddBinding
import com.android.kotlinmvvmtodolist.viewmodel.AddViewModel
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

class AddFragment : Fragment() {

    private val viewModel: AddViewModel by viewModels()
    lateinit var binding : FragmentAddBinding
    var cal: Calendar = Calendar.getInstance() // Календарь
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        activity?.setTheme(R.style.Theme_date);

         binding = FragmentAddBinding.inflate(inflater)

        setSpinner()
        saveClickListener()
        closeClickListener()
        setDate()
        return binding.root

    }

    fun setDate(){
        val dateSetListener =
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateDateInView(binding.date)
            }

        /** Свитч предназначен для изменения параметра даты  **/
        binding.dateSwitch.setOnCheckedChangeListener { compoundButton, b ->
            if (binding.dateSwitch.isChecked) {
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
                binding.date.text = ""
        }
    }

    fun closeClickListener(){
        binding.closeBtn.setOnClickListener {
            findNavController().navigate(R.id.action_addFragment_to_taskFragment)
        }
    }
    fun saveClickListener(){
      binding.save.setOnClickListener {
            if(TextUtils.isEmpty((binding.dealtext.text))){
                Toast.makeText(requireContext(), "It's empty!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
          viewModel.getList()

            val title_str =binding.dealtext.text.toString()
            val priority = spinnerSelectedItemId()
            var dates : Long = 0
            if (binding.date.text.toString()!="")
                dates = convertDateToLong(binding.date.text.toString())

          var id:Int=0
          viewModel.getAllTasks.observe(viewLifecycleOwner){ it ->

              it.forEach{
                  if (it.ids>id)
                      id=it.ids
              }
              id = if (it.isNotEmpty())
                  id+1
              else
                  0

              viewModel.insert(title_str,priority,dates,dateNow(),"",dateNow(),false,id.toString(),binding.date.text.toString())
          }
            Toast.makeText(requireContext(), "Successfully added!", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_addFragment_to_taskFragment)
        }
    }

    fun setSpinner(){
        val myAdapter = ArrayAdapter<String>(
            requireActivity(),
            android.R.layout.simple_spinner_dropdown_item,
            resources.getStringArray(R.array.importance_list)
        )
        binding.spinner.adapter = myAdapter
    }

    fun dateNow() : Long{
       return convertDateToLong(
            LocalDateTime.now()
            .format(DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm:ss")))
    }

    fun convertDateToLong(date: String): Long {
        val l = LocalDate.parse(date, DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm:ss"))

        val unix = l.atStartOfDay(ZoneId.systemDefault()).toInstant().epochSecond
        return unix
    }
    fun spinnerSelectedItemId() : String{
        var important = ""
        var spinnerID = binding.spinner.selectedItemId.toString()
        if (spinnerID == "0")
            important = getString(R.string.basic)
        if (spinnerID == "1")
            important = getString(R.string.low)
        if (spinnerID == "2")
            important = getString(R.string.important)
        return important
    }
    private fun updateDateInView(date: TextView) {
        val myFormat = "dd/MM/yyyy hh:mm:ss" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.FRANCE)
        var PeriodDate = cal.time

        date.text = PeriodDate?.let { sdf.format(it) }

    }

}
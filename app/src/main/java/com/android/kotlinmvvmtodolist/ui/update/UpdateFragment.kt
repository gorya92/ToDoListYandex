package com.android.kotlinmvvmtodolist.ui.update

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.android.kotlinmvvmtodolist.R
import com.android.kotlinmvvmtodolist.databinding.FragmentUpdateBinding
import com.android.kotlinmvvmtodolist.viewmodel.UpdateViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

class UpdateFragment : Fragment() {

    private val viewModel: UpdateViewModel by viewModels()

    lateinit var binding: FragmentUpdateBinding

    var cal: Calendar = Calendar.getInstance() // Календарь
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        activity?.setTheme(R.style.Theme_date);

        binding = FragmentUpdateBinding.inflate(inflater)

        setSpinner()
        deleteText()
        saveOnClick()
        setPreset()
        closeClick()
        setDate()
        changeGarbageColor()
        return binding.root
    }

    fun setDate() {
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

    fun closeClick() {
        binding.closeBtn.setOnClickListener {
            findNavController().navigate(R.id.action_updateFragment_to_taskFragment)
        }
    }

    fun setPreset() {
        val args = UpdateFragmentArgs.fromBundle(requireArguments())
        binding.dealtext.setText(args.taskEntry.text)

        if (args.taskEntry.importance == getString(R.string.important))
            binding.spinner.setSelection(2)
        if (args.taskEntry.importance == getString(R.string.basic))
            binding.spinner.setSelection(0)
        if (args.taskEntry.importance == getString(R.string.low))
            binding.spinner.setSelection(1)

        if (args.taskEntry.deadline != 0.toLong())
            binding.date.text = convertLongToTime(args.taskEntry.deadline)
    }

    fun saveOnClick() {
        val args = UpdateFragmentArgs.fromBundle(requireArguments())
        binding.save.setOnClickListener {

            if (TextUtils.isEmpty(binding.dealtext.text)) {
                Toast.makeText(requireContext(), "It's empty!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val task_str = binding.dealtext.text.toString()

            var important = ""
            var spinnerID = binding.spinner.selectedItemId.toString()
            if (spinnerID == "0")
                important = getString(R.string.basic)
            if (spinnerID == "1")
                important = getString(R.string.low)
            if (spinnerID == "2")
                important = getString(R.string.important)
            var deadline: Long = 0
            if (binding.date.text != "")
                deadline = convertDateToLong(binding.date.text as String)


            viewModel.update(
                args.taskEntry.ids,
                task_str,
                important,
                deadline,
                args.taskEntry.created_at,
                "",
                dateNow(),
                false,
                args.taskEntry.id,
                binding.date.text.toString()
            )
            Toast.makeText(requireContext(), "Updated!", Toast.LENGTH_SHORT).show()

            findNavController().navigate(R.id.action_updateFragment_to_taskFragment)
        }
    }

    fun deleteText() {
        val args = UpdateFragmentArgs.fromBundle(requireArguments())
        binding.deletetext.setOnClickListener {
            viewModel.delete(args.taskEntry)
            findNavController().navigate(R.id.action_updateFragment_to_taskFragment)
        }
    }

    fun setSpinner() {
        val myAdapter = ArrayAdapter<String>(
            requireActivity(),
            android.R.layout.simple_spinner_dropdown_item,
            resources.getStringArray(R.array.importance_list)
        )
        binding.spinner.adapter = myAdapter
    }

    fun convertLongToTime(time: Long): String {
        val sdf = SimpleDateFormat("dd/MM/yyyy hh:mm:ss")
        val date = java.util.Date(time * 1000)
        return sdf.format(date)
    }

    fun convertDateToLong(date: String): Long {
        val l = LocalDate.parse(date, DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm:ss"))

        val unix = l.atStartOfDay(ZoneId.systemDefault()).toInstant().epochSecond
        return unix
    }

    fun dateNow(): Long {
        return convertDateToLong(
            LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm:ss"))
        )
    }

    private fun updateDateInView(date: TextView) {
        val myFormat = "dd/MM/yyyy hh:mm:ss" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.FRANCE)
        var PeriodDate = cal.time

        date.text = PeriodDate?.let { sdf.format(it) }

    }

    fun changeGarbageColor() {
        if (binding.dealtext.text.toString() != "") {
            binding.deletetext.setTextColor(
                ContextCompat.getColor(
                    requireActivity(),
                    R.color.Red
                )
            )
            binding.deleteImg.setImageResource(R.drawable.ic_baseline_deletered_24)
        }

        binding.dealtext.addTextChangedListener {

            if (binding.dealtext.text.toString() != "" && !arguments?.getBoolean("new")!!) {
                binding.deletetext.setTextColor(
                    ContextCompat.getColor(
                        requireActivity(),
                        R.color.Red
                    )
                )
                binding.deleteImg.setImageResource(R.drawable.ic_baseline_deletered_24)
            } else {
                binding.deletetext.setTextColor(
                    ContextCompat.getColor(
                        requireActivity(),
                        R.color.Label_Disable
                    )
                )
                binding.deleteImg.setImageResource(R.drawable.ic_baseline_delete_24)
            }
        }

        if (binding.dealtext.lineCount == 4) {
            binding.textcard.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        }


    }


}
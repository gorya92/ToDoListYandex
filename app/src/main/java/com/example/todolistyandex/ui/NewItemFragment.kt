package com.example.todolistyandex.ui

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat.getColor
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bignerdranch.android.testing.network.NetworkUtil
import com.bignerdranch.android.testing.retrofitConnect.RetrofitConstants
import com.bignerdranch.android.testing.retrofitConnect.RetrofitViewModel
import com.bignerdranch.android.testing.retrofitConnect.repository.Repository
import com.example.todolistyandex.R
import com.example.todolistyandex.databinding.FragmentNewItemBinding
import com.example.todolistyandex.model.TodoItemRepository
import com.example.todolistyandex.presenters.NewListPresenters
import com.example.todolistyandex.views.NewItemView
import com.example.todolistyandex.model.Todo
import com.example.todolistyandex.model.element
import com.example.todolistyandex.retrofitConnect.repository.RetrofitViewModelFactory
import com.google.android.material.snackbar.Snackbar
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import java.text.SimpleDateFormat
import java.util.*

class NewItemFragment : MvpAppCompatFragment(), NewItemView {

    private lateinit var viewBinding: FragmentNewItemBinding

    @InjectPresenter()
    lateinit var newListPresenters: NewListPresenters


    private lateinit var retrofitViewModel: RetrofitViewModel  // For Retrofit

    var cal: Calendar = Calendar.getInstance() // Календарь


    val bundle = Bundle()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.setTheme(R.style.Theme_date);
        return inflater.inflate(R.layout.fragment_new_item, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        viewBinding = FragmentNewItemBinding.bind(view)


        /** Retrofit connection to project **/
        val repository = Repository()
        val viewModelFactory = RetrofitViewModelFactory(repository)
        retrofitViewModel = ViewModelProvider(this, viewModelFactory)[RetrofitViewModel::class.java]

        newListPresenters.startArr()

        bundle.putBoolean("start", false)

        changeGarbageColor()
        spinnerInitialize()

        if (requireArguments().getInt("id") != -1)
            setIfThatChange()

        viewBinding.closeBtn.setOnClickListener {
            newListPresenters.close()
            findNavController().navigate(R.id.toDoListFragment, bundle)
        }

        setDate()

        viewBinding.save.setOnClickListener {

            if (viewBinding.dealtext.text.toString() != "") {
                if (arguments?.getBoolean("new")!!) {
                    if (NetworkUtil.getConnectivityStatus(requireActivity())) {
                        addItem()
                    }
                    else
                        showInternetSnackBar()
                } else {
                    if (NetworkUtil.getConnectivityStatus(requireActivity())) {
                        putItem(
                            TodoItemRepository.todoList[requireArguments().getInt("id")].id,
                            changeItem(requireArguments().getInt("id"))
                        )
                    }
                    else
                        showInternetSnackBar()
                }
            }
        }

        viewBinding.deletetext.setOnClickListener {

            if (requireArguments().getInt("id") != -1) {
                deleteItem(TodoItemRepository.todoList[requireArguments().getInt("id")].id)
            }
        }

        super.onViewCreated(view, savedInstanceState)
    }


    fun spinnerInitialize() {
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

    fun setDate() {
        val dateSetListener =
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateDateInView(viewBinding.date)
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
    }


    private fun setIfThatChange() {
        val text = TodoItemRepository.todoList[requireArguments().getInt("id")]
        viewBinding.dealtext.setText(text.title)
        viewBinding.date.text = text.deadline

        if (text.importance == getString(R.string.important))
            viewBinding.spinner.setSelection(2)
        if (text.importance == getString(R.string.basic))
            viewBinding.spinner.setSelection(0)
        if (text.importance == getString(R.string.low))
            viewBinding.spinner.setSelection(1)
    }


    override fun changeGarbageColor() {
        if (viewBinding.dealtext.text.toString() != "") {
            viewBinding.deletetext.setTextColor(getColor(requireActivity(), R.color.Red))
            viewBinding.deleteImg.setImageResource(R.drawable.ic_baseline_deletered_24)
        }

        viewBinding.dealtext.addTextChangedListener {

            if (viewBinding.dealtext.text.toString() != "" && !arguments?.getBoolean("new")!!) {
                viewBinding.deletetext.setTextColor(getColor(requireActivity(), R.color.Red))
                viewBinding.deleteImg.setImageResource(R.drawable.ic_baseline_deletered_24)
            } else {
                viewBinding.deletetext.setTextColor(
                    getColor(
                        requireActivity(),
                        R.color.Label_Disable
                    )
                )
                viewBinding.deleteImg.setImageResource(R.drawable.ic_baseline_delete_24)
            }
        }

        if (viewBinding.dealtext.lineCount == 4) {
            viewBinding.textcard.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        }


    }

    override fun setDate(let: String?) {
        viewBinding.date.text = let
    }

    fun changeItem(id: Int): element {
        var important = ""

        var spinnerID = viewBinding.spinner.selectedItemId.toString()
        if (spinnerID == "0")
            important = getString(R.string.basic)
        if (spinnerID == "1")
            important = getString(R.string.low)
        if (spinnerID == "2")
            important = getString(R.string.important)

        return newListPresenters.changeItem(
            id.toString(),
            viewBinding.dealtext.text.toString(),
            important,
            viewBinding.date.text as String,
        )


    }

    fun addItem() {
        var important = ""
        var spinnerID = viewBinding.spinner.selectedItemId.toString()
        if (spinnerID == "0")
            important = getString(R.string.basic)
        if (spinnerID == "1")
            important = getString(R.string.low)
        if (spinnerID == "2")
            important = getString(R.string.important)

        var add = newListPresenters.newItem(
            id.toString(),
            viewBinding.dealtext.text.toString(),
            important,
            viewBinding.date.text as String,
        )
        if (NetworkUtil.getConnectivityStatus(requireActivity())) {
            postRequestToList(add)
        }
        else
            showInternetSnackBar()

    }

    private fun updateDateInView(date: TextView) {
        val myFormat = "dd/MM/yyyy" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.FRANCE)
        var PeriodDate = cal.time

        date.text = PeriodDate?.let { sdf.format(it) }
    }

    private fun postRequestToList(todo: Todo) {
        val element: element = element(todo, RetrofitConstants.REVISION.toString())
        retrofitViewModel.postList(element)
        retrofitViewModel.putItemResponse.observe(viewLifecycleOwner) { res ->
            if (res.isSuccessful) {
                RetrofitConstants.REVISION=  res.body()?.revision!!.toInt()
                findNavController().navigate(R.id.toDoListFragment, bundle)
            } else {
                snackbarShow(res.code().toString())
            }
        }
    }

    private fun deleteItem(id: String) {

        retrofitViewModel.deleteItem(id)
        retrofitViewModel.deleteItem.observe(viewLifecycleOwner) { res ->

            if (res.isSuccessful) {
                RetrofitConstants.REVISION=  res.body()?.revision!!.toInt()
                findNavController().navigate(R.id.toDoListFragment, bundle)
            } else {
                snackbarShow(res.code().toString())
            }
        }
    }

    private fun putItem(id: String, element: element) {

        retrofitViewModel.putItem(id, element)

        retrofitViewModel.putItem.observe(viewLifecycleOwner) { res ->
            if (res.isSuccessful) {
               RetrofitConstants.REVISION=  res.body()?.revision!!.toInt()
                findNavController().navigate(R.id.toDoListFragment, bundle)
            } else {
                snackbarShow(res.code().toString())
            }
        }
    }



    fun snackbarShow(code: String) {
        if (code[0] == '4') {
            Snackbar.make(
                viewBinding.coordinatorTodo,
                R.string.error400,
                Snackbar.LENGTH_SHORT
            )
                .setAction("Ок", View.OnClickListener {

                }).setActionTextColor(getColor(requireActivity(), R.color.white))
                .setTextColor(getColor(requireActivity(), R.color.white))
                .show()
        }
        if (code[0] == '5') {
            Snackbar.make(
                viewBinding.coordinatorTodo,
                R.string.error500,
                Snackbar.LENGTH_SHORT
            )
                .setAction("Ок", View.OnClickListener {

                }).setActionTextColor(getColor(requireActivity(), R.color.white))
                .setTextColor(getColor(requireActivity(), R.color.white))
                .show()
        }

    }
    fun showInternetSnackBar(){
            Snackbar.make(
                viewBinding.coordinatorTodo,
                R.string.noconnection,
                Snackbar.LENGTH_LONG
            ).setAction(R.string.ok, View.OnClickListener {
            }
            ).setActionTextColor(getColor(requireActivity(), R.color.white))
                .setTextColor(getColor(requireActivity(), R.color.white))
                .show()
        }
    }






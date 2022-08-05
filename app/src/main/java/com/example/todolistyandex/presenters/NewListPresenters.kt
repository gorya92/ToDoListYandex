package com.example.todolistyandex.presenters

import android.os.Bundle
import androidx.constraintlayout.motion.utils.ViewState
import com.example.todolistyandex.model.TodoItemRepository
import com.example.todolistyandex.views.NewItemView
import moxy.InjectViewState
import moxy.MvpPresenter
import java.text.SimpleDateFormat
import java.util.*

@InjectViewState()
class NewListPresenters : MvpPresenter<NewItemView>() {
    var cal: Calendar = Calendar.getInstance() // Календарь

    fun calendarGet(){
            val myFormat = "dd/MM/yyyy" // mention the format you need
            val sdf = SimpleDateFormat(myFormat, Locale.FRANCE)
            var PeriodDate = cal.time
            viewState.setData( PeriodDate?.let { sdf.format(it) } )
        }


    }

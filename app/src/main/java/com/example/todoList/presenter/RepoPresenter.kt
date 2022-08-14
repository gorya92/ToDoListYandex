package com.example.todoList.presenter

import android.util.Log
import com.example.todolistyandex.model.Todo
import com.example.todoList.rx.RxThread
import com.example.todoList.api.GithubAPI
import com.example.todoList.di.module.element
import com.example.todoList.model.Lists
import com.example.todoList.util.URL
import io.reactivex.disposables.CompositeDisposable
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import javax.inject.Inject


open class RepoPresenter @Inject constructor(private val api: GithubAPI, private val rxThread: RxThread) {

    private lateinit var view: View
    private val disposable = CompositeDisposable()

    var changeOrAddItem : element = element(Todo(
        changed_at = convertDateToLong(LocalDateTime.now()
            .format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))),
        id = "0" ,
        color = "",
        created_at = convertDateToLong(LocalDateTime.now()
            .format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))),
        deadline = 0,
        done=false,
        importance = "low",
        last_updated_by = "",
        text = ""
    ),"0")

    interface View : BaseView {

        fun goHome()
        fun changeGarbageColor()
        fun setDate()
        fun spinnerInitialize()
        fun close()
        fun setIfThatChange(text : String , deadline : String , importance : String)
        fun changeItem()
        fun newItem()
    }

    fun injectView(view: View) {
        this.view = view
    }


    fun delete(user: String) {
        view.loading()
        disposable.add(api.deleteItem(URL.REVISION.toString(), id = user)
                .compose(rxThread.applyAsync())
                .doOnTerminate { view.dismissLoading() }
                .onErrorReturnItem(changeOrAddItem)
                .subscribe { view.goHome() }
        )
    }





    fun onStop() {
        disposable.clear()
    }

    fun convertLongToTime(time: Long): String {
        val sdf = SimpleDateFormat("dd/MM/yyyy")
        val date = java.util.Date(time * 1000)
        return sdf.format(date)
    }

    fun setIfThatChange(int: Int) {
        val text = Lists.todo[int]
        view.setIfThatChange(text.text,convertLongToTime(text.deadline),text.importance)

    }

    fun changeItem(text: String, deadline: String, important: String, id: String) {
        changeOrAddItem = element(Lists.todo[id.toInt()],URL.REVISION.toString())
        changeOrAddItem.element.text=text
        changeOrAddItem.element.deadline=convertDateToLong(deadline)
        changeOrAddItem.element.importance=important
        changeOrAddItem.element.id= id.toString()
        Log.d("abcabc",URL.REVISION.toString())
        disposable.add(api.putItem(URL.REVISION.toString(),id.toString(),changeOrAddItem)
            .compose(rxThread.applyAsync())
            .doOnTerminate { view.dismissLoading() }
            .onErrorReturnItem(changeOrAddItem)
            .subscribe { view.goHome() }
        )
        Log.d("abcabc",changeOrAddItem.toString())
    }
    fun newItem(text: String, deadline: String, important: String){
        changeOrAddItem.element.text=text
        if (deadline!="")
        changeOrAddItem.element.deadline=convertDateToLong(deadline)

        changeOrAddItem.element.importance=important
        changeOrAddItem.element.id= (Lists.todo[Lists.todo.size-1].id.toInt() + 3).toString()
        changeOrAddItem.element.created_at=convertDateToLong(LocalDateTime.now()
            .format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
        changeOrAddItem.element.changed_at= convertDateToLong(LocalDateTime.now()
            .format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
        disposable.add(api.postElement(URL.REVISION.toString(),changeOrAddItem)
            .compose(rxThread.applyAsync())
            .doOnTerminate { view.dismissLoading() }
            .onErrorReturnItem(changeOrAddItem)
            .subscribe { view.goHome() }
        )
    }

    fun convertDateToLong(date: String): Long {
        val l = LocalDate.parse(date, DateTimeFormatter.ofPattern("dd/MM/yyy"))

        val unix = l.atStartOfDay(ZoneId.systemDefault()).toInstant().epochSecond
        return unix
    }
}

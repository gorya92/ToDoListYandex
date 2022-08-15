package com.example.todoList.presenter

import com.example.todolistyandex.model.Todo
import com.example.todoList.api.GithubAPI
import com.example.todoList.model.Lists
import com.example.todoList.rx.RxThread
import com.example.todoList.util.URL
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject


class GithubUserPresenter @Inject constructor(private val api: GithubAPI,
                                              private val rxThread: RxThread) {

    private lateinit var view: View
    private val disposable = CompositeDisposable()
   var  todoItemList : ArrayList<Todo> = arrayListOf()
    var todoListVisible: ArrayList<Todo> = arrayListOf()
    var visibility: Boolean = false

    interface View : BaseView {
        fun showUserInfo(userInfo: ArrayList<Todo>,visibility: Boolean)

        fun showUserInfoError(message: String?)
        fun setVisibility()
        fun setNoVisibility()
        fun setCompletedText(i: Int)
        fun changeMenuVisibility(visibility: Boolean)

    }

    fun injectView(view: View) {
        this.view = view
    }

    fun getUserInfo() {
        view.loading()
        disposable.add(api.getList()
                .compose(rxThread.applyAsync())
                .doOnTerminate {
                    view.dismissLoading()
                }
                .subscribe({
                    todoItemList=it.list
                    Lists.todo=it.list
                    URL.REVISION=it.revision
                    initializeVisibilityList()
                    view.showUserInfo(todoListVisible,visibility)
                }, {
                    view.showUserInfoError(it.message)
                })
        )
    }

    fun visibleClick(){
        visibility=!visibility
        if (!visibility){
            view.showUserInfo(todoListVisible,visibility)
            view.setNoVisibility()
        }
        else{
            view.showUserInfo(todoItemList,visibility)
            view.setVisibility()
        }
    }

    fun showListChange(){
        if (Lists.todo != todoItemList){
            todoItemList= Lists.todo
            initializeVisibilityList()
            if (!visibility){
                view.showUserInfo(todoListVisible,visibility)
                view.setNoVisibility()
            }
            else{
                view.showUserInfo(todoItemList,visibility)
                view.setVisibility()
            }

        }
    }


    fun onDestroy() {
        disposable.clear()
    }
    private fun initializeVisibilityList() {

        todoListVisible.clear()

        for (i in 0 until todoItemList.size) {
            if (!todoItemList[i].done) {
                todoListVisible.add(todoItemList[i])
            }
        }
        Lists.todoVisibility=todoListVisible
        changeCompleted()
    }

    fun changeCompleted() {
        view.setCompletedText(todoItemList.size - todoListVisible.size)
    }
    fun changeMenuVisibility() {
        view.changeMenuVisibility(visibility)
    }
}

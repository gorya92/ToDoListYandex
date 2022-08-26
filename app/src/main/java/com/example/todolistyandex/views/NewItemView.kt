package com.example.todolistyandex.views

import com.example.yandextask.model.TodoItem
import moxy.MvpView
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(OneExecutionStateStrategy::class)
interface NewItemView: MvpView {
    fun changeGarbageColor()
    fun setData(let: String?)
}
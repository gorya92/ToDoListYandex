package com.example.todolistyandex.views

import moxy.MvpView
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(OneExecutionStateStrategy::class)
interface NewItemView : MvpView {
    fun changeGarbageColor()
    fun setDate(let: String?)
}
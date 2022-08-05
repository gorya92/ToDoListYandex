package com.example.todolistyandex.views

import android.opengl.Visibility
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndStrategy
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.SkipStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(OneExecutionStateStrategy::class)
interface TodoListView : MvpView {
    fun startApp()
    fun setVisibility()
    fun setNoVisibility()
    fun setCompletedText(i: Int)
    fun changeMenuVisibility(visibility: Boolean)

}
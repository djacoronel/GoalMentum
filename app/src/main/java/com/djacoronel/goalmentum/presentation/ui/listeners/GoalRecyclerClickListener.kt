package com.djacoronel.goalmentum.presentation.ui.listeners

/**
 * Created by djacoronel on 10/7/17.
 */
interface GoalRecyclerClickListener {

    fun onClickViewGoal(position: Int)
    fun onClickAddGoal(position: Int)
    fun onLongClickView(position: Int)
}
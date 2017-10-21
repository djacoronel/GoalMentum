package com.djacoronel.goalmentum.presentation.ui.listeners

/**
 * Created by djacoronel on 10/7/17.
 */
interface GoalRecyclerClickListener {

    fun onClickAddGoal()
    fun onClickViewGoal(position: Int)
    fun onClickEditGoal(position: Int)
    fun onClickDeleteGoal(position: Int)
}
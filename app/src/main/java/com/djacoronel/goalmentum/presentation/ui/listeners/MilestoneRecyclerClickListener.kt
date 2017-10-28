package com.djacoronel.goalmentum.presentation.ui.listeners

/**
 * Created by djacoronel on 10/9/17.
 */
interface MilestoneRecyclerClickListener {

    fun onClickExpandMilestone(position: Int)
    fun onClickAddMilestone()
    fun onClickEditMilestone(position: Int)
    fun onClickDeleteMilestone(position: Int)
}
package com.djacoronel.goalmentum.presentation.ui.listeners

/**
 * Created by djacoronel on 10/9/17.
 */
interface MilestoneRecyclerClickListener {

    fun onClickExpandMilestone(position: Int)
    fun onClickCollapseMilestone(position: Int)
    fun onClickAddMilestone(position: Int)
    fun onLongClickMilestone(position: Int)
}
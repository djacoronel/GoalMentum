package com.djacoronel.goalmentum.presentation.presenters

import com.djacoronel.goalmentum.presentation.ui.BaseView



/**
 * Created by djacoronel on 10/9/17.
 */
interface AddGoalPresenter : BasePresenter {

    interface View : BaseView {
        fun onGoalAdded(goalId: Long)
    }

    fun addNewGoal(description: String, duration: String)

    fun addNewGeneralMilestone(goalId: Long)
}
package com.djacoronel.goalmentum.presentation.presenters


/**
 * Created by djacoronel on 10/9/17.
 */
interface AddGoalPresenter {

    interface View {
        fun onGoalAdded(goalId: Long)
    }

    fun addNewGoal(description: String, duration: String)

    fun addNewGeneralMilestone(goalId: Long)
}
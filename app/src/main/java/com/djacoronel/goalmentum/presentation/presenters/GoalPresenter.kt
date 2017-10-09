package com.djacoronel.goalmentum.presentation.presenters

import com.djacoronel.goalmentum.domain.model.Goal
import com.djacoronel.goalmentum.presentation.ui.BaseView

/**
 * Created by djacoronel on 10/7/17.
 */
interface GoalPresenter : BasePresenter {

    interface View : BaseView {

        fun showGoals(goals: List<Goal>)

        fun onClickViewGoal(goalId: Long)

        fun onClickAddGoal(goalId: Long)

        fun onLongClickGoal(goalId: Long)

        fun onGoalDeleted(goal: Goal)
    }

    fun getAllGoals()

    fun deleteGoal(goalId: Long)
}
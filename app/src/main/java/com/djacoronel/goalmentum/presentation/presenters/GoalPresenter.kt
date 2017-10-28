package com.djacoronel.goalmentum.presentation.presenters

import com.djacoronel.goalmentum.domain.model.Goal

/**
 * Created by djacoronel on 10/7/17.
 */
interface GoalPresenter {

    interface View {

        fun showGoals(goals: List<Goal>)

        fun onClickViewGoal(goal: Goal)

        fun onClickAddGoal()

        fun onGoalAdded(goal: Goal)

        fun onClickEditGoal(goal: Goal)

        fun onGoalUpdated(goal: Goal)

        fun onClickDeleteGoal(goal: Goal)

        fun onGoalDeleted(goal: Goal)
    }

    fun getAllGoals()

    fun deleteGoal(goal: Goal)
}
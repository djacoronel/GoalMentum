package com.djacoronel.goalmentum.presentation.presenters

import com.djacoronel.goalmentum.domain.model.Goal

/**
 * Created by djacoronel on 10/7/17.
 */
interface MainPresenter {

    interface View {

        fun showGoals(goals: List<Goal>)

        fun onClickViewGoal(goal: Goal)

        fun onClickAddGoal()

        fun onGoalAdded(goal: Goal)

        fun onSwapGoalPositions(goal1: Goal, goal2: Goal)
    }

    fun getAllGoals()

    fun swapGoalPositions(goal1: Goal, goal2: Goal)
}
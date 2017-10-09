package com.djacoronel.goalmentum.domain.repository

import com.djacoronel.goalmentum.domain.model.Goal

/**
 * Created by djacoronel on 10/6/17.
 */
interface GoalRepository {

    val allGoals: List<Goal>

    val allAchievedGoals: List<Goal>

    fun insert(goal: Goal)

    fun update(goal: Goal)

    fun getGoalById(id: Long): Goal?

    fun markAchieved(goal: Goal)

    fun delete(goal: Goal)
}
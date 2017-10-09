package com.djacoronel.goalmentum.storage.converter

import com.djacoronel.goalmentum.storage.model.Goal
import com.djacoronel.goalmentum.storage.model.Milestone
import com.djacoronel.goalmentum.storage.model.Work


/**
 * Created by djacoronel on 10/6/17.
 */
object GoalStorageModelConverter {

    fun convertToStorageModel(goal: com.djacoronel.goalmentum.domain.model.Goal): Goal {
        val result = Goal()
        result.id = goal.id
        result.description = goal.description
        result.date  = goal.date
        result.duration = goal.duration
        result.achieved = goal.achieved

        return result
    }

    fun convertToDomainModel(goal: Goal): com.djacoronel.goalmentum.domain.model.Goal {
        val id = goal.id
        val description = goal.description
        val date = goal.date
        val duration = goal.duration
        val achieved = goal.achieved

        return com.djacoronel.goalmentum.domain.model.Goal(
                id,
                description!!,
                date!!,
                duration!!,
                achieved
        )
    }

    fun convertListToDomainModel(goals: MutableList<Goal>?): List<com.djacoronel.goalmentum.domain.model.Goal> {
        val convertedGoals = ArrayList<com.djacoronel.goalmentum.domain.model.Goal>()
        goals!!.mapTo(convertedGoals) { convertToDomainModel(it) }
        return convertedGoals
    }

    fun convertListToStorageModel(goals: MutableList<com.djacoronel.goalmentum.domain.model.Goal>?): List<Goal> {
        val convertedGoals = ArrayList<Goal>()
        goals!!.mapTo(convertedGoals) { convertToStorageModel(it) }
        return convertedGoals
    }
}
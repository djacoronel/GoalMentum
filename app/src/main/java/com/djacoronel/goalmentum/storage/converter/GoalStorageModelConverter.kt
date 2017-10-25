package com.djacoronel.goalmentum.storage.converter

import com.djacoronel.goalmentum.storage.model.Goal

/**
 * Created by djacoronel on 10/6/17.
 */

object GoalStorageModelConverter {

    fun convertToStorageModel(goal: com.djacoronel.goalmentum.domain.model.Goal): Goal {
        return Goal(
                goal.id,
                goal.description!!,
                goal.dateCreated!!,
                goal.duration!!,
                goal.achieved,
                goal.momentum,
                goal.momentumDateUpdated!!
        )
    }

    fun convertToDomainModel(goal: Goal): com.djacoronel.goalmentum.domain.model.Goal {
        return com.djacoronel.goalmentum.domain.model.Goal(
                goal.id,
                goal.description!!,
                goal.date!!,
                goal.duration!!,
                goal.achieved,
                goal.momentum,
                goal.momentumDateUpdated!!
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
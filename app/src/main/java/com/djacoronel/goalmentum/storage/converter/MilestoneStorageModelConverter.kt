package com.djacoronel.goalmentum.storage.converter

import com.djacoronel.goalmentum.storage.model.Goal
import com.djacoronel.goalmentum.storage.model.Milestone
import com.djacoronel.goalmentum.storage.model.Work

/**
 * Created by djacoronel on 10/6/17.
 */
object MilestoneStorageModelConverter {

    fun convertToStorageModel(milestone: com.djacoronel.goalmentum.domain.model.Milestone): Milestone {
        val result = Milestone()
        result.id = milestone.id
        result.assignedGoal = milestone.assignedGoal
        result.description = milestone.description
        result.date  = milestone.date
        result.achieved = milestone.achieved

        return result
    }

    fun convertToDomainModel(milestone: Milestone): com.djacoronel.goalmentum.domain.model.Milestone {
        val id = milestone.id
        val assignedGoal = milestone.assignedGoal
        val description = milestone.description
        val date = milestone.date
        val achieved = milestone.achieved

        return com.djacoronel.goalmentum.domain.model.Milestone(
                id,
                assignedGoal,
                description!!,
                date!!,
                achieved
        )
    }

    fun convertListToDomainModel(milestones: MutableList<Milestone>?): List<com.djacoronel.goalmentum.domain.model.Milestone> {
        val convertedMilestones = ArrayList<com.djacoronel.goalmentum.domain.model.Milestone>()
        milestones!!.mapTo(convertedMilestones) { convertToDomainModel(it) }
        return convertedMilestones
    }

    fun convertListToStorageModel(milestones: MutableList<com.djacoronel.goalmentum.domain.model.Milestone>?): List<Milestone> {
        val convertedMilestones = ArrayList<Milestone>()
        milestones!!.mapTo(convertedMilestones) { convertToStorageModel(it) }
        return convertedMilestones
    }
}
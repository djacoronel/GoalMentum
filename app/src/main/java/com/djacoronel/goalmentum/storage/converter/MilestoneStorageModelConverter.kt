package com.djacoronel.goalmentum.storage.converter

import com.djacoronel.goalmentum.storage.model.Milestone

/**
 * Created by djacoronel on 10/6/17.
 */
object MilestoneStorageModelConverter {

    fun convertToStorageModel(milestone: com.djacoronel.goalmentum.domain.model.Milestone): Milestone {
        return Milestone(
                milestone.id,
                milestone.positionInList,
                milestone.assignedGoal,
                milestone.description!!,
                milestone.date!!,
                milestone.achieved
        )
    }

    fun convertToDomainModel(milestone: Milestone): com.djacoronel.goalmentum.domain.model.Milestone {
        return com.djacoronel.goalmentum.domain.model.Milestone(
                milestone.id,
                milestone.positionInList,
                milestone.assignedGoal,
                milestone.description!!,
                milestone.date!!,
                milestone.achieved
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
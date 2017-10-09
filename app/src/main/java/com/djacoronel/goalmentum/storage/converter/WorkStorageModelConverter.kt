package com.djacoronel.goalmentum.storage.converter

import com.djacoronel.goalmentum.storage.model.Work

/**
 * Created by djacoronel on 10/6/17.
 */
object WorkStorageModelConverter {

    fun convertToStorageModel(work: com.djacoronel.goalmentum.domain.model.Work): Work {
        val result = Work()
        result.id = work.id
        result.assignedMilestone = work.assignedMilestone
        result.description = work.description
        result.date  = work.date
        result.achieved = work.achieved

        return result
    }

    fun convertToDomainModel(work: Work): com.djacoronel.goalmentum.domain.model.Work {
        val id = work.id
        val assignedMilestone = work.assignedMilestone
        val description = work.description
        val date = work.date
        val achieved = work.achieved

        return com.djacoronel.goalmentum.domain.model.Work(
                id,
                assignedMilestone,
                description!!,
                date!!,
                achieved
        )
    }

    fun convertListToDomainModel(work: MutableList<Work>?): List<com.djacoronel.goalmentum.domain.model.Work> {
        val convertedWorks = ArrayList<com.djacoronel.goalmentum.domain.model.Work>()
        work!!.mapTo(convertedWorks) { convertToDomainModel(it) }
        return convertedWorks
    }

    fun convertListToStorageModel(work: MutableList<com.djacoronel.goalmentum.domain.model.Work>?): List<Work> {
        val convertedWorks = ArrayList<Work>()
        work!!.mapTo(convertedWorks) { convertToStorageModel(it) }
        return convertedWorks
    }
}
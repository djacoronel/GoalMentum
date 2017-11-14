package com.djacoronel.goalmentum.storage.converter

import com.djacoronel.goalmentum.storage.model.Work

/**
 * Created by djacoronel on 10/6/17.
 */
object WorkStorageModelConverter {

    fun convertToStorageModel(work: com.djacoronel.goalmentum.domain.model.Work): Work {
        return Work(
                work.id,
                work.positionInList,
                work.assignedMilestone,
                work.description!!,
                work.date!!,
                work.achieved,
                work.dateAchieved!!
        )
    }

    fun convertToDomainModel(work: Work): com.djacoronel.goalmentum.domain.model.Work {
        return com.djacoronel.goalmentum.domain.model.Work(
                work.id,
                work.positionInList,
                work.assignedMilestone,
                work.description!!,
                work.date!!,
                work.achieved,
                work.dateAchieved!!
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
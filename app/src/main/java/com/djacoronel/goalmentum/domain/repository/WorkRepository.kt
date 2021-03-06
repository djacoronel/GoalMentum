package com.djacoronel.goalmentum.domain.repository

import com.djacoronel.goalmentum.domain.model.Work

/**
 * Created by djacoronel on 10/6/17.
 */
interface WorkRepository {

    val allWorks: List<Work>

    val allAchievedWorks: List<Work>

    fun insert(work: Work)

    fun update(work: Work)

    fun getWorkById(id: Long): Work?

    fun getWorksByAssignedMilestone(assignedMilestone: Long): List<Work>

    fun markAchieved(work: Work)

    fun delete(work: Work)
}
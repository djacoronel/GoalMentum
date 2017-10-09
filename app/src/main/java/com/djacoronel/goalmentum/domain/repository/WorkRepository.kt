package com.djacoronel.goalmentum.domain.repository

import com.djacoronel.goalmentum.domain.model.Work

/**
 * Created by djacoronel on 10/6/17.
 */
interface WorkRepository {

    val allWorks: List<Work>

    val allAchievedWorks: List<Work>

    fun insert(Work: Work)

    fun update(Work: Work)

    fun getWorkById(id: Long): Work?

    fun getWorksByAssignedMilestone(assignedMilestone: Long): List<Work>

    fun markAchieved(Work: Work)

    fun delete(Work: Work)
}
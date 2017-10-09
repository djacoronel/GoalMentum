package com.djacoronel.goalmentum.storage

import com.djacoronel.goalmentum.domain.model.Milestone
import com.djacoronel.goalmentum.domain.repository.MilestoneRepository
import com.djacoronel.goalmentum.storage.converter.MilestoneStorageModelConverter
import com.djacoronel.goalmentum.storage.model.Milestone_Table
import com.raizlabs.android.dbflow.sql.language.SQLite

/**
 * Created by djacoronel on 10/6/17.
 */
class MilestoneRepositoryImpl() : MilestoneRepository {

    override val allMilestones: List<Milestone>
        get() {

            val milestones = SQLite
                    .select()
                    .from(com.djacoronel.goalmentum.storage.model.Milestone::class.java)
                    .queryList()

            return MilestoneStorageModelConverter.convertListToDomainModel(milestones)
        }

    override val allAchievedMilestones: List<Milestone>
        get() {

            val costs = SQLite
                    .select()
                    .from(com.djacoronel.goalmentum.storage.model.Milestone::class.java)
                    .where(Milestone_Table.achieved.eq(false))
                    .queryList()

            return MilestoneStorageModelConverter.convertListToDomainModel(costs)
        }


    override fun insert(milestone: Milestone) {
        val dbItem = MilestoneStorageModelConverter.convertToStorageModel(milestone)
        dbItem.insert()
    }

    override fun update(milestone: Milestone) {
        val dbItem = MilestoneStorageModelConverter.convertToStorageModel(milestone)
        dbItem.update()
    }

    override fun delete(milestone: Milestone) {
        val dbItem = MilestoneStorageModelConverter.convertToStorageModel(milestone)
        dbItem.delete()
    }

    override fun getMilestoneById(id: Long): Milestone {
        val milestones = SQLite
                .select()
                .from(com.djacoronel.goalmentum.storage.model.Milestone::class.java)
                .where(Milestone_Table.id.eq(id))
                .querySingle()

        return MilestoneStorageModelConverter.convertToDomainModel(milestones!!)
    }

    override fun getMilestonesByAssignedGoal(assignedGoal: Long): List<Milestone> {
        val milestones = SQLite
                .select()
                .from(com.djacoronel.goalmentum.storage.model.Milestone::class.java)
                .where(Milestone_Table.assignedGoal.eq(assignedGoal))
                .queryList()

        return MilestoneStorageModelConverter.convertListToDomainModel(milestones)
    }

    override fun markAchieved(milestone: Milestone) {
        val storageMilestone = MilestoneStorageModelConverter.convertToStorageModel(milestone)
        storageMilestone.achieved = true
        storageMilestone.update()
    }
}
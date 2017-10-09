package com.djacoronel.goalmentum.storage

import com.djacoronel.goalmentum.domain.model.Work
import com.djacoronel.goalmentum.domain.repository.WorkRepository
import com.djacoronel.goalmentum.storage.converter.WorkStorageModelConverter
import com.djacoronel.goalmentum.storage.model.Work_Table
import com.raizlabs.android.dbflow.sql.language.SQLite

/**
 * Created by djacoronel on 10/6/17.
 */
class WorkRepositoryImpl() : WorkRepository {

    override val allWorks: List<Work>
        get() {

            val works = SQLite
                    .select()
                    .from(com.djacoronel.goalmentum.storage.model.Work::class.java)
                    .queryList()

            return WorkStorageModelConverter.convertListToDomainModel(works)
        }

    override val allAchievedWorks: List<Work>
        get() {

            val costs = SQLite
                    .select()
                    .from(com.djacoronel.goalmentum.storage.model.Work::class.java)
                    .where(Work_Table.achieved.eq(false))
                    .queryList()

            return WorkStorageModelConverter.convertListToDomainModel(costs)
        }


    override fun insert(work: Work) {
        val dbItem = WorkStorageModelConverter.convertToStorageModel(work)
        dbItem.insert()
    }

    override fun update(work: Work) {
        val dbItem = WorkStorageModelConverter.convertToStorageModel(work)
        dbItem.update()
    }

    override fun delete(work: Work) {
        val dbItem = WorkStorageModelConverter.convertToStorageModel(work)
        dbItem.delete()
    }

    override fun getWorkById(id: Long): Work {
        val works = SQLite
                .select()
                .from(com.djacoronel.goalmentum.storage.model.Work::class.java)
                .where(Work_Table.id.eq(id))
                .querySingle()

        return WorkStorageModelConverter.convertToDomainModel(works!!)
    }

    override fun getWorksByAssignedMilestone(assignedMilestone: Long): List<Work> {
        val works = SQLite
                .select()
                .from(com.djacoronel.goalmentum.storage.model.Work::class.java)
                .where(Work_Table.assignedMilestone.eq(assignedMilestone))
                .queryList()

        return WorkStorageModelConverter.convertListToDomainModel(works)
    }

    override fun markAchieved(work: Work) {
        val storageWork = WorkStorageModelConverter.convertToStorageModel(work)
        storageWork.achieved = true
        storageWork.update()
    }
}
package com.djacoronel.goalmentum.storage.model

import com.djacoronel.goalmentum.storage.database.WorkDatabase
import com.raizlabs.android.dbflow.annotation.Column
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table
import com.raizlabs.android.dbflow.structure.BaseModel
import java.util.*

/**
 * Created by djacoronel on 10/6/17.
 */
@Table(database = WorkDatabase::class)
class Work : BaseModel {

    @PrimaryKey
    var id: Long = 0

    @Column
    var assignedMilestone: Long = 0

    @Column
    var description: String? = null

    @Column
    var date: Date? = null

    @Column(getterName = "getAchieved")
    var achieved: Boolean = false

    @Column
    var dateAchieved: Date? = null

    constructor()

    constructor(id:Long, assignedMilestone: Long, description: String, date: Date, achieved: Boolean, dateAchieved: Date) {
        this.id = id
        this.assignedMilestone = assignedMilestone
        this.description = description
        this.date = date
        this.achieved = achieved
        this.dateAchieved = dateAchieved
    }

    override fun toString(): String {
        return "Work{" +
                "id=" + id +
                ", assignedMilestone=" + assignedMilestone +
                ", description='" + description + '\'' +
                ", dateCreated=" + date +
                '}'
    }
}

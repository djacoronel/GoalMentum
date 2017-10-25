package com.djacoronel.goalmentum.domain.model

import com.djacoronel.goalmentum.util.DateUtils
import java.util.*

/**
 * Created by djacoronel on 10/6/17.
 */
class Work {
    var id: Long = 0
        private set
    var assignedMilestone: Long = 0
    var description: String? = null
    var date: Date? = null
    var achieved: Boolean = false
    var dateAchieved: Date? = null

    constructor(assignedMilestone: Long, description: String) {
        this.id = Date().time
        this.assignedMilestone = assignedMilestone
        this.description = description
        this.date = DateUtils.today
        this.achieved = false
        this.dateAchieved = DateUtils.createDate(1,1,1)
    }

    constructor(id: Long, assignedMilestone: Long, description: String, date: Date, achieved: Boolean, dateAchieved: Date) {
        this.id = id
        this.assignedMilestone = assignedMilestone
        this.description = description
        this.date = date
        this.achieved = achieved
        this.dateAchieved = dateAchieved
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false

        val work = other as Work?

        return id == work!!.id
    }

    override fun hashCode(): Int {
        return (id xor id.ushr(32)).toInt()
    }

    override fun toString(): String {
        return "Work{" +
                "mId=" + id +
                ", mAssignedMilestone=" + assignedMilestone +
                ", mDescription='" + description + '\'' +
                ", mDate=" + date +
                ", mAchieved=" + achieved +
                '}'
    }
}
package com.djacoronel.goalmentum.domain.model

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

    constructor(assignedMilestone: Long, description: String, date: Date) {
        id = Date().time

        this.assignedMilestone = assignedMilestone
        this.description = description
        this.date = date
        this.achieved = false
    }

    constructor(id: Long, assignedMilestone: Long, description: String, date: Date, achieved: Boolean) {
        this.id = id
        this.assignedMilestone = assignedMilestone
        this.description = description
        this.date = date
        this.achieved = achieved
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false

        val work = o as Work?

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
                '}'
    }
}
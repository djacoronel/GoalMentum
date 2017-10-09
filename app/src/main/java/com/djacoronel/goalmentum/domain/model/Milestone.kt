package com.djacoronel.goalmentum.domain.model

import com.djacoronel.goalmentum.util.DateUtils
import java.util.*

/**
 * Created by djacoronel on 10/6/17.
 */
class Milestone {
    var id: Long = 0
        private set
    var assignedGoal: Long = 0
    var description: String? = null
    var date: Date? = null
    var achieved: Boolean = false

    constructor(assignedGoal: Long, description: String) {
        id = Date().time

        this.assignedGoal = assignedGoal
        this.description = description
        this.date = DateUtils.today
        this.achieved = false
    }

    constructor(id: Long, assignedGoal: Long, description: String, date: Date, achieved: Boolean) {
        this.id = id
        this.assignedGoal = assignedGoal
        this.description = description
        this.date = date
        this.achieved = achieved
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false

        val milestone = o as Milestone?

        return id == milestone!!.id
    }

    override fun hashCode(): Int {
        return (id xor id.ushr(32)).toInt()
    }

    override fun toString(): String {
        return "Milestone{" +
                "mId=" + id +
                ", mAssignedGoal=" + assignedGoal +
                ", mDescription='" + description + '\'' +
                ", mDate=" + date +
                '}'
    }
}
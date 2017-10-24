package com.djacoronel.goalmentum.domain.model

import com.djacoronel.goalmentum.util.DateUtils
import java.util.*
import java.util.concurrent.TimeUnit


/**
 * Created by djacoronel on 10/6/17.
 */
class Goal {
    var id: Long = 0
        private set
    var description: String? = null
    var date: Date? = null
    var duration: String? = null
    var achieved: Boolean = false
    var activeWork: Int = 0
    var achievedWork: Int = 0
    var achievedMilestone: Int = 0

    constructor(description: String, duration: String) {
        id = Date().time

        this.description = description
        this.date = DateUtils.today
        this.duration = duration
        this.achieved = false
    }

    constructor(id: Long, description: String, date: Date, duration: String, achieved: Boolean) {
        this.id = id
        this.description = description
        this.date = date
        this.duration = duration
        this.achieved = achieved
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false

        val goal = other as Goal?

        return id == goal!!.id
    }

    override fun hashCode(): Int {
        return (id xor id.ushr(32)).toInt()
    }

    override fun toString(): String {
        return "Goal{" +
                "mId=" + id +
                ", mDescription='" + description + '\'' +
                ", mDate=" + date +
                ", mDuration=" + duration +
                '}'
    }

    fun getStringRemainingDays(): String{
        val remainingDays = getRemainingDays()
        return when {
            remainingDays.toInt() == 0 -> "(DUE!)"
            remainingDays.toInt() == 1 -> "(" + getRemainingDays() + " day remaining)"
            else -> "(" + getRemainingDays() + " days remaining)"
        }
    }

    private fun getRemainingDays(): Long{
        val currentDate = Date()
        val totalDays = getDifferenceDays(date!!, getEndDate())
        val elapsedDays = getDifferenceDays(date!!,currentDate)
        return totalDays - elapsedDays
    }

    private fun getDifferenceDays(d1: Date, d2: Date): Long {
        val diff = d2.time - d1.time
        return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)
    }

    private fun getEndDate(): Date{
        val cal = Calendar.getInstance()
        val numberOfUnits = duration!!.substringBefore(" ","0").toInt()
        cal.time = date

        when{
            "day" in duration!! -> cal.add(Calendar.DATE,numberOfUnits)
            "week" in duration!! -> cal.add(Calendar.DATE,7 * numberOfUnits)
            "month" in duration!! -> cal.add(Calendar.MONTH,numberOfUnits)
            "year" in duration!! -> cal.add(Calendar.YEAR,numberOfUnits)
        }

        return cal.time
    }
}
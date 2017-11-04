package com.djacoronel.goalmentum.util

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by djacoronel on 10/8/17.
 */
object DateUtils {

    // set the calendar to start of today
    // and get that as a Date
    val today: Date
        get() {

            val c = Calendar.getInstance()
            c.set(Calendar.HOUR_OF_DAY, 0)
            c.set(Calendar.MINUTE, 0)
            c.set(Calendar.SECOND, 0)
            c.set(Calendar.MILLISECOND, 0)

            return c.time
        }

    @SuppressLint("SimpleDateFormat") /**
     * Converts a dateCreated to the textual representation of dates used by people.
     *
     * @param date
     * @return If the dateCreated is of today, then this method will return 'Today's'. If its yesterday then 'Yesterday' is returned.
     * Otherwise it returns the dateCreated in the form of dd.mm
     */
    fun dateToText(date: Date): String {
        var dateCopy = date
        val textDate: String

        // clear hours, minutes and smaller time units from the dateCreated
        dateCopy = truncateHours(dateCopy)

        val c = Calendar.getInstance()

        // set the calendar to start of today
        c.set(Calendar.HOUR_OF_DAY, 0)
        c.set(Calendar.MINUTE, 0)
        c.set(Calendar.SECOND, 0)
        c.set(Calendar.MILLISECOND, 0)

        // and get that as a Date
        val today = c.time

        // get yesterday
        c.add(Calendar.DATE, -1)
        val yesterday = c.time


        textDate = when (dateCopy) {
            today -> "Today"
            yesterday -> "Yesterday"
            else -> formatDate(dateCopy, SimpleDateFormat("dd.MM"))
        }

        return textDate
    }

    fun createDate(year: Int, monthOfYear: Int, dayOfMonth: Int): Date {
        val c = Calendar.getInstance()

        // set the calendar to start of today
        c.set(Calendar.HOUR_OF_DAY, 0)
        c.set(Calendar.MINUTE, 0)
        c.set(Calendar.SECOND, 0)
        c.set(Calendar.MILLISECOND, 0)

        // setup the dateCreated
        c.set(Calendar.YEAR, year)
        c.set(Calendar.MONTH, monthOfYear)
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth)

        // and get that as a Date
        return c.time
    }

    fun formatDate(date: Date): String {
        val sdf = SimpleDateFormat("dd/MM/yy", Locale.US)
        return sdf.format(date)
    }

    fun formatDate(date: Date, sdf: SimpleDateFormat): String {
        return sdf.format(date)
    }

    fun truncateHours(date: Date): Date {
        val c = Calendar.getInstance()

        // set the calendar to start of today
        c.time = date
        c.set(Calendar.HOUR_OF_DAY, 0)
        c.set(Calendar.MINUTE, 0)
        c.set(Calendar.SECOND, 0)
        c.set(Calendar.MILLISECOND, 0)

        // and get that as a Date
        return c.time
    }

    fun convertValueToMondayFirst(dayValue: Int): Int{
        val mapping = listOf(-1,7,1,2,3,4,5,6)
        return mapping[dayValue]
    }

    fun getDayOfWeekLabel(dayValue: Int): String{
        val dayLabels = listOf("Invalid day value", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
        return dayLabels[dayValue]
    }

    fun getCurrentWeek(): List<Date> {
        val calendar = Calendar.getInstance()
        calendar.time = DateUtils.today
        calendar.firstDayOfWeek = Calendar.MONDAY
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)

        val week = mutableListOf<Date>()

        for (i in 0..6) {
            week.add(calendar.time)
            calendar.add(Calendar.DATE, 1)
        }

        return week
    }

    fun getPreviousWeek(): List<Date>{
        val calendar = Calendar.getInstance()
        calendar.time = DateUtils.today
        calendar.firstDayOfWeek = Calendar.MONDAY
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
        calendar.add(Calendar.WEEK_OF_MONTH,-1)

        val week = mutableListOf<Date>()

        for (i in 0..6) {
            week.add(calendar.time)
            calendar.add(Calendar.DATE, 1)
        }

        return week
    }
}
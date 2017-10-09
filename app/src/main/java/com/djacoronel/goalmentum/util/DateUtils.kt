package com.djacoronel.goalmentum.util

import android.content.Context
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

    /**
     * Converts a date to the textual representation of dates used by people.
     *
     * @param date
     * @return If the date is of today, then this method will return 'Today's'. If its yesterday then 'Yesterday' is returned.
     * Otherwise it returns the date in the form of dd.mm
     */
    fun dateToText(context: Context, date: Date): String {
        var date = date
        val textDate: String

        // clear hours, minutes and smaller time units from the date
        date = truncateHours(date)

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


        textDate = when (date) {
            today -> "Today"
            yesterday -> "Yesterday"
            else -> formatDate(date, SimpleDateFormat("dd.MM"))
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

        // setup the date
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
}
package com.djacoronel.goalmentum.storage.database

import com.raizlabs.android.dbflow.annotation.Database

/**
 * Created by djacoronel on 10/6/17.
 */

@Database(name = GoalDatabase.NAME, version = GoalDatabase.VERSION)
object GoalDatabase {
    const val NAME = "Goals_db"
    const val VERSION = 1
}
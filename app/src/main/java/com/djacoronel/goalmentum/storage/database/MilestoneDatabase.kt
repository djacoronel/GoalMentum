package com.djacoronel.goalmentum.storage.database

import com.raizlabs.android.dbflow.annotation.Database

/**
 * Created by djacoronel on 10/6/17.
 */
@Database(name = MilestoneDatabase.NAME, version = MilestoneDatabase.VERSION)
object MilestoneDatabase {
    const val NAME = "Milestones_db"
    const val VERSION = 1
}
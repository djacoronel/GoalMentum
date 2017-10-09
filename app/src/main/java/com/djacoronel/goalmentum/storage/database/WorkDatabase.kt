package com.djacoronel.goalmentum.storage.database

import com.raizlabs.android.dbflow.annotation.Database

/**
 * Created by djacoronel on 10/6/17.
 */
@Database(name = WorkDatabase.NAME, version = WorkDatabase.VERSION)
object WorkDatabase {
    const val NAME = "Work_db"
    const val VERSION = 1
}
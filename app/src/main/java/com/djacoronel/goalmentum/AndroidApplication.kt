package com.djacoronel.goalmentum

import android.app.Application
import com.raizlabs.android.dbflow.config.FlowManager


/**
 * Created by djacoronel on 10/6/17.
 */
class AndroidApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // init database
        FlowManager.init(this)
    }
}
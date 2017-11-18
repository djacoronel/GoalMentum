package com.djacoronel.goalmentum

import android.app.Activity
import android.app.Application
import com.djacoronel.goalmentum.di.DaggerAppComponent
import com.raizlabs.android.dbflow.config.FlowManager
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject


/**
 * Created by djacoronel on 10/6/17.
 */
class MomentumApplication : Application(), HasActivityInjector {
    @Inject
    lateinit var activityDispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    override fun onCreate() {
        super.onCreate()
        FlowManager.init(this)
        DaggerAppComponent
                .builder()
                .application(this)
                .build()
                .inject(this)
    }

    override fun activityInjector(): DispatchingAndroidInjector<Activity>? {
        return activityDispatchingAndroidInjector
    }
}
package com.djacoronel.goalmentum.di

import com.djacoronel.goalmentum.presentation.ui.activities.AddGoalActivity
import com.djacoronel.goalmentum.presentation.ui.activities.GoalActivity
import com.djacoronel.goalmentum.presentation.ui.activities.MainActivity
import com.djacoronel.goalmentum.presentation.ui.activities.MilestoneActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by djacoronel on 11/17/17.
 */
@Module
abstract class ActivityBuilder {
    @ContributesAndroidInjector(modules = arrayOf(FragmentProvider::class))
    abstract fun bindMainActivity(): MainActivity

    @ContributesAndroidInjector(modules = arrayOf(AddGoalActivityModule::class))
    abstract fun bindAddGoalActivity(): AddGoalActivity

    @ContributesAndroidInjector(modules = arrayOf(GoalActivityModule::class))
    abstract fun bindGoalActivity(): GoalActivity

    @ContributesAndroidInjector(modules = arrayOf(MilestoneActivityModule::class))
    abstract fun bindMilestoneActivity(): MilestoneActivity

}
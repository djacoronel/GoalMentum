package com.djacoronel.goalmentum.di.module

import com.djacoronel.goalmentum.presentation.presenters.MilestonePresenter
import com.djacoronel.goalmentum.presentation.presenters.impl.MilestonePresenterImpl
import com.djacoronel.goalmentum.presentation.ui.activities.MilestoneActivity
import dagger.Binds
import dagger.Module

/**
 * Created by djacoronel on 11/18/17.
 */
@Module
abstract class MilestoneActivityModule{
    @Binds
    abstract fun provideView(view: MilestoneActivity): MilestonePresenter.View

    @Binds
    abstract fun provideMilestonePresenter(milestonePresenter: MilestonePresenterImpl): MilestonePresenter
}
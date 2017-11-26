package com.djacoronel.goalmentum.di.module

import com.djacoronel.goalmentum.presentation.presenters.AddGoalPresenter
import com.djacoronel.goalmentum.presentation.presenters.GoalPresenter
import com.djacoronel.goalmentum.presentation.presenters.impl.AddGoalPresenterImpl
import com.djacoronel.goalmentum.presentation.presenters.impl.GoalPresenterImpl
import com.djacoronel.goalmentum.presentation.ui.activities.GoalActivity
import dagger.Binds
import dagger.Module

/**
 * Created by djacoronel on 11/18/17.
 */
@Module
abstract class GoalActivityModule{
    @Binds
    abstract fun provideView(view: GoalActivity): GoalPresenter.View

    @Binds
    abstract fun providePresenter(GoalPresenter: GoalPresenterImpl): GoalPresenter
}
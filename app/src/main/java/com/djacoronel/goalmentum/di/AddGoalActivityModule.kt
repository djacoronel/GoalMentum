package com.djacoronel.goalmentum.di

import com.djacoronel.goalmentum.presentation.presenters.AddGoalPresenter
import com.djacoronel.goalmentum.presentation.presenters.impl.AddGoalPresenterImpl
import com.djacoronel.goalmentum.presentation.ui.activities.AddGoalActivity
import dagger.Binds
import dagger.Module

/**
 * Created by djacoronel on 11/18/17.
 */
@Module
abstract class AddGoalActivityModule{
    @Binds
    abstract fun provideView(view: AddGoalActivity): AddGoalPresenter.View

    @Binds
    abstract fun bindHomePresenter(addGoalsPresenterImpl: AddGoalPresenterImpl): AddGoalPresenter
}
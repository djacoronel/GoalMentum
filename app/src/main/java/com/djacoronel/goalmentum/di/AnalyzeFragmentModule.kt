package com.djacoronel.goalmentum.di

import com.djacoronel.goalmentum.presentation.presenters.AnalyzeGoalsPresenter
import com.djacoronel.goalmentum.presentation.presenters.impl.AnalyzeGoalPresenterImpl
import com.djacoronel.goalmentum.presentation.ui.fragments.ActiveGoalsFragment
import com.djacoronel.goalmentum.presentation.ui.fragments.AnalyzeGoalsFragment
import dagger.Binds
import dagger.Module

/**
 * Created by djacoronel on 11/18/17.
 */
@Module
abstract class AnalyzeFragmentModule{
    @Binds
    abstract fun provideView(view: AnalyzeGoalsFragment): AnalyzeGoalsPresenter.View

    @Binds
    abstract fun bindHomePresenter(analyzeGoalsPresenterImpl: AnalyzeGoalPresenterImpl): AnalyzeGoalsPresenter
}
package com.djacoronel.goalmentum.di.module

import com.djacoronel.goalmentum.presentation.presenters.MainPresenter
import com.djacoronel.goalmentum.presentation.presenters.impl.MainPresenterImpl
import com.djacoronel.goalmentum.presentation.ui.fragments.AchievedGoalsFragment
import dagger.Binds
import dagger.Module

/**
 * Created by djacoronel on 11/18/17.
 */
@Module
abstract class AchievedFragmentModule{
    @Binds
    abstract fun provideView(view: AchievedGoalsFragment): MainPresenter.View

    @Binds
    abstract fun bindHomePresenter(mainPresenterImpl: MainPresenterImpl): MainPresenter
}
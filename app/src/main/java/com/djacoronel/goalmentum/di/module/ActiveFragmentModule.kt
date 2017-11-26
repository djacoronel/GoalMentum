package com.djacoronel.goalmentum.di.module

import com.djacoronel.goalmentum.presentation.presenters.MainPresenter
import com.djacoronel.goalmentum.presentation.presenters.impl.MainPresenterImpl
import com.djacoronel.goalmentum.presentation.ui.fragments.ActiveGoalsFragment
import dagger.Binds
import dagger.Module

/**
 * Created by djacoronel on 11/17/17.
 */
@Module
abstract class ActiveFragmentModule{
    @Binds
    abstract fun provideView(view: ActiveGoalsFragment): MainPresenter.View

    @Binds
    abstract fun bindHomePresenter(mainPresenterImpl: MainPresenterImpl): MainPresenter
}
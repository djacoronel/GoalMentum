package com.djacoronel.goalmentum.di

import com.djacoronel.goalmentum.di.module.AchievedFragmentModule
import com.djacoronel.goalmentum.di.module.ActiveFragmentModule
import com.djacoronel.goalmentum.di.module.AnalyzeFragmentModule
import com.djacoronel.goalmentum.di.scope.FragmentScope
import com.djacoronel.goalmentum.presentation.ui.fragments.AchievedGoalsFragment
import com.djacoronel.goalmentum.presentation.ui.fragments.ActiveGoalsFragment
import com.djacoronel.goalmentum.presentation.ui.fragments.AnalyzeGoalsFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by djacoronel on 11/17/17.
 */
@Module
abstract class FragmentProvider {
    @FragmentScope
    @ContributesAndroidInjector(modules = arrayOf(ActiveFragmentModule::class))
    abstract fun provideActiveFragmentFactory(): ActiveGoalsFragment

    @FragmentScope
    @ContributesAndroidInjector(modules = arrayOf(AnalyzeFragmentModule::class))
    abstract fun provideAnalyzeFragmentFactory(): AnalyzeGoalsFragment

    @FragmentScope
    @ContributesAndroidInjector(modules = arrayOf(AchievedFragmentModule::class))
    abstract fun provideAchievedFragmentFactory(): AchievedGoalsFragment
}
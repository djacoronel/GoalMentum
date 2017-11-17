package com.djacoronel.goalmentum.di

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
    @ContributesAndroidInjector(modules = arrayOf(ActiveFragmentModule::class))
    abstract fun provideActiveFragmentFactory(): ActiveGoalsFragment

    @ContributesAndroidInjector(modules = arrayOf(AnalyzeFragmentModule::class))
    abstract fun provideAnalyzeFragmentFactory(): AnalyzeGoalsFragment

    @ContributesAndroidInjector(modules = arrayOf(AchievedFragmentModule::class))
    abstract fun provideAchievedFragmentFactory(): AchievedGoalsFragment
}
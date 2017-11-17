package com.djacoronel.goalmentum.di

import com.djacoronel.goalmentum.presentation.ui.activities.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by djacoronel on 11/17/17.
 */
@Module
abstract class ActivityBuilder {
    @ContributesAndroidInjector(modules = arrayOf(FragmentProvider::class))
    abstract fun bindMainActivity(): MainActivity
}
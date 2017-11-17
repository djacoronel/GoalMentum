package com.djacoronel.goalmentum.di

import android.app.Application
import android.content.Context
import com.djacoronel.goalmentum.domain.executor.Executor
import com.djacoronel.goalmentum.domain.executor.MainThread
import com.djacoronel.goalmentum.domain.executor.impl.ThreadExecutor
import com.djacoronel.goalmentum.domain.repository.GoalRepository
import com.djacoronel.goalmentum.domain.repository.MilestoneRepository
import com.djacoronel.goalmentum.domain.repository.WorkRepository
import com.djacoronel.goalmentum.storage.GoalRepositoryImpl
import com.djacoronel.goalmentum.storage.MilestoneRepositoryImpl
import com.djacoronel.goalmentum.storage.WorkRepositoryImpl
import com.djacoronel.goalmentum.threading.MainThreadImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by djacoronel on 11/17/17.
 */
@Module
class AppModule {
    @Provides
    @Singleton
    fun provideContext(application: Application): Context = application

    @Provides
    @Singleton
    fun provideExecutor(): Executor = ThreadExecutor.instance

    @Provides
    @Singleton
    fun provideThread(): MainThread = MainThreadImpl.instance

    @Provides
    @Singleton
    fun provideGoalRepo(): GoalRepository = GoalRepositoryImpl()

    @Provides
    @Singleton
    fun provideMilestoneRepo(): MilestoneRepository = MilestoneRepositoryImpl()

    @Provides
    @Singleton
    fun provideWorkRepo(): WorkRepository = WorkRepositoryImpl()
}
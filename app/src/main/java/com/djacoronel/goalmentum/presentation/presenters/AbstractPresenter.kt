package com.djacoronel.goalmentum.presentation.presenters

import com.djacoronel.goalmentum.domain.executor.Executor
import com.djacoronel.goalmentum.domain.executor.MainThread

/**
 * Created by djacoronel on 10/6/17.
 */

abstract class AbstractPresenter(
        protected var mExecutor: Executor,
        protected var mMainThread: MainThread
)
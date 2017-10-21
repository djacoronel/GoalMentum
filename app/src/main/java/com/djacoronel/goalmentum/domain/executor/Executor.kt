package com.djacoronel.goalmentum.domain.executor

import com.djacoronel.goalmentum.domain.interactors.base.AbstractInteractor

/**
 * This executor is responsible for running interactors on background threads.
 *
 *
 * Created by djacoronel on 10/6/17.
 */
interface Executor {

    /**
     * This method should call the interactor's run method and thus start the interactor. This should be called
     * on a background thread as interactors might do lengthy operations.
     *
     * @param interactor The interactor to run.
     */
    fun execute(interactor: AbstractInteractor)
}
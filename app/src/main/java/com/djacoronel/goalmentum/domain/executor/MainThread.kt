package com.djacoronel.goalmentum.domain.executor

/**
 * Created by djacoronel on 10/6/17.
 */

/**
 * This interface will define a class that will enable interactors to run certain operations on the main (UI) thread. For example,
 * if an interactor needs to show an object to the UI this can be used to make sure the show method is called on the UI
 * thread.
 *
 *
 * Created by dmilicic on 7/29/15.
 */
interface MainThread {

    /**
     * Make runnable operation run in the main thread.
     *
     * @param runnable The runnable to run.
     */
    fun post(runnable: Runnable)
}
package com.djacoronel.goalmentum.domain.interactors.base

/**
 * Created by djacoronel on 10/6/17.
 */
interface Interactor {

    /**
     * This is the main method that starts an interactor. It will make sure that the interactor operation is done on a
     * background thread.
     */
    fun execute()
}
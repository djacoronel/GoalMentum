package com.djacoronel.goalmentum.presentation.ui

/**
 * Created by djacoronel on 10/7/17.
 */

interface BaseView {

    /**
     * This is a general method used for showing some kind of progress during a background task. For example, this
     * method should show a progress bar and/or disable buttons before some background work starts.
     */
    fun showProgress()

    /**
     * This is a general method used for hiding progress information after a background task finishes.
     */
    fun hideProgress()

    /**
     * This method is used for showing error messages on the UI.
     *
     * @param message The error message to be dislayed.
     */
    fun showError(message: String)
}
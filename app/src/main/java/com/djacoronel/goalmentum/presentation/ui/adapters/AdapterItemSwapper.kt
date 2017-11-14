package com.djacoronel.goalmentum.presentation.ui.adapters

/**
 * Created by djacoronel on 11/14/17.
 */
interface AdapterItemSwapper{
    fun swapItemPositions(fromPosition: Int, toPosition: Int)
    fun getTotalItems(): Int
}
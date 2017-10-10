package com.djacoronel.goalmentum.presentation.ui.listeners

/**
 * Created by djacoronel on 10/10/17.
 */
interface WorkRecyclerClickListener {
    fun onClickAddWork(position: Int)
    fun onLongClickWork(position: Int)
    fun onClickFinishWork(position: Int)
}
package com.djacoronel.goalmentum.presentation.ui.listeners

/**
 * Created by djacoronel on 10/10/17.
 */
interface WorkRecyclerClickListener {
    fun onClickAddWork(position: Int)
    fun onClickEditWork(position: Int)
    fun onClickDeleteWork(position: Int)
}
package com.djacoronel.goalmentum.util

/**
 * Created by djacoronel on 11/11/17.
 */
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import com.djacoronel.goalmentum.presentation.ui.adapters.AdapterItemSwapper

internal class TouchHelper(private val listAdapter: AdapterItemSwapper) :
        ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP or ItemTouchHelper.DOWN, ItemTouchHelper.LEFT) {

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        listAdapter.swapItemPositions(viewHolder.adapterPosition, target.adapterPosition)
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

    }

    override fun getSwipeDirs(recyclerView: RecyclerView?, holder: RecyclerView.ViewHolder?): Int {
        return 0
    }
}
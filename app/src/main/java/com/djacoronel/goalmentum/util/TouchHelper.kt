package com.djacoronel.goalmentum.util

/**
 * Created by djacoronel on 11/11/17.
 */
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import com.djacoronel.goalmentum.presentation.ui.adapters.GoalItemAdapter

internal class TouchHelper(private val listAdapter: GoalItemAdapter) :
        ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP or ItemTouchHelper.DOWN, ItemTouchHelper.LEFT) {

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        if (viewHolder.adapterPosition != listAdapter.getTotalItems() - 1 &&
                target.adapterPosition != listAdapter.getTotalItems() - 1) {
            listAdapter.swapItemPositions(viewHolder.adapterPosition, target.adapterPosition)
            return true
        } else {
            return false
        }
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

    }

    override fun getSwipeDirs(recyclerView: RecyclerView?, holder: RecyclerView.ViewHolder?): Int {
        return 0
    }
}